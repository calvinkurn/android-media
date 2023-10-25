package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoucherShop(
        @SerializedName("benefit_type")
        @Expose
        val benefitType: String? = null,
        @SerializedName("benefit_value")
        @Expose
        val benefitValue: Int = 0,
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
        @SerializedName("max_limit")
        @Expose
        val maxLimit: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("period_lang_en")
        @Expose
        val periodLangEN: String = "",
        @SerializedName("period_lang_id")
        @Expose
        val periodLangID: String = "",
        @SerializedName("remaining_days")
        @Expose
        val remainingDays: Int = 0): Parcelable
