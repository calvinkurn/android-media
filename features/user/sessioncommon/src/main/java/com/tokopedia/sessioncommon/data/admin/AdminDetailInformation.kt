package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminDetailInformation(
    @Expose
    @SerializedName("admin_role_type")
    val roleType: AdminRoleType = AdminRoleType()
)