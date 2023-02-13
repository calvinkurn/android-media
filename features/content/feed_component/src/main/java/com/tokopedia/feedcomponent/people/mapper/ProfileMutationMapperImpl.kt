package com.tokopedia.feedcomponent.people.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase
import javax.inject.Inject
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.people.model.MutationUiModel

class ProfileMutationMapperImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): ProfileMutationMapper {


    override fun mapFollow(response: ProfileDoFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(errorCode.isEmpty()) MutationUiModel.Success()
            else MutationUiModel.Error(
                messages.firstOrNull() ?: context.getString(R.string.up_error_follow)
            )
        }
    }

    override fun mapUnfollow(response: ProfileDoUnFollowModelBase): MutationUiModel {
        return with(response.profileFollowers) {
            if(data.isSuccess == SUCCESS_UNFOLLOW_CODE) MutationUiModel.Success()
            else MutationUiModel.Error(
                messages.firstOrNull() ?: context.getString(R.string.up_error_unfollow)
            )
        }
    }

    companion object {
        private const val SUCCESS_UNFOLLOW_CODE = "1"
    }
}
