package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.content.Context
import android.view.View
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
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster

class MultiBannerViewHolder(private val customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView) {
    private var constraintLayout: ConstraintLayout = customItemView.findViewById(R.id.banner_container_layout)
    private var context: Context
    private lateinit var bannerName: String
    private lateinit var multiBannerViewModel: MultiBannerViewModel
    private lateinit var bannersItemList: ArrayList<BannerItem>

    init {
        context = constraintLayout.context
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        multiBannerViewModel = discoveryBaseViewModel as MultiBannerViewModel
        getSubComponent().inject(multiBannerViewModel)
        multiBannerViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { item ->

            if (!item.data.isNullOrEmpty()) {
                constraintLayout.removeAllViews()
                bannersItemList = ArrayList()
                bannerName = item?.name ?: ""
                addBanners(item.data!!)
            }
        })

        multiBannerViewModel.getPushBannerStatusData().observe(fragment.viewLifecycleOwner, Observer {
            if (bannersItemList.isNotEmpty() && it != Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS) {
                (bannersItemList[it].bannerImageView as ImageUnify).loadImageFitCenter(bannersItemList[it].bannerItemData.registeredImageApp)
            }
        })

        multiBannerViewModel.getPushBannerSubscriptionData().observe(fragment.viewLifecycleOwner, Observer {
            if (bannersItemList.isNotEmpty() && it != Utils.BANNER_SUBSCRIPTION_DEFAULT_STATUS) {
                (bannersItemList[it].bannerImageView as ImageUnify).loadImageFitCenter(bannersItemList[it].bannerItemData.registeredImageApp)
            }
        })
        multiBannerViewModel.getShowLoginData().observe(fragment.viewLifecycleOwner, Observer {
            if (it) context.startActivity(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        })

        multiBannerViewModel.checkApplink().observe(fragment.viewLifecycleOwner, Observer { applink ->
            try {
                if (applink.isNotEmpty()) {
                    Toaster.make(customItemView, fragment.getString(R.string.coupon_code_successfully_copied), Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL,
                            customItemView.context.getString(R.string.coupon_code_btn_text), View.OnClickListener {
                        multiBannerViewModel.navigate(customItemView.context, applink)
                    })
                } else {
                    Toaster.make(customItemView, fragment.getString(R.string.coupon_code_successfully_copied), Toast.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        multiBannerViewModel.isPageRefresh().observe(fragment.viewLifecycleOwner, Observer {
            if (it) fragment.startActivityForResult(RouteManager.getIntent(fragment.context, ApplinkConst.LOGIN), PAGE_REFRESH_LOGIN)
        })
    }

    private fun addBanners(data: List<DataItem>) {
        val constraintSet = ConstraintSet()
        val height = multiBannerViewModel.getBannerUrlHeight()
        val width = multiBannerViewModel.getBannerUrlWidth()

        for ((index, bannerItem) in data.withIndex()) {
            var bannerView: BannerItem
            val isLastItem = index == data.size - 1
            if (bannerItem.parentComponentName.isNullOrEmpty()) {
                bannerItem.parentComponentName = bannerName
            }
            bannerItem.positionForParentItem = multiBannerViewModel.position
            bannerView = if (index == 0) {
                BannerItem(bannerItem, constraintLayout, constraintSet, width, height, index,
                        null, context, isLastItem)
            } else {
                BannerItem(bannerItem, constraintLayout, constraintSet, width, height, index,
                        bannersItemList.get(index - 1), context, isLastItem)
            }
            bannersItemList.add(bannerView)

            checkSubscriptionStatus(index)
            setClickOnBanners(bannerItem, index)
        }
        sendImpressionEventForBanners(data)
    }

    private fun sendImpressionEventForBanners(data: List<DataItem>) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerImpression(data, multiBannerViewModel.getComponentPosition())
    }

    private fun checkSubscriptionStatus(position: Int) {
        multiBannerViewModel.campaignSubscribedStatus(position)
    }

    private fun setClickOnBanners(itemData: DataItem, index: Int) {
        bannersItemList[index].bannerImageView.setOnClickListener {
            multiBannerViewModel.onBannerClicked(index, context)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(itemData, index)
        }
    }
}