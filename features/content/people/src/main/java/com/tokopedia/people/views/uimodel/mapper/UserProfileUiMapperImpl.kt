package com.tokopedia.people.views.uimodel.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.people.R
import com.tokopedia.people.model.*
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.data.pojo.shoprecom.UserShopRecomModel
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.profile.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
class UserProfileUiMapperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : UserProfileUiMapper {

    override fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel {
        return ProfileUiModel(
            userID = response.profileHeader.profile.userID,
            encryptedUserID = response.profileHeader.profile.encryptedUserID,
            imageCover = response.profileHeader.profile.imageCover,
            name = response.profileHeader.profile.name,
            username = response.profileHeader.profile.username,
            biography = response.profileHeader.profile.biography.replace("\n", "<br />"),
            badges = response.profileHeader.profile.badges,
            stats = ProfileStatsUiModel(
                totalPostFmt = response.profileHeader.stats.totalPostFmt,
                totalFollowerFmt = response.profileHeader.stats.totalFollowerFmt,
                totalFollowingFmt = response.profileHeader.stats.totalFollowingFmt,
            ),
            shareLink = LinkUiModel(
                webLink = response.profileHeader.profile.sharelink.weblink,
                appLink = response.profileHeader.profile.sharelink.applink,
            ),
            liveInfo = LivePlayChannelUiModel(
                isLive = response.profileHeader.profile.liveplaychannel.islive,
                channelId = response.profileHeader.profile.liveplaychannel.liveplaychannelid,
                channelLink = LinkUiModel(
                    webLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.weblink,
                    appLink = response.profileHeader.profile.liveplaychannel.liveplaychannellink.applink,
                )
            )
        )
    }

    override fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel {
        return FollowInfoUiModel(
            userID = response.profileHeader.items.firstOrNull()?.userID ?: "",
            encryptedUserID = response.profileHeader.items.firstOrNull()?.encryptedUserID ?: "",
            status = response.profileHeader.items.firstOrNull()?.status ?: false,
        )
    }

    override fun mapUserWhitelist(response: WhitelistQuery): ProfileWhitelistUiModel {
        val authorUgc = response.whitelist.authors.find { it.type == Author.TYPE_USER }

        return ProfileWhitelistUiModel(
            isWhitelist = authorUgc != null,
            hasUsername = authorUgc?.post?.hasUsername ?: false,
            hasAcceptTnc = authorUgc?.post?.hasAcceptTnc ?: false,
        )
    }

    override fun mapFollow(response: ProfileDoFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(errorCode.isEmpty()) MutationUiModel.Success()
            else MutationUiModel.Error(messages.firstOrNull() ?: context.getString(R.string.up_error_follow))
        }
    }

    override fun mapUnfollow(response: ProfileDoUnFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(data.isSuccess == SUCCESS_UNFOLLOW_CODE) MutationUiModel.Success()
            else MutationUiModel.Error(messages.firstOrNull() ?: context.getString(R.string.up_error_unfollow))
        }
    }

    override fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel {
        return with(response.playToggleChannelReminder) {
            if(header.status == SUCCESS_UPDATE_REMINDER_CODE) MutationUiModel.Success(header.message)
            else MutationUiModel.Error(header.message)
        }
    }

    override fun mapShopRecom(response: UserShopRecomModel): ShopRecomUiModel {
        return with(response.feedXRecomWidget) {
            ShopRecomUiModel(
                isShown = isShown,
                items = items.map {
                  ShopRecomUiModelItem(
                      badgeImageURL = it.badgeImageURL,
                      encryptedID = it.encryptedID,
                      id = it.id,
                      logoImageURL = it.logoImageURL,
                      name = it.name,
                      nickname = it.nickname,
                      type = it.type,
                      applink = it.applink,
                  )
                },
                nextCursor = nextCursor,
                title = title,
            )
        }
    }

    override fun mapShopFollow(response: ShopFollowModel): MutationUiModel {
        return with(response.followShop) {
            if (success) MutationUiModel.Success()
            else MutationUiModel.Error(message)
        }
    }

    companion object {
        private const val SUCCESS_UPDATE_REMINDER_CODE = 200
        private const val SUCCESS_UNFOLLOW_CODE = "1"
    }
}