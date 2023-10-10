package com.tokopedia.discovery2.viewcontrollers.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.discovery2.databinding.ItemMerchantVoucherCarouselLayoutBinding
import com.tokopedia.discovery2.viewcontrollers.fragment.NotchEdgeTreatment
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener

@SuppressLint("RestrictedApi")
class MerchantVoucherViewCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = ItemMerchantVoucherCarouselLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.divider.addOneTimeGlobalLayoutListener {
            val rightPosition = binding.divider.y - 15
            val leftPosition = binding.root.height - rightPosition - 95

            val rightEdge = NotchEdgeTreatment(
                horizontalOffset = (rightPosition),
            )
                .apply {
                    scallopDiameter = (2 * 20).toFloat()
                }

            val leftEdge = NotchEdgeTreatment(
                horizontalOffset = (leftPosition),
                isLeftEdge = true
            )
                .apply {
                    scallopDiameter = (2 * 20).toFloat()
                }

            val shapePathModel = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 40.toFloat())
                .setLeftEdge(leftEdge)
                .setRightEdge(rightEdge)
                .build()

            val materialShapeDrawable = MaterialShapeDrawable(shapePathModel)
            materialShapeDrawable.setTint(Color.WHITE)
            background = materialShapeDrawable
        }
    }
}
