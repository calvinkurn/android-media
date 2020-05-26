package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.changeConstraint
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel

/**
 * Created by jegul on 26/05/20
 */
class ProductPreviewViewHolder(itemView: View, gridLayoutManager: GridLayoutManager) : BaseViewHolder(itemView) {

    private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

    val spacing = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    private val spanSizeLookup = gridLayoutManager.spanSizeLookup
    private val spanCount = gridLayoutManager.spanCount

    fun bind(item: ProductUiModel, itemCount: Int) {
        itemView.viewTreeObserver.addOnPreDrawListener {
            when (itemCount) {
                1 -> {
                    itemView.changeConstraint {
                        setDimensionRatio(ivImage.id, "W, 1:1")
                    }
                }
                else -> {
                    itemView.changeConstraint {
                        val ratio = if (spanSizeLookup.getSpanSize(adapterPosition) == spanCount) {
                            val widthRatio = itemView.height / ((itemView.height - spacing) / 2f)
                            widthRatio.toString()
                        } else "1"

                        val finalRatio = "W, 1:$ratio"

                        val constraint = getConstraint(ivImage.id)
                        if (constraint.layout.dimensionRatio != finalRatio) setDimensionRatio(ivImage.id, finalRatio)
                    }
                }
            }
            true
        }

        ivImage.loadImage(item.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_product_preview
    }
}