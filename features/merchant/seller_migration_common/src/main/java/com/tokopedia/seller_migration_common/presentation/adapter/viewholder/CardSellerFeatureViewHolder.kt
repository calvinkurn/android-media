package com.tokopedia.seller_migration_common.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.item_seller_feature_with_card.view.*

class CardSellerFeatureViewHolder(
        itemView: View?,
        private val listener: SellerFeatureCarousel.SellerFeatureClickListener?
) : AbstractViewHolder<SellerFeatureUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_seller_feature_with_card
    }

    override fun bind(element: SellerFeatureUiModel) {
        with(itemView) {
            ivSellerFeature.setImage(element.imageUrl, 0f)
            tvSellerFeatureTitle.text = getString(element.titleId)
            tvSellerFeatureDescription.text = getString(element.descriptionId)
            setOnClickListener { listener?.onSellerFeatureClicked(element) }
        }
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