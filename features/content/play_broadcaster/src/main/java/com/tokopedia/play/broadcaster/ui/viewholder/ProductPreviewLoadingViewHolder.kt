package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play_common.util.extension.changeConstraint
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by jegul on 26/05/20
 */
class ProductPreviewLoadingViewHolder(
        itemView: View,
        gridLayoutManager: GridLayoutManager
) : BaseViewHolder(itemView), SpacingProvider {

    val loaderPreview: LoaderUnify = itemView.findViewById(R.id.loader_preview)

    override val spacing = itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    private val spanSizeLookup = gridLayoutManager.spanSizeLookup
    private val spanCount = gridLayoutManager.spanCount

    fun bind(item: ProductLoadingUiModel, itemCount: Int) {
        if (itemView.height != 0) {
            adjustSize(itemCount)
        } else {
            itemView.doOnPreDraw {
                adjustSize(itemCount)
            }
        }
    }

    private fun adjustSize(itemCount: Int) {
        when (itemCount) {
            1 -> {
                itemView.changeConstraint {
                    setDimensionRatio(loaderPreview.id, "W, 1:1")
                }
            }
            else -> {
                itemView.changeConstraint {
                    val ratio = if (spanSizeLookup.getSpanSize(adapterPosition) == spanCount) {
                        val widthRatio = itemView.height / ((itemView.height - spacing) / 2f)
                        widthRatio.toString()
                    } else "1"

                    val finalRatio = "W, 1:$ratio"

                    val constraint = getConstraint(loaderPreview.id)
                    if (constraint.layout.dimensionRatio != finalRatio) setDimensionRatio(loaderPreview.id, finalRatio)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_preview_loading
    }
}