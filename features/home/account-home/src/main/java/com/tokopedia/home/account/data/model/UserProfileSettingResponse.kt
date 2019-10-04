package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileSettingResponse(@SerializedName("userProfileSetting")
                                      val userProfileSetting: UserProfileSetting)