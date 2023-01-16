package com.tokopedia.people.model.followerfollowing

import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedData
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedDataVal

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowModelBuilder {

    fun buildModel(
        userIdSource: String,
        userIdTarget: String,
        relation: String = "",
        isSuccess: String = "1",
    ): ProfileDoFollowModelBase {
        return ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data = ProfileDoFollowedDataVal(
                    userIdSource = userIdSource,
                    userIdTarget = userIdTarget,
                    relation = relation,
                    isSuccess = isSuccess,
                ),
                messages = emptyList(),
                errorCode = "",
            ),
        )
    }
}
