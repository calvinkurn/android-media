package com.tokopedia.feedcomponent.people.mapper

import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase

interface ProfileMutationMapper {
    fun mapFollow(response: ProfileDoFollowModelBase): MutationUiModel
    fun mapUnfollow(response: ProfileDoUnFollowModelBase): MutationUiModel
}
