package com.tokopedia.seller_migration_common.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel

class CardSellerFeatureViewHolder(itemView: View?, private val listener: SellerFeatureCarousel.SellerFeatureClickListener?) : SellerFeatureViewHolder(itemView) {
    companion object {
        val LAYOUT = R.layout.item_seller_feature_with_card
    }

    override fun bind(element: SellerFeatureUiModel) {
        super.bind(element)
        itemView.setOnClickListener { listener?.onSellerFeatureClicked(element) }
    }

    class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val isFirstItem: Boolean = parent.getChildAdapterPosition(view) == 0
            val isLastItem: Boolean = parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
            if (isFirstItem) {
                outRect.left = margin
            } else if (isLastItem) {
                outRect.right = margin
            }
        }
    }
}