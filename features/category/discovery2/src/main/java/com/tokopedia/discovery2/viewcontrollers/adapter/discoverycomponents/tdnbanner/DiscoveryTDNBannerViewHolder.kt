package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSession

class DiscoveryTDNBannerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), TopAdsImageVieWApiResponseListener, TopAdsImageViewClickListener, TopAdsImageViewImpressionListener {
    private val tdnImageView: TopAdsImageView? = itemView.findViewById(R.id.tdn_view)
    private val mHeaderView: FrameLayout? = itemView.findViewById(R.id.header_view)
    private val shimmerView: LoaderUnify? = itemView.findViewById(R.id.shimmer_view)
    private lateinit var viewModel: DiscoveryTDNBannerViewModel

    private val inventoryID = "13"
    private val adCount = 1
    private val dimenID = 3
    private var shouldHitService = true
    private lateinit var topAdsModel: TopAdsImageViewModel

    init {
        tdnImageView?.setApiResponseListener(this)
        tdnImageView?.setTopAdsImageViewClick(this)
        tdnImageView?.setTopAdsImageViewImpression(this)
    }


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as DiscoveryTDNBannerViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner != null) {
            itemView.context?.let { context ->
                if (UserSession(context).isLoggedIn) {
                    viewModel.componentLiveData.observe(lifecycleOwner, {
                        if (shouldHitService) {
                            addCardHeader(it)
                            tdnImageView?.getImageData(inventoryID, adCount, dimenID, depId = it.data?.firstOrNull()?.depID
                                    ?: "")
                        }
                    })
                } else {
                    handleError()
                }
            }
        }

    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel.componentLiveData.removeObservers(it)
        }
    }

    private fun addCardHeader(componentsItem: ComponentsItem?) {
        mHeaderView?.removeAllViews()
        componentsItem?.data?.firstOrNull()?.let {
            if (!it.title.isNullOrEmpty() || !it.subtitle.isNullOrEmpty()) {
                mHeaderView?.addView(CustomViewCreator.getCustomViewObject(itemView.context,
                        ComponentsList.LihatSemua, componentsItem, fragment))
            }
        }
    }

    private fun handleError() {
        mHeaderView?.run {
            if (childCount > 0)
                removeAllViews()
        }
        shimmerView?.hide()
        tdnImageView?.hide()
    }


    override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
        shouldHitService = false
        if (imageDataList.isNotEmpty()) {
            shimmerView?.hide()
            tdnImageView?.show()
            topAdsModel = imageDataList[0]
            tdnImageView?.loadImage(topAdsModel)
        } else {
            shouldHitService = false
            handleError()
        }
    }

    override fun onError(t: Throwable) {
        shouldHitService = false
        handleError()
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTDNBannerClick(viewModel.components, UserSession(fragment.context).userId,
                viewModel.position, topAdsModel.bannerId ?: "", topAdsModel.shopId)
        if (!applink.isNullOrEmpty() && fragment.context != null)
            RouteManager.route(fragment.context, applink)

    }

    override fun onTopAdsImageViewImpression(viewUrl: String) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTDNBannerImpression(viewModel.components, UserSession(fragment.context).userId,
                viewModel.position, topAdsModel.bannerId ?: "", topAdsModel.shopId)
    }
}