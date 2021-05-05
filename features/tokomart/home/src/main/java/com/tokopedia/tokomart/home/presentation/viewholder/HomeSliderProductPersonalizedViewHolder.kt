package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeSliderProductPersonalizedAdapter
import com.tokopedia.tokomart.home.presentation.adapter.delegate.TokoMartHomeSliderProductPersonalizedDelegate
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderBannerUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductPersonalizedUiModel
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_tokomart_slider_product_personalized.view.*

class HomeSliderProductPersonalizedViewHolder(
        itemView: View
): AbstractViewHolder<HomeSliderProductPersonalizedUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_slider_product_personalized
    }

    override fun bind(element: HomeSliderProductPersonalizedUiModel?) {
        val adapter = TokoMartHomeSliderProductPersonalizedAdapter()
        itemView.rv_slider_product_personalized.adapter = adapter
        itemView.rv_slider_product_personalized.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        adapter.addItems(listOf(HomeSliderProductPersonalizedUiModel(), HomeSliderProductPersonalizedUiModel(), HomeSliderProductPersonalizedUiModel()))
    }

}