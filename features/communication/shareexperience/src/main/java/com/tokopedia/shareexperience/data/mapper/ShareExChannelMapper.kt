package com.tokopedia.shareexperience.data.mapper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.toArray
import com.tokopedia.shareexperience.domain.ShareExConstants
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import org.json.JSONArray
import timber.log.Timber
import javax.inject.Inject

class ShareExChannelMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceProvider: ShareExResourceProvider,
    private val remoteConfig: RemoteConfig
) {

    fun generateSocialMediaChannel(): ShareExChannelModel {
        var socialMediaChannelList = generateSocialMediaChannelList()
        val orderingArray = getSocialMediaOrderingArray()
        socialMediaChannelList = socialMediaChannelList.sortedWith(
            compareBy(
                // First Criterion to mark the non-existent greater than existed value (order asc)
                { orderingArray.indexOf(it.idEnum.appName) == -1 },
                // Second Criterion give value of each item
                { orderingArray.indexOf(it.idEnum.appName) },
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
            arrayOf()
        }
    }

    private fun generateSocialMediaChannelList(): List<ShareExChannelItemModel> {
        val socialMediaChannelList = arrayListOf<ShareExChannelItemModel>()
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.WHATSAPP,
                title = resourceProvider.getWhatsappChannelTitle(),
                icon = IconUnify.WHATSAPP,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "com.whatsapp"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.FB_FEED,
                title = resourceProvider.getFacebookFeedChannelTitle(),
                icon = IconUnify.FACEBOOK,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = "com.facebook.katana"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.FB_STORY,
                title = resourceProvider.getFacebookStoryChannelTitle(),
                icon = IconUnify.FACEBOOK_STORY,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "com.facebook.katana",
                actionIntent = "com.facebook.stories.ADD_TO_STORY"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_FEED,
                title = resourceProvider.getInstagramFeedChannelTitle(),
                icon = IconUnify.INSTAGRAM,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "com.instagram.android",
                actionIntent = "com.instagram.share.ADD_TO_FEED"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_STORY,
                title = resourceProvider.getInstagramStoryChannelTitle(),
                icon = IconUnify.INSTAGRAM_STORY,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "com.instagram.android",
                actionIntent = "com.instagram.share.ADD_TO_STORY"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_DM,
                title = resourceProvider.getInstagramDirectMessageChannelTitle(),
                icon = IconUnify.INSTAGRAM_DM,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = "com.instagram.android",
                actionIntent = "com.instagram.share.ADD_TO_FEED"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.LINE,
                title = resourceProvider.getLineChannelTitle(),
                icon = IconUnify.LINE,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = "jp.naver.line.android"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.X_TWITTER,
                title = resourceProvider.getXTwitterChannelTitle(),
                icon = IconUnify.TWITTER,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "com.twitter.android"
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.TELEGRAM,
                title = resourceProvider.getTelegramChannelTitle(),
                icon = IconUnify.TELEGRAM,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "org.telegram.messenger"
            )
        )
        return socialMediaChannelList
    }

    @SuppressLint("PII Data Exposure")
    fun generateDefaultChannel(): ShareExChannelModel {
        val generalChannelList = arrayListOf<ShareExChannelItemModel>()
        generalChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.COPY_LINK,
                title = resourceProvider.getCopyLinkChannelTitle(),
                icon = IconUnify.LINK,
                mimeType = ShareExMimeTypeEnum.NOTHING
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.SMS,
                title = resourceProvider.getSMSChannelTitle(),
                icon = IconUnify.CHAT,
                mimeType = ShareExMimeTypeEnum.TEXT
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.EMAIL,
                title = resourceProvider.getEmailChannelTitle(),
                icon = IconUnify.MESSAGE,
                mimeType = ShareExMimeTypeEnum.IMAGE
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.OTHERS,
                title = resourceProvider.getOthersChannelTitle(),
                icon = IconUnify.MENU_KEBAB_HORIZONTAL,
                mimeType = ShareExMimeTypeEnum.NOTHING
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
            setType(type.nameType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(packageName)
        }
    }
}
