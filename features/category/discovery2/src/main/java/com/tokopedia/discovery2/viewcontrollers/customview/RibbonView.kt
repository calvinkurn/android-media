package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
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

    private var shapeDrawable: MaterialShapeDrawable? = null

    init {
        val backgroundColor = ContextCompat.getColorStateList(
            context,
            unifyprinciplesR.color.Unify_RN600
        )

        backgroundColor?.let {
            renderRibbonShape(it)
        }
    }

    fun setText(value: String?) {
        binding.percentageValue.text = value
        binding.percentageLabel.text = "%"
    }

    fun setType(value: Int) {
        binding.percentageValue.setType(value)
        binding.percentageLabel.textSize = 8f
    }

    fun setBackgroundColor(color: String?) {
        if (color.isNullOrEmpty()) return

        renderRibbonShape(ColorStateList.valueOf(Color.parseColor(color)))
    }

    fun setFontColor(color: String?) {
        if (color.isNullOrEmpty()) return

        val intColor = Color.parseColor(color)
        binding.percentageValue.setTextColor(intColor)
        binding.percentageLabel.setTextColor(intColor)
    }

    fun changeToDisable(textColor: Int, backgroundColor: Int) {
        renderRibbonShape(ColorStateList.valueOf(backgroundColor))

        binding.percentageValue.setTextColor(textColor)
        binding.percentageLabel.setTextColor(textColor)
    }

    private fun renderRibbonShape(backgroundColor: ColorStateList) {
        binding.percentageValue.addOneTimeGlobalLayoutListener {
            val ribbonPathModel = constructShapeAppearance(binding.container.width)

            shapeDrawable = MaterialShapeDrawable(ribbonPathModel).apply {
                fillColor = backgroundColor
            }

            background = shapeDrawable
        }
    }

    private fun constructShapeAppearance(width: Int): ShapeAppearanceModel {
        val shapeAppearanceBuilder = ShapeAppearanceModel.builder()
            .setTopRightCorner(
                CornerFamily.ROUNDED,
                convertDpToPixel(CORNER_SIZE, context).toFloat()
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val edgeTreatment = TriangleEdgeTreatment(
                width.toFloat() / 2,
                HEIGHT_RATIO,
                false
            )

            shapeAppearanceBuilder.setBottomEdge(edgeTreatment)
        }

        return shapeAppearanceBuilder.build()
    }

    companion object {
        private const val HEIGHT_RATIO = 3
        private const val CORNER_SIZE = 12f
    }
}
