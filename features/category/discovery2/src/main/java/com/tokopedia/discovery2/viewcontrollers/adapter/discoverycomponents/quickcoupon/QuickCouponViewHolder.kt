package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster

class QuickCouponViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), View.OnClickListener {
    private var applyButton: Button = itemView.findViewById(R.id.apply_btn)
    private var titleTextView: TextView = itemView.findViewById(R.id.title_tv)
    private var cardLayout: ConstraintLayout = itemView.findViewById(R.id.cardLayout)
    private var couponShimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private var couponAddedImage: ImageView = itemView.findViewById(R.id.applied_img)
    private var componentPosition: Int? = null

    private lateinit var quickCouponViewModel: QuickCouponViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        quickCouponViewModel = discoveryBaseViewModel as QuickCouponViewModel
        initView()
    }

    private fun initView() {
        applyButton.setOnClickListener(this)
        cardLayout.setOnClickListener(this)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { viewLifecycleOwner ->
            quickCouponViewModel.getCouponStatus().observe(viewLifecycleOwner, Observer {
                updateCouponStatusUI(it)
            })
            quickCouponViewModel.isUserLoggedIn().observe(viewLifecycleOwner, Observer {
                userLoggedInStatus(it)
            })
            quickCouponViewModel.getPhoneVerificationStatus().observe(viewLifecycleOwner, Observer {
                handlePhoneVerification(it)
            })
            quickCouponViewModel.getCouponVisibilityStatus().observe(viewLifecycleOwner, Observer {
                if (it) {
                    (fragment as? DiscoveryFragment)?.reSync()
                    quickCouponViewModel.getCouponDetail()?.let { clickCouponData ->
                        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponImpression(clickCouponData)
                    }
                }
            })
            quickCouponViewModel.getCouponAddedStatus().observe(viewLifecycleOwner, Observer {
                handleCouponAdded(it)
            })
            quickCouponViewModel.getComponentPosition().observe(viewLifecycleOwner, Observer {
                componentPosition = it
            })
        }
    }

    private fun userLoggedInStatus(it: Boolean?) {
        if (it == true) {
            quickCouponViewModel.checkMobileVerificationStatus()
        } else {
            componentPosition?.let { position -> (fragment as DiscoveryFragment).quickCouponLoginScreen(position) }
        }
    }

    private fun updateCouponStatusUI(it: Boolean) {
        couponShimmerView.hide()
        cardLayout.visible()
        if (it) {
            couponAddedImage.visible()
            applyButton.hide()
        } else {
            couponAddedImage.hide()
            applyButton.show()
        }
        titleTextView.text = quickCouponViewModel.getCouponTitle()
    }

    override fun onClick(view: View?) {
        when (view) {
            applyButton -> {
                quickCouponViewModel.onClaimCouponClick()
                quickCouponViewModel.getCouponDetail()?.let { clickCouponData ->
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponApply(clickCouponData)
                }
            }
            cardLayout -> {
                RouteManager.route(itemView.context, quickCouponViewModel.getCouponApplink())
                quickCouponViewModel.getCouponDetail()?.let { clickCouponData ->
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponClick(clickCouponData)
                }
            }
        }
    }

    private fun handlePhoneVerification(phoneStatus: Boolean?) {
        phoneStatus?.let {
            (fragment as DiscoveryFragment).phoneVerificationResponseCallBack(it)
        }
    }

    private fun handleCouponAdded(it: Boolean) {
        if (it) {
            val message = quickCouponViewModel.getCouponAddedFailMessage()
            if (message.isNotEmpty()) {
                Toaster.make(itemView.rootView, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            }
        }
    }
}