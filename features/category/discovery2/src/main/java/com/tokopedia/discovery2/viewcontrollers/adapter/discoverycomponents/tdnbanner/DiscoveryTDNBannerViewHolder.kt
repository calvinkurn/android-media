package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.DiscoveryTdnBannerViewBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.binding.viewBinding

class DiscoveryTDNBannerViewHolder(
    itemView: View,
    private val fragment: Fragment
) : AbstractViewHolder(
    itemView = itemView,
    lifecycleOwner = fragment.viewLifecycleOwner
), TdnBannerResponseListener {
    companion object {
        private const val BANNER_CORNER_RADIUS_DP = 8
    }

    private val binding: DiscoveryTdnBannerViewBinding?
        by viewBinding()

    private val userSession: UserSession
        by lazy { UserSession(itemView.context) }

    private val analytics: BaseDiscoveryAnalytics?
        by lazy { (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics() }

    private var viewModel: DiscoveryTDNBannerViewModel? = null
    private var shouldHitService: Boolean = true

    init {
        binding?.apply {
            showWidget()
            tdnBannerView.setTdnResponseListener(this@DiscoveryTDNBannerViewHolder)
        }
    }

    override fun bindView(
        discoveryBaseViewModel: DiscoveryBaseViewModel
    ) {
        viewModel = discoveryBaseViewModel as? DiscoveryTDNBannerViewModel
    }

    override fun setUpObservers(
        lifecycleOwner: LifecycleOwner?
    ) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            binding?.setupLayout(owner)
        }
    }

    override fun removeObservers(
        lifecycleOwner: LifecycleOwner?
    ) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            viewModel?.componentLiveData?.removeObservers(owner)
        }
    }

    override fun onTdnBannerResponse(
        categoriesList: MutableList<List<TopAdsImageViewModel>>
    ) {
        binding?.apply {
            val tdnBanners = categoriesList.firstOrNull()
            if (!tdnBanners.isNullOrEmpty()) {
                shimmerView.hide()
                tdnBannerView.show()
                tdnBannerView.renderTdnBanner(
                    tdnBanners = tdnBanners,
                    cornerRadius = BANNER_CORNER_RADIUS_DP.toPx(),
                    onLoadFailed = ::onTdnBannerFailedLoaded,
                    onTdnBannerClicked = ::onTdnBannerClicked,
                    onTdnBannerImpressed = ::onTdnBannerImpressed
                )
            } else {
                hideWidget()
            }
        }
    }

    override fun onError(
        t: Throwable
    ) {
        binding?.hideWidget()
    }

    private fun DiscoveryTdnBannerViewBinding.setupLayout(
        owner: LifecycleOwner
    ) {
        if (userSession.isLoggedIn) {
            if (!shouldHitService) return

            viewModel?.componentLiveData?.observe(owner) { component ->
                shouldHitService = false
                addHeader(component)
                getBannerData(component)
            }
        } else {
            hideWidget()
        }
    }

    private fun DiscoveryTdnBannerViewBinding.addHeader(
        componentsItem: ComponentsItem
    ) {
        val data = componentsItem.data?.firstOrNull()
        headerView.showIfWithBlock(!data?.title.isNullOrEmpty() || !data?.subtitle.isNullOrEmpty()) {
            headerView.removeAllViews()
            headerView.addView(
                CustomViewCreator.getCustomViewObject(
                    context = itemView.context,
                    component = ComponentsList.LihatSemua,
                    componentItem = componentsItem,
                    fragment = fragment
                )
            )
        }
    }

    private fun DiscoveryTdnBannerViewBinding.getBannerData(
        component: ComponentsItem
    ) {
        component.data?.firstOrNull()?.run {
            tdnBannerView.getTdnData(
                source = inventoryId.orEmpty(),
                adsCount = adsCount.orZero(),
                dimenId = dimensionId.toIntSafely(),
                depId = depID.orEmpty(),
                productID = productId.orEmpty()
            )
        }
    }

    private fun DiscoveryTdnBannerViewBinding.showWidget() {
        headerView.removeAllViews()
        tdnBannerView.hide()
        shimmerView.show()
    }

    private fun DiscoveryTdnBannerViewBinding.hideWidget() {
        headerView.removeAllViews()
        tdnBannerView.hide()
        shimmerView.hide()
    }

    private fun onTdnBannerFailedLoaded() {
        binding?.hideWidget()
    }

    private fun onTdnBannerClicked(
        bannerData: TopAdsImageViewModel
    ) {
        viewModel?.apply {
            analytics?.trackTDNBannerClick(
                componentsItem = components,
                userID = userSession.userId,
                positionInPage = position,
                adID = bannerData.bannerId.orEmpty(),
                shopId = bannerData.bannerName,
                itemPosition = bannerData.position
            )
        }
        if (!bannerData.applink.isNullOrBlank()) {
            RouteManager.route(itemView.context, bannerData.applink)
        }
    }

    private fun onTdnBannerImpressed(
        bannerData: TopAdsImageViewModel
    ) {
        viewModel?.apply {
            analytics?.trackTDNBannerImpression(
                componentsItem = components,
                userID = userSession.userId,
                positionInPage = position,
                adID = bannerData.bannerId.orEmpty(),
                shopId = bannerData.bannerName,
                itemPosition = bannerData.position
            )
        }
    }
}
