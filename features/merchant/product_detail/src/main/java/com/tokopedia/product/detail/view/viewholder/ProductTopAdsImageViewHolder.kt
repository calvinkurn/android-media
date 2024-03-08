package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.topads.sdk.widget.TdnBannerView

class ProductTopAdsImageViewHolder(
    val view: View,
    val listener: ProductDetailListener
) : AbstractViewHolder<TopAdsImageDataModel>(view) {

    private val topAdsTdnView: TdnBannerView = view.findViewById(R.id.adsTdnView)

    companion object {
        val LAYOUT = R.layout.item_top_ads_image_view
    }

    override fun bind(element: TopAdsImageDataModel) {
        if (!element.data.isNullOrEmpty()) {
            val bannerId = element.data?.firstOrNull()?.bannerId ?: ""
            val bannerName = element.data?.firstOrNull()?.bannerName ?: ""
            element.data?.let {
                val tdnBannerList = TdnHelper.categoriesTdnBanners(it)
                val tdnBanner = tdnBannerList.toList().firstOrNull()
                if (tdnBanner != null) {
                    topAdsTdnView.renderTdnBanner(
                        tdnBanner,
                        onTdnBannerClicked = { banner ->
                            listener.onTopAdsImageViewClicked(
                                element,
                                banner.applink,
                                bannerId,
                                bannerName
                            )
                        },
                        onLoadFailed = { listener.removeComponent(element.name()) },
                        onTdnBannerImpressed = {
                            listener.onTopAdsImageViewImpression(element, bannerId, bannerName)
                        }
                    )
                    view.addOnImpressionListener(
                        holder = element.impressHolder,
                        holders = listener.getImpressionHolders(),
                        name = element.name,
                        useHolders = listener.isRemoteCacheableActive()
                    ) {
                        listener.onImpressComponent(getComponentTrackData(element))
                    }
                }
            }
        }
    }

    private fun getComponentTrackData(
        element: TopAdsImageDataModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}
