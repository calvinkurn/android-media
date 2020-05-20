package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 11/05/20.
 */

data class SetInjectCouponTimeBased (
        @SerializedName("SetInjectCouponTimeBased") @Expose
        val data: InjectCouponTimeBased = InjectCouponTimeBased()
)

data class InjectCouponTimeBased(
        @SerializedName("is_success") @Expose
        val isSuccess: Boolean = false,
        @SerializedName("error_message") @Expose
        val errorMessage: String = ""
)