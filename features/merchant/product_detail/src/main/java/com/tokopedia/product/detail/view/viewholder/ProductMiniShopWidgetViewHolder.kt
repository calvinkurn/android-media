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
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

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

    private val shopAva: ImageUnify? = view.findViewById(R.id.shop_ava)
    private val shopName: Typography? =  view.findViewById(R.id.shop_name)
    private val shopLocation: Typography? = view.findViewById(R.id.shop_location_online)
    private val container: View? = view.findViewById(R.id.mini_shop_widget_container)
    private val shimmering: View? =  view.findViewById(R.id.mini_shop_widget_shimmering)

    override fun bind(element: ProductMiniShopWidgetDataModel) = with(view) {

        val componentTracker = getComponentTrackData(element)

        shopAva?.apply {
            loadImageCircle(element.shopAva)
            setOnClickListener { listener.gotoShopDetail(componentTracker) }
        }
        shopName?.apply {
            text = MethodChecker.fromHtml(element.shopName)
            setOnClickListener { listener.gotoShopDetail(componentTracker) }
        }
        shopLocation?.text = element.shopLocation

        hideLoading()
    }


    private fun hideLoading() = with(view) {
        container?.show()
        shimmering?.hide()
        return@with
    }

    private fun showLoading() = with(view) {
        container?.hide()
        shimmering?.show()
        return@with
    }

    private fun getComponentTrackData(element: ProductMiniShopWidgetDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}