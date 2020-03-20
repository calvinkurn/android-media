package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileDob(@SerializedName("userId")
                          val userId: Long,
                          @SerializedName("age")
                          val age: Int,
                          @SerializedName("bday")
                          val bday: String,
                          @SerializedName("isDobExist")
                          val isDobExist: Boolean,
                          @SerializedName("isDobVerified")
                          val isDobVerified: Boolean,
                          @SerializedName("isAdult")
                          val isAdult: Boolean,
                          @SerializedName("error")
                          val error: String)