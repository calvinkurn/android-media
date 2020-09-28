package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant.BrandRecommendation.RECTANGLE_DESIGN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder

class BrandRecommendationItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var brandRecommendationItemViewModel: BrandRecommendationItemViewModel
    private val imageView: ImageView = itemView.findViewById(R.id.brand_recom_iv)
    private val context = itemView.context


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationItemViewModel = discoveryBaseViewModel as BrandRecommendationItemViewModel
        updateCardDesign()
        brandRecommendationItemViewModel.getComponentDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            item.data?.get(0)?.let {
                it.imageUrlMobile?.let { it1 -> imageView.loadImageWithoutPlaceholder(it1) }
                setClick(item.data?.get(0))
            }
        })
    }

    private fun updateCardDesign() {
        when (brandRecommendationItemViewModel.getDesignType()) {
            RECTANGLE_DESIGN -> {
                val cardPadding =  context.resources.getDimension(R.dimen.dp_4).toInt()
                val layoutParams: ViewGroup.LayoutParams = itemView.layoutParams
                layoutParams.width = context.resources.getDimension(R.dimen.dp_90).toInt()
                layoutParams.height = context.resources.getDimension(R.dimen.dp_60).toInt()
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.setPadding(cardPadding, cardPadding, cardPadding, cardPadding)
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