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
import com.tokopedia.kotlin.extensions.view.hide
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

        if (claimCouponItemViewModel.getIsDouble()) {
            claimCouponImageDouble.show()
            claimCouponImage.hide()
            claimCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer {
                ImageHandler.LoadImage(claimCouponImageDouble, it.imageUrlMobile)
            })
        } else {
            claimCouponImageDouble.hide()
            claimCouponImage.show()
            claimCouponItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer {
                ImageHandler.LoadImage(claimCouponImage, it.imageUrlMobile)
            })
        }

        claimCouponItemViewModel.getClaimStatus().observe(fragment.viewLifecycleOwner, Observer { status ->
            setBtn(status)
            itemView.setOnClickListener {
                claimCouponItemViewModel.setClick(itemView.context, status)

            }
        })

        claimBtn.setOnClickListener {
            claimCouponItemViewModel.redeemCoupon()
            claimCouponItemViewModel.getRedeemCouponCode().observe(fragment.viewLifecycleOwner, Observer { item ->
                if (!item.isNullOrEmpty() && item != ClaimCouponConstant.NOT_LOGGEDIN) {
                    setBtn(ClaimCouponConstant.UNCLAIMED)
                    Toaster.make(itemView.rootView, ClaimCouponConstant.REDEEM_COUPON_MSG, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ClaimCouponConstant.LIHAT_TEXT, View.OnClickListener {
                        val applink = GenerateUrl.getClaimCoupon(item)
                        claimCouponItemViewModel.navigate(itemView.context, applink)
                    })
                } else {
                    Toaster.make(itemView.rootView, itemView.context.getString(R.string.discovery_please_log_in), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, "", View.OnClickListener {})
                }
            })

        }

    }

    private fun setBtn(status: String) {
        claimBtn.text = if (status == ClaimCouponConstant.OUT_OF_STOCK)
            ClaimCouponConstant.UNCLAIMED
        else
            status
        claimBtn.isEnabled = status == ClaimCouponConstant.CLAIMED
    }

}