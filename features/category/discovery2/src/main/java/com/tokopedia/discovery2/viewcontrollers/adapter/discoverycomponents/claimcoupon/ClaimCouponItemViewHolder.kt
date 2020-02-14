package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.ClaimCouponConstant
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class ClaimCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var claimCouponItemViewModel: ClaimCouponItemViewModel
    private val claimCouponImage: ImageView = itemView.findViewById(R.id.appCompatImageView)
    private val claimCouponLabel: Typography = itemView.findViewById(R.id.claim_coupon_label)
    private val claimCouponLabelSubTitle: Typography = itemView.findViewById(R.id.claim_coupon_label_subtitle)
    private val claimCouponTitle: Typography = itemView.findViewById(R.id.claim_coupon_title)
    private val claimCouponSubTitle: Typography = itemView.findViewById(R.id.claim_coupon_title_sub_title)
    private val claimBtn: Typography = itemView.findViewById(R.id.claim_btn)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        claimCouponItemViewModel = discoveryBaseViewModel as ClaimCouponItemViewModel

        claimCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer {
            ImageHandler.LoadImage(claimCouponImage, it.imageUrlMobile)
            claimCouponLabel.setTextAndCheckShow(it.minUsageLabel)
            claimCouponLabelSubTitle.setTextAndCheckShow(it.minUsage)
            claimCouponTitle.setTextAndCheckShow(it.title)
            claimCouponSubTitle.setTextAndCheckShow(it.subtitle)
        })

        claimCouponItemViewModel.getClaimStatus().observe(fragment.viewLifecycleOwner, Observer { status ->
            setBtn(status)
            itemView.setOnClickListener {
                claimCouponItemViewModel.setClick(itemView.context, status)

            }
        })

        claimBtn.setOnClickListener {
            claimCouponItemViewModel.redeemCoupon()
            claimCouponItemViewModel.getRedeemCouponCode().observe(fragment.viewLifecycleOwner, Observer { item ->
                if (!item.isNullOrEmpty() && item != ClaimCouponConstant.NOT_LOGEDIN) {
                    setBtn(ClaimCouponConstant.UNCLAIMED)
                    Toaster.make(itemView.rootView, ClaimCouponConstant.REDEEM_COUPON_MSG, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ClaimCouponConstant.LIHAT_TEXT, View.OnClickListener {
                        val applink = GenerateUrl.getClaimCoupon(item)
                        claimCouponItemViewModel.navigate(itemView.context, applink)
                    })
                } else {
                    Toaster.make(itemView.rootView, "please log in", Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, "", View.OnClickListener {})
                }
            })

        }

    }

    private fun setBtn(status: String) {
        claimBtn.text = status
        claimBtn.isEnabled = status == ClaimCouponConstant.CLAIMED
    }

}