package com.tokopedia.shareexperience.data.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.data.util.ShareExRemoteConfigKey
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.toArray
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import org.json.JSONArray
import timber.log.Timber
import javax.inject.Inject

class ShareExChannelMapper @Inject constructor(
    private val resourceProvider: ShareExResourceProvider,
    private val remoteConfig: RemoteConfig
) {

    fun generateSocialMediaChannel(): ShareExChannelModel {
        var socialMediaChannelList = generateSocialMediaChannelList()
        val orderingArray = getSocialMediaOrderingArray()
        socialMediaChannelList = socialMediaChannelList.sortedWith(
            compareBy(
                // First Criterion to mark the non-existent greater than existed value (order asc)
                { orderingArray.indexOf(it.idEnum.id) == -1 },
                // Second Criterion give value of each item
                { orderingArray.indexOf(it.idEnum.id) },
                // Thrid Criterion sort by title in alphabetical order
                { it.title }
            )
        )
        return ShareExChannelModel(
            description = "",
            listChannel = socialMediaChannelList
        )
    }

    private fun getSocialMediaOrderingArray(): Array<String> {
        return try {
            val socialMediaOrdering =
                remoteConfig.getString(ShareExRemoteConfigKey.SOCIAL_MEDIA_ORDERING, "")
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
                icon = IconUnify.WHATSAPP
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.FB_FEED,
                title = resourceProvider.getFacebookFeedChannelTitle(),
                icon = IconUnify.FACEBOOK
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.FB_STORY,
                title = resourceProvider.getFacebookStoryChannelTitle(),
                icon = IconUnify.FACEBOOK_STORY
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_FEED,
                title = resourceProvider.getInstagramFeedChannelTitle(),
                icon = IconUnify.INSTAGRAM
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_STORY,
                title = resourceProvider.getInstagramStoryChannelTitle(),
                icon = IconUnify.INSTAGRAM_STORY
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.IG_DM,
                title = resourceProvider.getInstagramDirectMessageChannelTitle(),
                icon = IconUnify.INSTAGRAM_DM
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.LINE,
                title = resourceProvider.getLineChannelTitle(),
                icon = IconUnify.LINE
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.X_TWITTER,
                title = resourceProvider.getXTwitterChannelTitle(),
                icon = IconUnify.TWITTER
            )
        )
        socialMediaChannelList.add(
            ShareExChannelItemModel(
                idEnum = ShareExChannelEnum.TELEGRAM,
                title = resourceProvider.getTelegramChannelTitle(),
                icon = IconUnify.TELEGRAM
            )
        )
        return socialMediaChannelList
    }

    fun generateDefaultChannel(): ShareExChannelModel {
        val generalChannelList = arrayListOf<ShareExChannelItemModel>()
        generalChannelList.add(
            ShareExChannelItemModel(
                title = resourceProvider.getCopyLinkChannelTitle(),
                icon = IconUnify.LINK
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                title = resourceProvider.getSMSChannelTitle(),
                icon = IconUnify.CHAT
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                title = resourceProvider.getEmailChannelTitle(),
                icon = IconUnify.MESSAGE
            )
        )
        generalChannelList.add(
            ShareExChannelItemModel(
                title = resourceProvider.getOthersChannelTitle(),
                icon = IconUnify.MENU_KEBAB_HORIZONTAL
            )
        )

        return ShareExChannelModel(
            description = "",
            listChannel = generalChannelList
        )
    }
}
