package com.tokopedia.product_ar.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ComparissonImageUiModel
import com.tokopedia.product_ar.util.ItemDividerGrid
import com.tokopedia.product_ar.view.fragment.ComparissonHelperListener
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

class GridImageViewHolder(itemView: View, val listener: ComparissonHelperListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.grid_image_view_holder
    }

    private val imgFull: ShapeableImageView? = itemView.findViewById(R.id.grid_img)
    private val txtTitle: Typography? = itemView.findViewById(R.id.txt_title_var_small)
    private val radius = itemView.context.resources.getDimension(R.dimen.image_comparison_size)

    fun bind(data: ComparissonImageUiModel) {
        if (listener.getRecyclerViewFullHeight() != 0) {
            imgFull?.layoutParams?.height = listener.getRecyclerViewFullHeight() / 2 -
                    (ItemDividerGrid.MARGIN_PER_GRID * 2).dpToPx().roundToInt()
        }

        imgFull?.shapeAppearanceModel = imgFull?.shapeAppearanceModel?.toBuilder()
                ?.setAllCorners(CornerFamily.ROUNDED, radius)
                ?.build() ?: ShapeAppearanceModel()
        imgFull?.loadImage(data.bitmap)
        txtTitle?.text = data.productName
    }
}