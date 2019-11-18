package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewVisibilityListener
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialHeaderView
import com.tokopedia.product.detail.view.util.OnImageClick
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(itemView: View, private val onImageClick: OnImageClick,
                                private val childFragmentManager: FragmentManager) : AbstractViewHolder<ProductSnapshotDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun bind(element: ProductSnapshotDataModel) {
        val screenWidth = itemView.resources.displayMetrics.widthPixels
        itemView.view_picture_search_bar.layoutParams.height = screenWidth

        val header = PartialHeaderView.build(itemView.base_header, null)
        header.renderData(element.productInfoP1)
        header.showOfficialStore(element.shopInfo.goldOS)
        header.renderCod(element.shouldShowCod)
        element.nearestWarehouse?.let {
            if (it.warehouseInfo.id.isNotBlank())
                header.updateStockAndPriceWarehouse(it, element.productInfoP1.campaign)
        }

        itemView.view_picture_search_bar.isVisibleOnTheScreen(object : ViewVisibilityListener {
            override fun onViewNotVisible() {
                itemView.fab_detail.hide()
            }

            override fun onViewVisible() {
                itemView.fab_detail.show()
            }

        })

        itemView.view_picture_search_bar.renderData(element.media, onImageClick, childFragmentManager)
    }

}