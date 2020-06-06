package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.changeConstraint
import com.tokopedia.play.broadcaster.util.compatTransitionName
import com.tokopedia.play.broadcaster.util.doOnPreDraw

/**
 * Created by jegul on 26/05/20
 */
class ProductPreviewViewHolder(
        itemView: View,
        gridLayoutManager: GridLayoutManager
) : BaseViewHolder(itemView) {

    val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

    val spacing = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    private val spanSizeLookup = gridLayoutManager.spanSizeLookup
    private val spanCount = gridLayoutManager.spanCount

    fun bind(item: ProductContentUiModel, itemCount: Int) {
        if (itemView.height != 0) {
            adjustSize(itemCount)
        } else {
            itemView.doOnPreDraw {
                adjustSize(itemCount)
            }
        }

        ivImage.loadImage(item.imageUrl)
        ivImage.compatTransitionName = item.transitionName
    }

    private fun adjustSize(itemCount: Int) {
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
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_preview
    }
}