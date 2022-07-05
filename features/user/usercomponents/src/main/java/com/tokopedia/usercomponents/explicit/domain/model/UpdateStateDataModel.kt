package com.tokopedia.usercomponents.explicit.domain.model

import com.google.gson.annotations.SerializedName

data class ExplicitprofileUpdateUserState(

    @SerializedName("configActiveSuccess")
    val configActiveSuccess: Boolean = false,

    @SerializedName("errorCode")
    val errorCode: String = "",

    @SerializedName("templateActiveSuccess")
    val templateActiveSuccess: Boolean = false,

    @SerializedName("message")
    val message: String = ""
)

data class UpdateStateDataModel(

    @SerializedName("explicitprofileUpdateUserState")
    val explicitprofileUpdateUserState: ExplicitprofileUpdateUserState = ExplicitprofileUpdateUserState()
)