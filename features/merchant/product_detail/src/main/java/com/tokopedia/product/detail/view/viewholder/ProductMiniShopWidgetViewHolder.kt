package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold
import kotlinx.android.synthetic.main.item_mini_shop_widget.view.*

class ProductMiniShopWidgetViewHolder(
        private val view: View,
        private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductMiniShopWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_shop_widget
    }

    init {
        showLoading()
    }

    override fun bind(element: ProductMiniShopWidgetDataModel) = with(view) {
        shop_ava.loadImageCircle(element.shopAva)
        shop_name.text = MethodChecker.fromHtml(element.shopName)
        shop_location_online.text = element.shopLocation

        shop_ava.setOnClickListener { listener.goToTokoNow() }
        shop_name.setOnClickListener { listener.goToTokoNow() }

        hideLoading()
    }


    private fun hideLoading() = with(view) {
        mini_shop_widget_container.show()
        mini_shop_widget_shimmering.hide()
    }

    private fun showLoading() = with(view) {
        mini_shop_widget_container.hide()
        mini_shop_widget_shimmering.show()
    }

    // TODO Vindo - Remove if not used for later
    private fun getComponentTrackData(element: ProductMiniShopWidgetDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

}