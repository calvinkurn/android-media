package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant.BrandRecommendation.RECTANGLE_DESIGN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.unifycomponents.ImageUnify

class BrandRecommendationItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var brandRecommendationItemViewModel: BrandRecommendationItemViewModel
    private val brandImage: ImageUnify = itemView.findViewById(R.id.brand_recom_iv)
    private val context = itemView.context


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationItemViewModel = discoveryBaseViewModel as BrandRecommendationItemViewModel
        updateCardDesign()
        setClick(brandRecommendationItemViewModel.getComponentItem())
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            brandRecommendationItemViewModel.getComponentDataLiveData().observe(lifecycle, { item ->
                item.data?.firstOrNull()?.let {
                    try {
                        if (context.isValidGlideContext())
                            it.imageUrlMobile?.let { url -> brandImage.setImageUrl(url) }
                    } catch (e: Throwable) {
                    }
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            brandRecommendationItemViewModel.getComponentDataLiveData().removeObservers(it)
        }
    }

    private fun updateCardDesign() {
        when (brandRecommendationItemViewModel.getDesignType()) {
            RECTANGLE_DESIGN -> {
                val cardPadding = context.resources.getDimension(R.dimen.dp_4).toInt()
                val layoutParams: ViewGroup.LayoutParams = itemView.layoutParams
                layoutParams.width = context.resources.getDimension(R.dimen.dp_90).toInt()
                layoutParams.height = context.resources.getDimension(R.dimen.dp_60).toInt()
                brandImage.scaleType = ImageView.ScaleType.FIT_CENTER
                brandImage.setPadding(cardPadding, cardPadding, cardPadding, cardPadding)
                itemView.layoutParams = layoutParams
            }
        }
    }

    private fun setClick(data: DataItem?) {
        data?.let {
            if (!it.applinks.isNullOrEmpty()) {
                itemView.setOnClickListener { itemView ->
                    RouteManager.route(itemView.context, it.applinks)
                    sendClickBrandRecommendationClickEvent(it)
                }
            }
        }
    }

    private fun sendClickBrandRecommendationClickEvent(it: DataItem) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(it, adapterPosition)
    }
}
