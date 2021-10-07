package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TDN_ADS_COUNT
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TDN_DIMEN_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TDN_INVENTORY_ID
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class UohTdnBannerViewHolder(itemView: View) :
        UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    private val tdnBanner: TopAdsImageView by lazy {
        itemView.findViewById<TopAdsImageView>(
                R.id.uohTdnBanner
        )
    }

    private fun bindTdn(element: TopAdsImageViewModel) {

        if (element.imageUrl?.isEmpty() == true) {
            tdnBanner.getImageData(TDN_INVENTORY_ID, TDN_ADS_COUNT, TDN_DIMEN_ID)
        }

        tdnBanner.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                if (imageDataList.isNotEmpty()) tdnBanner.loadImage(imageDataList[0])
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        })

        tdnBanner.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
            override fun onTopAdsImageViewClicked(applink: String?) {
                RouteManager.route(itemView.context, applink)
            }

        })
    }


    override fun bind(item: UohTypeData, position: Int) {
        bindTdn(item.dataObject as TopAdsImageViewModel)
    }
}
