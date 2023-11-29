package com.tokopedia.product.detail.view.viewholder.gwp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicProductGwpBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.widget.GWPWidgetListener
import com.tokopedia.product.detail.view.viewholder.gwp.widget.GWPWidgetTracker

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<GWPUiModel>(view), GWPWidgetListener {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_gwp
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicProductGwpBinding.bind(view)
    }

    init {
        binding.pdpGwpWidget.init(this)
    }

    override fun bind(element: GWPUiModel) {
        binding.pdpGwpWidget.setData(
            state = element.state,
            tracker = object : GWPWidgetTracker {
                override fun getImpressionHolder(): ImpressHolder = element.impressHolder

                override fun getImpressionHolders(): MutableList<String> = listener.getImpressionHolders()

                override fun onImpressed() {
                    listener.onImpressComponent(getComponentTrackData(element))
                }

                override fun isCacheable(): Boolean = listener.isRemoteCacheableActive()

                override fun onClickTracking(data: GWPWidgetUiModel) {
                }
            }
        )
    }

    override fun bind(element: GWPUiModel?, payloads: MutableList<Any>) {
        val mElement = element ?: return
        if (payloads.isEmpty()) return

        bind(element = mElement)
    }

    override fun goToAppLink(appLink: String) {
        listener.goToApplink(url = appLink)
    }

    override fun goToWebView(link: String) {
        listener.goToWebView(url = link)
    }

    override fun getRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return listener.getParentRecyclerViewPool()
    }
}
