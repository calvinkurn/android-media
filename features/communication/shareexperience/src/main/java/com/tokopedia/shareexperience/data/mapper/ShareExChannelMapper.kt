package com.tokopedia.shareexperience.data.mapper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.data.dto.ShareExChannelListItemResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExChannelResponseDto
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExTelephonyUtil
import com.tokopedia.shareexperience.data.util.toArray
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.user.session.UserSessionInterface
import org.json.JSONArray
import timber.log.Timber
import javax.inject.Inject

open class ShareExChannelMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceProvider: ShareExResourceProvider,
    private val telephony: ShareExTelephonyUtil,
    private val remoteConfig: RemoteConfig,
    private val userSession: UserSessionInterface
) {

    fun mapToSocialMediaChannel(dto: ShareExChannelResponseDto): ShareExChannelModel {
        return mapToChannelModel(
            title = dto.title,
            list = dto.list,
            defaultChannelModel = generateSocialMediaChannel()
        )
    }

    fun mapToCommonChannel(dto: ShareExChannelResponseDto): ShareExChannelModel {
        return mapToChannelModel(
            title = "",
            list = dto.list,
            defaultChannelModel = generateDefaultChannel()
        )
    }

    private fun mapToChannelModel(
        title: String,
        list: List<ShareExChannelListItemResponseDto>,
        defaultChannelModel: ShareExChannelModel
    ): ShareExChannelModel {
        // Convert response list to a map for quick access
        val responseItemMap = list.associateBy { it.channelId }

        // Filtering: Include only channels present in response
        val filteredChannelList = filterChannels(
            responseItemMap = responseItemMap,
            defaultChannelModel = defaultChannelModel
        )

        // Sorting: Based on the order of channels in response list
        val sortedAndFilteredSocialMediaChannelList = sortChannels(
            responseList = list,
            channelList = filteredChannelList
        )

        // Mapping: Update channels as necessary
        val finalSocialMediaChannelList = updateChannels(
            responseItemMap = responseItemMap,
            channelList = sortedAndFilteredSocialMediaChannelList
        )

        return ShareExChannelModel(
            description = title,
            listChannel = finalSocialMediaChannelList
        )
    }

    private fun filterChannels(
        responseItemMap: Map<Long, ShareExChannelListItemResponseDto>,
        defaultChannelModel: ShareExChannelModel
    ): List<ShareExChannelItemModel> {
        return defaultChannelModel.listChannel.filter {
            it.channelEnum.id in responseItemMap.keys
        }
    }

    private fun sortChannels(
        responseList: List<ShareExChannelListItemResponseDto>,
        channelList: List<ShareExChannelItemModel>
    ): List<ShareExChannelItemModel> {
        val indexMap = responseList.mapIndexed { index, item -> item.channelId to index }.toMap()
        return channelList.sortedBy { channelItem ->
            indexMap[channelItem.channelEnum.id] ?: Int.MAX_VALUE
        }
    }

    private fun updateChannels(
        responseItemMap: Map<Long, ShareExChannelListItemResponseDto>,
        channelList: List<ShareExChannelItemModel>
    ): List<ShareExChannelItemModel> {
        return channelList.map { channelItem ->
            responseItemMap[channelItem.channelEnum.id]?.let { responseItem ->
                channelItem.copy(
                    platform = responseItem.platform,
                    imageResolution = responseItem.imageResolution
                )
            } ?: channelItem
        }
    }

    open fun generateSocialMediaChannel(isDefault: Boolean = false): ShareExChannelModel {
        var socialMediaChannelList = generateSocialMediaChannelList()
        val orderingArray = getSocialMediaOrderingArray()
        socialMediaChannelList = socialMediaChannelList.sortedWith(
            compareBy(
                // First Criterion to mark the non-existent greater than existed value (order asc)
                { orderingArray.indexOf(it.channelEnum.appName) == -1 },
                // Second Criterion give value of each item
                { orderingArray.indexOf(it.channelEnum.appName) },
                // Third Criterion sort by title in alphabetical order
                { it.title }
            )
        )
        socialMediaChannelList = socialMediaChannelList.filter {
            isAppInstalled(
                getAppIntent(
                    type = it.mimeType,
                    packageName = it.packageName,
                    actionIntent = it.actionIntent
                )
            )
        }
        if (isDefault) {
            socialMediaChannelList = socialMediaChannelList.filter {
                it.mimeType != ShareExMimeTypeEnum.IMAGE
            }
        }
        return ShareExChannelModel(
            description = "",
            listChannel = socialMediaChannelList
        )
    }

    @SuppressLint("DeprecatedMethod")
    private fun isAppInstalled(intent: Intent): Boolean {
        return try {
            val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.queryIntentActivities(
                    intent,
                    PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
                )
            } else {
                context.packageManager.queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            }
            resolveInfoList.isNotEmpty()
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            ShareExLogger.logExceptionToServerLogger(
                throwable = throwable,
                deviceId = userSession.deviceId,
                description = ::isAppInstalled.name
            )
            false
        }
    }

    private fun getSocialMediaOrderingArray(): Array<String> {
        return try {
            val socialMediaOrdering =
                remoteConfig.getString(
                    ShareExConstants.RemoteConfigKey.SOCIAL_MEDIA_ORDERING,
                    ""
                )
            JSONArray(socialMediaOrdering).toArray()
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            ShareExLogger.logExceptionToServerLogger(
                throwable = throwable,
                deviceId = userSession.deviceId,
                description = ::getSocialMediaOrderingArray.name
            )
            arrayOf()
        }
    }

    protected fun generateSocialMediaChannelList(): List<ShareExChannelItemModel> {
        val socialMediaChannelList = arrayListOf<ShareExChannelItemModel>()
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.WHATSAPP,
                title = resourceProvider.getWhatsappChannelTitle(),
                icon = IconUnify.WHATSAPP,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = ShareExConstants.PackageName.WHATSAPP
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.FB_FEED,
                title = resourceProvider.getFacebookFeedChannelTitle(),
                icon = IconUnify.FACEBOOK,
                mimeType = ShareExMimeTypeEnum.ALL,
                packageName = ShareExConstants.PackageName.FACEBOOK
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.FB_STORY,
                title = resourceProvider.getFacebookStoryChannelTitle(),
                icon = IconUnify.FACEBOOK_STORY,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = ShareExConstants.PackageName.FACEBOOK,
                actionIntent = ShareExConstants.IntentAction.FB_STORY
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.IG_FEED,
                title = resourceProvider.getInstagramFeedChannelTitle(),
                icon = IconUnify.INSTAGRAM,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = ShareExConstants.PackageName.INSTAGRAM,
                actionIntent = ShareExConstants.IntentAction.IG_FEED
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.IG_STORY,
                title = resourceProvider.getInstagramStoryChannelTitle(),
                icon = IconUnify.INSTAGRAM_STORY,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = ShareExConstants.PackageName.INSTAGRAM,
                actionIntent = ShareExConstants.IntentAction.IG_STORY
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.IG_DM,
                title = resourceProvider.getInstagramDirectMessageChannelTitle(),
                icon = IconUnify.INSTAGRAM_DM,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = ShareExConstants.PackageName.INSTAGRAM
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.LINE,
                title = resourceProvider.getLineChannelTitle(),
                icon = IconUnify.LINE,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = ShareExConstants.PackageName.LINE
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.X_TWITTER,
                title = resourceProvider.getXTwitterChannelTitle(),
                icon = IconUnify.SOCIAL_X,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = ShareExConstants.PackageName.TWITTER
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.TELEGRAM,
                title = resourceProvider.getTelegramChannelTitle(),
                icon = IconUnify.TELEGRAM,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = ShareExConstants.PackageName.TELEGRAM
            )
        )
        return socialMediaChannelList
    }

    @SuppressLint("PII Data Exposure")
    open fun generateDefaultChannel(): ShareExChannelModel {
        val generalChannelList = arrayListOf<ShareExChannelItemModel>()
        generalChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.COPY_LINK,
                title = resourceProvider.getCopyLinkChannelTitle(),
                icon = IconUnify.LINK,
                packageName = "", // No need package name, will not use intent
                mimeType = ShareExMimeTypeEnum.NOTHING
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.SMS,
                title = resourceProvider.getSMSChannelTitle(),
                icon = IconUnify.CHAT,
                mimeType = ShareExMimeTypeEnum.ALL,
                packageName = telephony.getSMSPackageName()
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.EMAIL,
                title = resourceProvider.getEmailChannelTitle(),
                icon = IconUnify.MESSAGE,
                mimeType = ShareExMimeTypeEnum.ALL,
                packageName = ShareExConstants.PackageName.GMAIL
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.OTHERS,
                title = resourceProvider.getOthersChannelTitle(),
                icon = IconUnify.MENU_KEBAB_HORIZONTAL,
                packageName = "", // No need package name, will not use intent
                mimeType = ShareExMimeTypeEnum.TEXT
            )
        )

        return ShareExChannelModel(
            description = "",
            listChannel = generalChannelList
        )
    }

    private fun getAppIntent(
        type: ShareExMimeTypeEnum,
        packageName: String?,
        actionIntent: String
    ): Intent {
        return Intent(actionIntent).apply {
            setType(type.textType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(packageName)
        }
    }
}
