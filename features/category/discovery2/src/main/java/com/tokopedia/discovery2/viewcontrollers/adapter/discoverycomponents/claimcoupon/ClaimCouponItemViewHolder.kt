package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DIKLAIM
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.HABIS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.KLAIM
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class ClaimCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var claimCouponItemViewModel: ClaimCouponItemViewModel
    private val claimCouponImage: ImageView = itemView.findViewById(R.id.appCompatImageView)
    private val claimCouponImageDouble: ImageView = itemView.findViewById(R.id.appCompatImageViewDouble)
    private val claimBtn: Typography = itemView.findViewById(R.id.claim_btn)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        claimCouponItemViewModel = discoveryBaseViewModel as ClaimCouponItemViewModel
        claimCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer {
            setData(it, claimCouponItemViewModel.getIsDouble())
        })

    }

    private fun setData(dataItem: DataItem?, isDouble: Boolean) {
        if (isDouble) {
            claimCouponImageDouble.show()
            claimCouponImage.hide()
            claimCouponImageDouble.loadImage(dataItem?.smallImageUrlMobile ?: "")
        } else {
            claimCouponImageDouble.hide()
            claimCouponImage.show()
            claimCouponImage.loadImage(dataItem?.imageUrlMobile ?: "")
        }

        setBtn(dataItem?.status)
        itemView.setOnClickListener {
            claimCouponItemViewModel.setClick(itemView.context, dataItem?.status)
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventClickCoupon(dataItem, adapterPosition, isDouble)
        }

        claimBtn.setOnClickListener {
            claimCouponItemViewModel.redeemCoupon()
            claimCouponItemViewModel.getRedeemCouponCode().observe(fragment.viewLifecycleOwner, Observer { item ->
                try {
                    if (item == NOT_LOGGEDIN) {
                        Toaster.make(itemView.rootView, itemView.context.getString(R.string.discovery_please_log_in), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, itemView.context.getString(R.string.discovery_login), View.OnClickListener {
                            RouteManager.route(itemView.context, ApplinkConst.LOGIN)
                        })
                    } else {
                        setBtn(DIKLAIM)
                        Toaster.make(itemView.rootView, itemView.context.getString(R.string.claim_coupon_redeem_coupon_msg),
                                Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, itemView.context.getString(R.string.claim_coupon_lihat_text), View.OnClickListener {

                            claimCouponItemViewModel.getCouponSlug()?.let { slug ->
                                val applink = GenerateUrl.getClaimCouponApplink(slug)
                                claimCouponItemViewModel.navigate(itemView.context, applink)
                            }
                        })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickClaimCoupon(dataItem?.title, dataItem?.slug)
        }
    }

    private fun setBtn(status: String?) {
        claimBtn.text = if (status == HABIS || status == null)
            HABIS
        else
            status
        claimBtn.isEnabled = status == KLAIM
        if (claimBtn.isEnabled)
            claimBtn.setTextColor(MethodChecker.getColor(itemView.context, R.color.white))
        else
            claimBtn.setTextColor(MethodChecker.getColor(itemView.context, R.color.voucher_text_color_disable))
    }

}