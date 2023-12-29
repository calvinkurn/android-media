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
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
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
    private var componentData: ComponentsItem? = null

    private var quickCouponViewModel: QuickCouponViewModel? = null

    init {
        initView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        quickCouponViewModel = discoveryBaseViewModel as QuickCouponViewModel
        quickCouponViewModel?.let {
            getSubComponent().inject(it)
        }
    }

    private fun initView() {
        applyButton.setOnClickListener(this)
        cardLayout.setOnClickListener(this)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { viewLifecycleOwner ->
            quickCouponViewModel?.getCouponStatus()?.observe(
                viewLifecycleOwner,
                Observer {
                    updateCouponStatusUI(it)
                }
            )
            quickCouponViewModel?.getLoggedInStatusLiveData()?.observe(
                viewLifecycleOwner,
                Observer {
                    handleLoggedInStatus(it)
                }
            )
            quickCouponViewModel?.getCouponVisibilityStatus()?.observe(
                viewLifecycleOwner,
                Observer {
                    handleCouponVisibility(it)
                }
            )

            quickCouponViewModel?.getPhoneVerificationStatus()?.observe(
                viewLifecycleOwner,
                Observer {
                    handlePhoneVerification(it)
                }
            )
            quickCouponViewModel?.getComponentPosition()?.observe(
                viewLifecycleOwner,
                Observer {
                    componentPosition = it
                }
            )
            quickCouponViewModel?.getCouponAddedStatus()?.observe(
                viewLifecycleOwner,
                Observer {
                    handleCouponAdded(it)
                }
            )
            quickCouponViewModel?.getSyncPageLiveData()?.observe(fragment.viewLifecycleOwner) {
                if (it) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
            quickCouponViewModel?.hideSectionLD?.observe(fragment.viewLifecycleOwner) { sectionId ->
                (fragment as DiscoveryFragment).handleHideSection(sectionId)
            }
        }
        componentData = quickCouponViewModel?.getComponentData()
    }

    private fun handleCouponVisibility(couponVisibleStatus: Boolean) {
        if (couponVisibleStatus) {
            quickCouponViewModel?.getCouponDetail()?.let { clickCouponData ->
                if (componentData?.couponViewImpression == false) {
                    componentData?.couponViewImpression = true
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponImpression(clickCouponData)
                }
            }
        } else {
            if (componentData?.couponDetailClicked == true || componentData?.couponAppliedClicked == true) {
                componentData?.couponAppliedClicked = false
                componentData?.couponDetailClicked = false
                handleCouponAdded()
            }
            (fragment as? DiscoveryFragment)?.reSync()
        }
    }

    private fun handleLoggedInStatus(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            if (componentData?.couponAppliedClicked == true) {
                componentData?.couponAppliedClicked = false
                if (quickCouponViewModel?.getCouponAppliedStatus() == false && quickCouponViewModel?.getCouponApplicableStatus() == true) {
                    quickCouponViewModel?.checkMobileVerificationStatus()
                }
            } else if (componentData?.couponDetailClicked == true) {
                redirectCouponPage()
            }
        } else {
            if (componentData?.couponDetailClicked == true || componentData?.couponAppliedClicked == true) {
                componentPosition?.let { position -> (fragment as DiscoveryFragment).openLoginScreen(position) }
            }
        }
    }

    private fun handlePhoneVerification(phoneStatus: Boolean) {
        if (phoneStatus) {
            quickCouponViewModel?.applyQuickCoupon()
        } else {
            componentPosition?.let { position -> (fragment as DiscoveryFragment).openMobileVerificationWithBottomSheet(position) }
        }
    }

    private fun handleCouponAdded(it: Boolean = false) {
        if (!it) {
            val message = quickCouponViewModel?.getCouponAddedFailMessage()
            if (message?.isNotEmpty() == true) {
                try {
                    Toaster.make(itemView.rootView, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateCouponStatusUI(it: Boolean) {
        couponShimmerView.hide()
        cardLayout.visible()
        if (it) {
            couponAddedImage.visible()
            applyButton.hide()
            quickCouponViewModel?.getCouponDetail()?.let { clickCouponData ->
                clickCouponData.couponApplied = true
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponImpression(clickCouponData)
            }
        } else {
            couponAddedImage.hide()
            applyButton.show()
        }
        titleTextView.text = quickCouponViewModel?.getCouponTitle()
    }

    override fun onClick(view: View?) {
        when (view) {
            applyButton -> {
                enableCouponApplyClick()
            }
            cardLayout -> {
                enableCouponDetailClick()
            }
        }
    }

    private fun enableCouponDetailClick() {
        componentData?.couponDetailClicked = true
        componentData?.couponAppliedClicked = false
        quickCouponViewModel?.loggedInStatus()
        quickCouponViewModel?.getCouponDetail()?.let { clickCouponData ->
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponClick(clickCouponData)
        }
    }

    private fun redirectCouponPage() {
        componentData?.couponDetailClicked = false
        RouteManager.route(itemView.context, quickCouponViewModel?.getCouponApplink())
    }

    private fun enableCouponApplyClick() {
        componentData?.couponAppliedClicked = true
        componentData?.couponDetailClicked = false
        quickCouponViewModel?.loggedInStatus()
        quickCouponViewModel?.getCouponDetail()?.let { clickCouponData ->
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackQuickCouponApply(clickCouponData)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            quickCouponViewModel?.getCouponStatus()?.removeObservers(it)
            quickCouponViewModel?.getLoggedInStatusLiveData()?.removeObservers(it)
            quickCouponViewModel?.getCouponVisibilityStatus()?.removeObservers(it)
            quickCouponViewModel?.getPhoneVerificationStatus()?.removeObservers(it)
            quickCouponViewModel?.getComponentPosition()?.removeObservers(it)
            quickCouponViewModel?.getCouponAddedStatus()?.removeObservers(it)
            quickCouponViewModel?.getSyncPageLiveData()?.removeObservers(it)
            quickCouponViewModel?.hideSectionLD?.removeObservers(it)
        }
    }
}
