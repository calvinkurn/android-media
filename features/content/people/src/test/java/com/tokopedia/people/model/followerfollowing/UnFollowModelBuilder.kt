package com.tokopedia.people.model.followerfollowing

import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedData
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedDataVal
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class UnFollowModelBuilder {

    fun buildModel(
        userIdSource: String,
        userIdTarget: String,
        relation: String = "",
        isSuccess: String = "1",
    ): ProfileDoUnFollowModelBase {
        return ProfileDoUnFollowModelBase(
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
