package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

class ClaimCouponItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var componentItem: ComponentsItem? = null
    private var claimCouponItemViewModel: ClaimCouponItemViewModel? = null
    private val claimCouponImage: ImageView = itemView.findViewById(R.id.appCompatImageView)
    private val claimCouponImageDouble: ImageView = itemView.findViewById(R.id.appCompatImageViewDouble)
    private val claimBtn: UnifyButton = itemView.findViewById(R.id.claim_btn)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        claimCouponItemViewModel = discoveryBaseViewModel as ClaimCouponItemViewModel
        claimCouponItemViewModel?.let {
            getSubComponent().inject(it)
        }
        claimCouponItemViewModel?.getComponentData()?.observe(
            fragment.viewLifecycleOwner,
            Observer {
                this.componentItem = it
                claimCouponItemViewModel?.let { it1 ->
                    setData(it1.getClaimCouponData(), it1.getIsDouble())
                }
            }
        )
    }

    private fun setData(claimCouponItem: CatalogWithCouponList?, isDouble: Boolean) {
        if (isDouble) {
            claimCouponImageDouble.show()
            claimCouponImage.hide()
            claimCouponImageDouble.loadImage(claimCouponItem?.smallImageURLMobile ?: "")
        } else {
            claimCouponImageDouble.hide()
            claimCouponImage.show()
            claimCouponImage.loadImage(claimCouponItem?.imageURLMobile ?: "")
        }

        setBtn(claimCouponItem?.status, isDouble, claimCouponItem?.isDisabled)
        itemView.setOnClickListener {
            claimCouponItemViewModel?.setClick(itemView.context, claimCouponItem?.status)
            componentItem?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventClickCoupon(it, adapterPosition, isDouble)
            }
        }

        claimBtn.setOnClickListener {
            claimCouponItemViewModel?.redeemCoupon { message ->
                Toaster.make(itemView, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
            claimCouponItemViewModel?.getRedeemCouponCode()?.observe(
                fragment.viewLifecycleOwner,
                Observer { item ->
                    try {
                        if (item == NOT_LOGGEDIN) {
                            Toaster.make(
                                itemView.rootView,
                                itemView.context.getString(R.string.discovery_please_log_in),
                                Snackbar.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                itemView.context.getString(R.string.discovery_login),
                                View.OnClickListener {
                                    (fragment as DiscoveryFragment).openLoginScreen()
                                }
                            )
                        } else {
                            setBtn(DIKLAIM, isDisabled = false)
                            Toaster.make(
                                itemView.rootView,
                                itemView.context.getString(R.string.claim_coupon_redeem_coupon_msg),
                                Snackbar.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                itemView.context.getString(R.string.claim_coupon_lihat_text),
                                View.OnClickListener {
                                    claimCouponItemViewModel?.getCouponAppLink()?.let { appLink ->
                                        claimCouponItemViewModel?.navigate(itemView.context, appLink)
                                    }
                                }
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickClaimCoupon(claimCouponItem?.title, claimCouponItem?.baseCode)
        }
    }

    private fun setBtn(status: String?, isDouble: Boolean? = null, isDisabled: Boolean?) {
        if (isDisabled == true) {
            claimBtn.hide()
        } else {
            claimBtn.show()
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
                claimBtn.buttonVariant = UnifyButton.Variant.GHOST
                claimBtn.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            } else {
                claimBtn.isInverse = false
                claimBtn.buttonVariant = UnifyButton.Variant.FILLED
            }
        }
    }
}
