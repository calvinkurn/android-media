package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.people.model.*
import com.tokopedia.people.views.uimodel.MutationUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserProfileUiMapper @Inject constructor() {

    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel {
        return ProfileUiModel(
            userID = response.profileHeader.profile.userID,
            encryptedUserID = response.profileHeader.profile.encryptedUserID,
            imageCover = response.profileHeader.profile.imageCover,
            name = response.profileHeader.profile.name,
            username = response.profileHeader.profile.username,
            biography = response.profileHeader.profile.biography.replace("\n", "<br />"),
            badges = response.profileHeader.profile.badges,
            stats = ProfileStatsUiModel(
                totalPost = response.profileHeader.stats.totalPost,
                totalPostFmt = response.profileHeader.stats.totalPostFmt,
                totalFollower = response.profileHeader.stats.totalFollower,
                totalFollowerFmt = response.profileHeader.stats.totalFollowerFmt,
                totalFollowing = response.profileHeader.stats.totalFollowing,
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

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel {
        return FollowInfoUiModel(
            userID = response.profileHeader.items.firstOrNull()?.userID ?: "",
            encryptedUserID = response.profileHeader.items.firstOrNull()?.encryptedUserID ?: "",
            status = response.profileHeader.items.firstOrNull()?.status ?: false,
        )
    }

    fun mapUserWhitelist(response: WhitelistQuery): ProfileWhitelistUiModel {
        return ProfileWhitelistUiModel(
            isWhitelist = response.whitelist.isWhitelist,
            hasUsername = response.whitelist.authors.find { it.type == Author.TYPE_USER }?.post?.hasUsername ?: false,
            hasAcceptTnc = response.whitelist.authors.find { it.type == Author.TYPE_USER }?.post?.hasAcceptTnc ?: false,
        )
    }

    fun mapFollow(response: ProfileDoFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(errorCode.isEmpty()) MutationUiModel.Success()
            else MutationUiModel.Error(messages.firstOrNull() ?: "Gagal follow akun")
        }
    }

    fun mapUnfollow(response: ProfileDoUnFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(data.isSuccess == SUCCESS_UNFOLLOW_CODE) MutationUiModel.Success()
            else MutationUiModel.Error(messages.firstOrNull() ?: "Gagal unfollow akun")
        }
    }

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel {
        return with(response.playToggleChannelReminder) {
            if(header.status == SUCCESS_UPDATE_REMINDER_CODE) MutationUiModel.Success(header.message)
            else MutationUiModel.Error(header.message)
        }
    }

    companion object {
        private const val SUCCESS_UPDATE_REMINDER_CODE = 200
        private const val SUCCESS_UNFOLLOW_CODE = "1"
    }
}