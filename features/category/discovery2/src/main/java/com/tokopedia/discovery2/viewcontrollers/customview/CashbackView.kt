package com.tokopedia.discovery2.viewcontrollers.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.databinding.CashbackLayoutBinding
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("RestrictedApi")
class CashbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = CashbackLayoutBinding.inflate(LayoutInflater.from(context), this)
    private var shapeDrawable: MaterialShapeDrawable? = null

    init {
        val pathModel = constructShapeAppearance()

        shapeDrawable = MaterialShapeDrawable(pathModel)
        val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_RN300)
        shapeDrawable?.setTint(defaultColor)

        background = shapeDrawable

        binding.cardBackground.shapeAppearanceModel = pathModel
    }

    fun renderCashback(title: String?, colors: ArrayList<String>?) {
        isVisible = !title.isNullOrEmpty()

        if (title.isNullOrEmpty()) return
        binding.cashback.text = title

        if (colors.isNullOrEmpty()) return

        val drawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(colors.first()), Color.parseColor(colors[1]))
        )

        renderBackgroundColor(drawable)
    }

    fun changeToInactive() {
        val drawable = ColorDrawable(
            ContextCompat.getColor(
                context,
                unifyprinciplesR.color.Unify_NN100
            )
        )

        renderBackgroundColor(drawable)

        binding.cashback.setTextColor(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN400))
    }

    private fun constructShapeAppearance(): ShapeAppearanceModel {
        val shapeAppearanceBuilder = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, CORNER_SIZE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val edgeTreatment = BottomAppBarTopEdgeTreatment(0f, 0f, 0f)
                .apply {
                    fabDiameter = SCALLOP_DIAMETER
                }

            shapeAppearanceBuilder
                .setRightEdge(edgeTreatment)
                .setLeftEdge(edgeTreatment)
        }

        return shapeAppearanceBuilder.build()
    }

    private fun renderBackgroundColor(drawable: Drawable) {
        binding.cashback.addOneTimeGlobalLayoutListener {
            binding.cardBackground.apply {
                maxHeight = binding.cashback.height

                loadImage(drawable)
            }
        }
    }

    companion object {
        private const val CORNER_SIZE = 8f
        private const val SCALLOP_DIAMETER = 10f
    }
}
