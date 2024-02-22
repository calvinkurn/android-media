package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTabletLeftSectionDataModel
import com.tokopedia.product.detail.databinding.ItemTabletModeBinding
import com.tokopedia.product.detail.view.adapter.diffutil.ProductDetailDiffUtilCallback
import com.tokopedia.product.detail.view.adapter.dynamicadapter.ProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.product.detail.view.listener.ProductDetailListener

class TabletLeftSectionViewHolder(
    view: View,
    private val listener: ProductDetailListener,
    adapterTypeFactory: ProductDetailAdapterFactory
) : AbstractViewHolder<ProductTabletLeftSectionDataModel>(view) {

    private val binding = ItemTabletModeBinding.bind(view)
    private val asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel> by lazy {
        AsyncDifferConfig.Builder(ProductDetailDiffUtilCallback())
            .build()
    }

    private val adapter: ProductDetailAdapter by lazy {
        ProductDetailAdapter(
            asyncDifferConfig, listener, adapterTypeFactory
        )
    }

    init {
        binding.pdpRvLeftTablet.adapter = adapter
    }

    companion object {
        val LAYOUT = R.layout.item_tablet_mode
    }

    override fun bind(element: ProductTabletLeftSectionDataModel) {
        adapter.submitList(element.listVisitable)
    }

    override fun bind(element: ProductTabletLeftSectionDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        adapter.submitList(element.listVisitable)
    }

    fun detachView() {
        listener.getProductVideoCoordinator()?.onPause()
    }
}
