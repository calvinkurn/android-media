package com.tokopedia.scp_rewards_widgets.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.scp_rewards_widgets.coupon_list.CouponListViewTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedalBenefitModel(
        val title: String? = null,
        val isActive: Boolean = false,
        val status: String? = null,
        val url: String? = null,
        val appLink: String? = null,
        val tncList: List<String>? = null,
        val medaliImageURL: String? = null,
        val podiumImageURL: String? = null,
        val backgroundImageURL: String? = null,
        val statusBadgeText: String? = null,
        val statusBadgeColor: String? = null,
        val statusDescription: String? = null,
        val additionalInfoText: String? = null,
        val additionalInfoColor: String? = null,
        val typeImageURL: String? = null,
        val typeBackgroundColor: String? = null,
        val cta: CtaButton? = null
) : Visitable<CouponListViewTypeFactory>, Parcelable {

    var statusBadgeEnabled: Boolean = true
    var isLoading: Boolean = false
    override fun type(typeFactory: CouponListViewTypeFactory): Int = typeFactory.type(this)
}
