package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewcontrollers.fragment.PAGE_REFRESH_LOGIN
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.item_empty_error_state.view.*

class MultiBannerViewHolder(private val customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView) {
    private var constraintLayout: ConstraintLayout = customItemView.findViewById(R.id.banner_container_layout)
    private var context: Context = constraintLayout.context
    private var bannerName: String? = null
    private var multiBannerViewModel: MultiBannerViewModel? = null
    private var bannersItemList: ArrayList<BannerItem> = arrayListOf()

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        multiBannerViewModel = discoveryBaseViewModel as MultiBannerViewModel
        multiBannerViewModel?.let { multiBannerViewModel ->
            getSubComponent().inject(multiBannerViewModel)
            if (multiBannerViewModel.shouldShowShimmer()) {
                constraintLayout.removeAllViews()
                val shimmerView = constraintLayout.inflateLayout(multiBannerViewModel.layoutSelector(), false)
                constraintLayout.addView(shimmerView)
            }
            multiBannerViewModel.checkForDarkMode(itemView.context)
            multiBannerViewModel.getComponentData().observe(
                fragment.viewLifecycleOwner,
                Observer { item ->

                    if (!item.data.isNullOrEmpty()) {
                        constraintLayout.removeAllViews()
                        bannersItemList = ArrayList()
                        bannerName = item?.name ?: ""
                        addBanners(item.data!!, item.properties?.compType)
                    }
                }
            )

            multiBannerViewModel.getPushNotificationBannerSubscription().observe(
                fragment.viewLifecycleOwner
            ) { model ->
                if (model.errorMessage.isNotEmpty()) {
                    Toaster.build(
                        customItemView,
                        model.errorMessage,
                        Toast.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                } else {
                    updateImage(
                        position = model.position,
                        isSubscribed = model.isSubscribed
                    )
                }
            }

            multiBannerViewModel.getPushNotificationBannerSubscriptionInit().observe(
                fragment.viewLifecycleOwner
            ) { model ->
                updateImage(
                    position = model.position,
                    isSubscribed =  model.isSubscribed
                )
            }

            multiBannerViewModel.getShowLoginData().observe(
                fragment.viewLifecycleOwner,
                Observer {
                    if (it) context.startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
                }
            )

            multiBannerViewModel.checkApplink().observe(
                fragment.viewLifecycleOwner,
                Observer { applink ->
                    try {
                        if (applink.isNotEmpty()) {
                            Toaster.make(
                                customItemView,
                                fragment.getString(R.string.coupon_code_successfully_copied),
                                Toast.LENGTH_SHORT,
                                Toaster.TYPE_NORMAL,
                                customItemView.context.getString(R.string.coupon_code_btn_text),
                                View.OnClickListener {
                                    multiBannerViewModel.navigate(customItemView.context, applink)
                                }
                            )
                        } else {
                            Toaster.make(customItemView, fragment.getString(R.string.coupon_code_successfully_copied), Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )

            multiBannerViewModel.isPageRefresh().observe(
                fragment.viewLifecycleOwner,
                Observer {
                    if (it) fragment.startActivityForResult(RouteManager.getIntent(fragment.context, ApplinkConst.LOGIN), PAGE_REFRESH_LOGIN)
                }
            )

            multiBannerViewModel.hideShimmer.observe(fragment.viewLifecycleOwner) { shouldHideShimmer ->
                if (shouldHideShimmer) {
                    constraintLayout.removeAllViews()
                }
            }

            multiBannerViewModel.showErrorState.observe(fragment.viewLifecycleOwner) { shouldShowError ->
                if (shouldShowError) {
                    handleError()
                }
            }
        }
    }

    private fun handleError() {
        constraintLayout.removeAllViews()
        val emptyStateParentView = constraintLayout.inflateLayout(R.layout.item_empty_error_state, false)
        val emptyStateView: LocalLoad = emptyStateParentView.findViewById(R.id.viewEmptyState)
        emptyStateView.apply {
            val errorLoadUnifyView = emptyStateView.viewEmptyState
            errorLoadUnifyView.title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            errorLoadUnifyView.description?.text =
                context?.getString(R.string.discovery_product_empty_state_description).orEmpty()
            errorLoadUnifyView.refreshBtn?.setOnClickListener {
                hide()
                constraintLayout.removeAllViews()
                val shimmerView = multiBannerViewModel?.layoutSelector()?.let { it1 -> constraintLayout.inflateLayout(it1, false) }
                constraintLayout.addView(shimmerView)
                multiBannerViewModel?.reload()
            }
        }
        emptyStateView.isVisible = true
        constraintLayout.addView(emptyStateParentView)
    }

    private fun updateImage(
        position: Int,
        isSubscribed: Boolean
    ) {
        if (bannersItemList.isNotEmpty() && position != Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS &&
            !bannersItemList[position].bannerItemData.registeredImageApp.isNullOrEmpty()
        ) {
            (bannersItemList[position].bannerImageView as ImageUnify).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                setImageUrl(if (isSubscribed) bannersItemList[position].bannerItemData.registeredImageApp.orEmpty() else bannersItemList[position].bannerItemData.imageUrlDynamicMobile.orEmpty())
            }
        }
    }

    private fun addBanners(data: List<DataItem>, compType: String?) {
        val constraintSet = ConstraintSet()
        val height = multiBannerViewModel?.getBannerUrlHeight()
        val width = multiBannerViewModel?.getBannerUrlWidth()

        /*coupons can be there in form of banners so we need to set hardcoded values for component_name
         (i have added componentPromoName key in dataItem for it)*/
        // purpose - we need to send different GTM in case of coupons
        bannerName?.let { multiBannerViewModel?.setComponentPromoNameForCoupons(it, data) }

        for ((index, bannerItem) in data.withIndex()) {
            var bannerView: BannerItem
            val isLastItem = index == data.size - 1
            if (bannerItem.parentComponentName.isNullOrEmpty()) {
                bannerItem.parentComponentName = bannerName
            }
            multiBannerViewModel?.let {
                bannerItem.positionForParentItem = it.position
            }
            bannerView = if (index == 0) {
                BannerItem(
                    bannerItem, constraintLayout, constraintSet, width, height, bannerItem.itemWeight, index,
                    null, context, isLastItem, compType
                )
            } else {
                BannerItem(
                    bannerItem, constraintLayout, constraintSet, width, height, bannerItem.itemWeight, index,
                    bannersItemList.get(index - 1), context, isLastItem, compType
                )
            }
            bannersItemList.add(bannerView)

            checkSubscriptionStatus(index)
            setClickOnBanners(bannerItem, index)
        }
        sendImpressionEventForBanners(data)
    }

    private fun sendImpressionEventForBanners(data: List<DataItem>) {
        if (data.firstOrNull()?.action == BANNER_ACTION_CODE) {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackPromoBannerImpression(
                data
            )
        } else {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerImpression(
                data,
                null,
                Utils.getUserId(fragment.context)
            )
        }
    }

    private fun checkSubscriptionStatus(position: Int) {
        multiBannerViewModel?.campaignSubscribedStatus(position)
    }

    private fun setClickOnBanners(itemData: DataItem, index: Int) {
        bannersItemList[index].bannerImageView.setOnClickListener {
            multiBannerViewModel?.onBannerClicked(
                position = index,
                context = context,
                defaultErrorMessage = context.getString(R.string.discovery_push_notification_banner_subscription_error_toaster_message)
            )
            if (itemData.action == BANNER_ACTION_CODE) {
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackPromoBannerClick(itemData, index)
            } else {
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackBannerClick(itemData, index, Utils.getUserId(fragment.context))
            }
        }
    }
}
