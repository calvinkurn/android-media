package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileSetting(@SerializedName("safeMode")
                              val safeMode: Boolean)