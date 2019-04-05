package com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterUsernameData (
    @SerializedName("bymeRegisterAffiliateName")
    @Expose
    var bymeRegisterAffiliateName: BymeRegisterAffiliateName = BymeRegisterAffiliateName()
)
