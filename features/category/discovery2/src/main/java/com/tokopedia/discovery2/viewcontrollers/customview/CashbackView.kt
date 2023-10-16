package com.tokopedia.discovery2.viewcontrollers.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.databinding.CashbackLayoutBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("RestrictedApi")
class CashbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = CashbackLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        val edgeTreatment = BottomAppBarTopEdgeTreatment(0f, 0f, 0f)
            .apply {
                fabDiameter = SCALLOP_DIAMETER
            }

        val pathModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, CORNER_SIZE)
            .setRightEdge(edgeTreatment)
            .setLeftEdge(edgeTreatment)
            .build()

        val shapeDrawable = MaterialShapeDrawable(pathModel)
        val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_RN300)
        shapeDrawable.setTint(defaultColor)

        background = shapeDrawable
    }

    companion object {
        private const val CORNER_SIZE = 8f
        private const val SCALLOP_DIAMETER = 10f
    }
}
