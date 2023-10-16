package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.discovery2.databinding.RibbonLayoutBinding
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class RibbonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = RibbonLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.percentage.addOneTimeGlobalLayoutListener {
            val ribbonPathModel = ShapeAppearanceModel.builder()
                .setTopRightCorner(CornerFamily.ROUNDED, convertDpToPixel(CORNER_SIZE, context).toFloat())
                .setBottomEdge(
                    TriangleEdgeTreatment(
                        binding.percentage.width.toFloat() / 2,
                        HEIGHT_RATIO,
                        false
                    )
                )
                .build()

            val ribbonDrawable = MaterialShapeDrawable(ribbonPathModel)
            ribbonDrawable.fillColor = ContextCompat.getColorStateList(
                context,
                unifyprinciplesR.color.Unify_RN600
            )

            background = ribbonDrawable
        }
    }

    companion object {
        private const val HEIGHT_RATIO = 2
        private const val CORNER_SIZE = 12f
    }
}
