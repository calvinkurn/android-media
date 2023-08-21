package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.databinding.LayoutRibbonBinding
import kotlin.math.roundToInt

class Ribbon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = LayoutRibbonBinding.inflate(LayoutInflater.from(context), this)


    private companion object {
        private const val PADDING_VERTICAL = 8
        private const val PADDING_HORIZONTAL = 8
        private const val CORNER_RADIUS = 16
        private const val FOLD_RADIUS = 10
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        applyEdgeTreatment()
        setContentPadding(
            dpToPx(context, PADDING_VERTICAL).toInt(),
            dpToPx(context, PADDING_HORIZONTAL).toInt(),
            dpToPx(context, PADDING_VERTICAL).toInt(),
            dpToPx(context, PADDING_HORIZONTAL).toInt())
    }

    private fun applyEdgeTreatment() {
        val radius = dpToPx(context, CORNER_RADIUS)
        val radiusFold = dpToPx(context, FOLD_RADIUS)

        val foldAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(OutsideCircleCornerTreatment(radiusFold))
            .build()

        val innerAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()


        this.shapeAppearanceModel = foldAppearanceModel
        binding.cardView.shapeAppearanceModel = innerAppearanceModel
        background = MaterialShapeDrawable(foldAppearanceModel)
        binding.cardView.background = MaterialShapeDrawable(innerAppearanceModel)
        elevation = 0f
        binding.cardView.elevation = 0f
    }

    fun setData() {
         binding.tvStatus.text = "asd"
         binding.icStatus.visible()
         applyBackgroundColor("#ffffff")
    }

    private fun applyBackgroundColor(color: String) {
        background = background
            .apply {
                setTint(darkenColor(parseColor(color)
                    ?: com.tokopedia.unifyprinciples.R.color.Unify_NN1000_32, 0.8f))
            }

        binding.cardView.background = binding.cardView.background
            .apply {
                setTint(parseColor(color)
                    ?: com.tokopedia.unifyprinciples.R.color.Unify_NN100)
            }
    }


    private fun darkenColor(color: Int, factor: Float): Int {
        val a: Int = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()

        return Color.argb(a,
            r.coerceAtMost(255),
            g.coerceAtMost(255),
            b.coerceAtMost(255))
    }
}
