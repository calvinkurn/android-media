package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialHeaderView
import com.tokopedia.product.detail.view.util.OnImageClick
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(itemView: View, private val onImageClick: OnImageClick,
                                private val childFragmentManager: FragmentManager) : AbstractViewHolder<ProductSnapshotDataModel>(itemView) {

    override fun bind(element: ProductSnapshotDataModel) {
        val screenWidth = itemView.resources.displayMetrics.widthPixels
        itemView.view_picture_search_bar.layoutParams.height = screenWidth
        val header = PartialHeaderView.build(itemView.base_header, null)
        header.renderData(element.productInfoP1)
        itemView.view_picture_search_bar.renderData(element.media, onImageClick, childFragmentManager)
    }

}