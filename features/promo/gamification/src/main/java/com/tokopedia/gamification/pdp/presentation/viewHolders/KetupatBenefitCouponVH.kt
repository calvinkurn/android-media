package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponItemVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatBenefitCouponVH(itemView: View) : AbstractViewHolder<KetupatBenefitCouponVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_benefit_coupon
    }

    override fun bind(element: KetupatBenefitCouponVHModel?) {
        element?.benefitCoupon.let { couponData ->
            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_title)?.text =
                couponData?.title

            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_subtitle)?.text =
                couponData?.text?.find { it?.key == "SUBTITLE" }?.value

            itemView.findViewById<Typography>(gamificationR.id.coupon_banner_subtitle)?.apply {
                setOnClickListener {
                    RouteManager.route(
                        context,
                        couponData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
            }
        }
        element?.value.let {
            val tempList: ArrayList<Visitable<KetupatLandingTypeFactory>> = ArrayList()
            it?.tokopointsCouponList?.tokopointsCouponData?.forEach{
                it?.let { it1 -> KetupatBenefitCouponItemVHModel(it1) }
                    ?.let { it2 -> tempList.add(it2) }
            }
            val adapter = KetupatLandingAdapter(
                KetupatLandingAdapterTypeFactory())
            adapter.addMoreData(tempList)
            itemView.findViewById<RecyclerView>(gamificationR.id.rv_benefit_coupon).adapter = adapter
        }


    }
}
