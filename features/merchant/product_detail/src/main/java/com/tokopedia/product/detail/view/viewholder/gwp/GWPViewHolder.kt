package com.tokopedia.product.detail.view.viewholder.gwp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicProductGwpBinding
import com.tokopedia.product.detail.view.componentization.ComponentEvent
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.gwp.callback.GWPCallback
import com.tokopedia.product.detail.view.viewholder.gwp.widget.GWPWidgetListener

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPViewHolder(
    private val view: View,
    private val gwpCallback: GWPCallback
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

    private var uiModel: GWPUiModel? = null

    override fun bind(element: GWPUiModel) {
        uiModel = element
        binding.pdpGwpWidget.setData(state = element.state)
    }

    override fun bind(element: GWPUiModel?, payloads: MutableList<Any>) {
        val mElement = element ?: return
        if (payloads.isEmpty()) return

        bind(element = mElement)
    }

    override fun getRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return gwpCallback.parentRecyclerViewPool
    }

    override fun getImpressionHolder(): ImpressHolder? = uiModel?.impressHolder

    override fun getImpressionHolders(): MutableList<String> = gwpCallback.impressionHolders

    override fun onImpressed() {
        val element = uiModel ?: return
        gwpCallback.event(BasicComponentEvent.OnImpressed(getComponentTrackData(element)))
    }

    override fun isRemoteCacheableActive(): Boolean = gwpCallback.isRemoteCacheableActive

    override fun event(event: ComponentEvent) {
        gwpCallback.event(event)
    }
}
