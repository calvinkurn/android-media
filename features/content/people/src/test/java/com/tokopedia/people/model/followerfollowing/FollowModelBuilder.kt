package com.tokopedia.people.model.followerfollowing

import com.tokopedia.people.model.ProfileDoFollowModelBase
import com.tokopedia.people.model.ProfileDoFollowedData
import com.tokopedia.people.model.ProfileDoFollowedDataVal

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
            )
        )
    }
}