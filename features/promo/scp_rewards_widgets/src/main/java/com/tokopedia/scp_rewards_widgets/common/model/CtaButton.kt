package com.tokopedia.scp_rewards_widgets.common.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CtaButton(
        val unifiedStyle: String? = null,
        val text: String? = null,
        val appLink: String? = null,
        val url: String? = null,
        val isAutoApply: Boolean? = false,
        val couponCode: String? = null
) : Parcelable
