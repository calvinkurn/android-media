package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.LoaderUnify

class DiscoveryTDNBannerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), TopAdsImageVieWApiResponseListener, TopAdsImageViewClickListener, TopAdsImageViewImpressionListener {
    private val tdnImageView: TopAdsImageView? = itemView.findViewById(R.id.tdn_view)
    private val mHeaderView: FrameLayout? = itemView.findViewById(R.id.header_view)
    private val shimmerView: LoaderUnify? = itemView.findViewById(R.id.shimmer_view)
    private var shouldHitService = true
    private lateinit var viewModel: DiscoveryTDNBannerViewModel

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
            viewModel.componentLiveData.observe(lifecycleOwner, {
//                TODO::CHANGE SOURCE ID FOR IMAGE_DATA = 5
                addCardHeader(it)
                if (shouldHitService)
                    tdnImageView?.getImageData("5", 1, 3, depId = it.data?.firstOrNull()?.depID
                            ?: "")
            })
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
        checkHeaderVisibility(componentsItem)
    }

    private fun checkHeaderVisibility(componentsItem: ComponentsItem?) {
        componentsItem?.data?.firstOrNull()?.let {
            if (!it.title.isNullOrEmpty() || !it.subtitle.isNullOrEmpty()) {
                mHeaderView?.addView(CustomViewCreator.getCustomViewObject(itemView.context,
                        ComponentsList.LihatSemua, componentsItem, fragment))
            }
        }
    }

    private fun handleError() {
        shouldHitService = false
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
            tdnImageView?.loadImage(imageDataList[0])
        } else
            handleError()
    }

    override fun onError(t: Throwable) {
        handleError()
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
//        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTDNBannerClick(viewModel.components, UserSession(fragment.context).userId)
    }

    override fun onTopAdsImageViewImpression(viewUrl: String) {
//        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTDNBannerImpression(viewModel.components, UserSession(fragment.context).userId)
    }
}