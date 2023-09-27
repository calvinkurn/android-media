package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DIKLAIM
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.HABIS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.KLAIM
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ClaimCouponItemViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView) {

    private var componentItem: ComponentsItem? = null
    private var claimCouponItemViewModel: ClaimCouponItemViewModel? = null
    private val claimCouponImage: ImageView = itemView.findViewById(R.id.appCompatImageView)
    private val claimCouponImageDouble: ImageView =
        itemView.findViewById(R.id.appCompatImageViewDouble)
    private val claimBtn: UnifyButton = itemView.findViewById(R.id.claim_btn)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        claimCouponItemViewModel = discoveryBaseViewModel as ClaimCouponItemViewModel
        claimCouponItemViewModel?.let {
            getSubComponent().inject(it)
        }
        claimCouponItemViewModel?.getComponentData()
            ?.observe(fragment.viewLifecycleOwner) {
                this.componentItem = it
                claimCouponItemViewModel?.let { it1 ->
                    setData(it1.getClaimCouponData(), it1.getIsDouble())
                }
            }
    }

    private fun setData(claimCouponItem: CatalogWithCouponList?, isDouble: Boolean) {
        if (isDouble) {
            claimCouponImageDouble.show()
            claimCouponImage.hide()
            claimCouponImageDouble.loadImage(claimCouponItem?.smallImageURLMobile.orEmpty())
        } else {
            claimCouponImageDouble.hide()
            claimCouponImage.show()
            claimCouponImage.loadImage(claimCouponItem?.imageURLMobile.orEmpty())
        }

        setBtn(claimCouponItem?.status, isDouble)
        itemView.setOnClickListener {
            claimCouponItemViewModel?.setClick(itemView.context, claimCouponItem?.status)
            componentItem?.let {
                (fragment as DiscoveryFragment)
                    .getDiscoveryAnalytics()
                    .trackEventClickCoupon(it, adapterPosition, isDouble)
            }
        }

        claimBtn.setOnClickListener {
            claimCouponItemViewModel?.redeemCoupon { message ->
                Toaster
                    .build(
                        view = itemView,
                        text = message,
                        duration = Snackbar.LENGTH_SHORT,
                        type = Toaster.TYPE_ERROR
                    )
                    .show()
            }
            claimCouponItemViewModel?.getRedeemCouponCode()
                ?.observe(fragment.viewLifecycleOwner) { item ->
                    try {
                        if (item == NOT_LOGGEDIN) {
                            Toaster.build(
                                view = itemView.rootView,
                                text = itemView.context.getString(R.string.discovery_please_log_in),
                                duration = Snackbar.LENGTH_LONG,
                                type = Toaster.TYPE_NORMAL,
                                actionText = itemView.context.getString(R.string.discovery_login)
                            ) {
                                (fragment as DiscoveryFragment).openLoginScreen()
                            }
                                .show()
                        } else {
                            setBtn(DIKLAIM)
                            Toaster.build(
                                view = itemView.rootView,
                                text = itemView.context.getString(R.string.claim_coupon_redeem_coupon_msg),
                                duration = Snackbar.LENGTH_LONG,
                                type = Toaster.TYPE_NORMAL,
                                actionText = itemView.context.getString(R.string.claim_coupon_lihat_text)
                            ) {
                                claimCouponItemViewModel?.getCouponAppLink()?.let { appLink ->
                                    claimCouponItemViewModel?.navigate(
                                        itemView.context,
                                        appLink
                                    )
                                }
                            }
                                .show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackClickClaimCoupon(claimCouponItem?.title, claimCouponItem?.baseCode)
        }
    }

    private fun setBtn(status: String?, isDouble: Boolean? = null) {
        isDouble?.let {
            if (isDouble) {
                claimBtn.buttonSize = UnifyButton.Size.MICRO
            } else {
                claimBtn.buttonSize = UnifyButton.Size.SMALL
            }
        }
        claimBtn.text = if (status == HABIS || status == null) {
            HABIS
        } else {
            status
        }
        claimBtn.isEnabled = status == KLAIM
        if (claimBtn.isEnabled) {
            claimBtn.isInverse = true
            claimBtn.setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN0
                )
            )
        } else {
            claimBtn.isInverse = false
        }
        if (claimBtn.text.isNullOrEmpty()) {
            claimBtn.visibility = View.GONE
        }
    }
}
