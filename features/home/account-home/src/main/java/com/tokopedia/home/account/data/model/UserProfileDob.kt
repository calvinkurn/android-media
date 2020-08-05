package com.tokopedia.home.account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileDob(
      @SerializedName("userId")
      @Expose
      val userId: Long,

      @SerializedName("age")
      @Expose
      val age: Int,

      @SerializedName("bday")
      @Expose
      val bday: String,

      @SerializedName("isDobExist")
      @Expose
      val isDobExist: Boolean,

      @SerializedName("isDobVerified")
      @Expose
      val isDobVerified: Boolean,

      @SerializedName("isAdult")
      @Expose
      val isAdult: Boolean,

      @SerializedName("error")
      @Expose
      val error: String
)