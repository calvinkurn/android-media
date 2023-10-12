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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.ItemMerchantVoucherCarouselLayoutBinding
import com.tokopedia.discovery2.viewcontrollers.fragment.NotchEdgeTreatment
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("RestrictedApi")
class MerchantVoucherViewCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = ItemMerchantVoucherCarouselLayoutBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.divider.addOneTimeGlobalLayoutListener {
            val rightPosition = binding.divider.y - 15
            val leftPosition = binding.root.height - rightPosition - 95

            val rightEdge = NotchEdgeTreatment(
                horizontalOffset = (rightPosition)
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
            val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
            materialShapeDrawable.setTint(defaultColor)
            background = materialShapeDrawable
        }
    }

    fun setData(item: DataItem) {
        item.shopInfo?.run {
            binding.tvShopName.text = name.orEmpty()
            binding.shopLogo.setImageUrl(iconUrl.orEmpty())
        }

        binding.cardBackground.setImageUrl(item.backgroundImageUrl.orEmpty())
        binding.actionBtn.text = item.buttonText.orEmpty()
        binding.tvTitle.text = item.title
        binding.tvSubtitle.text = item.subtitle
        binding.tvMinPurchase.text = item.subtitle_1

        if (!item.fontColor.isNullOrEmpty()) {
            binding.tvTitle.setTextColor(Color.parseColor(item.fontColor))
        }
    }

    fun onClick(action: () -> Unit) {
        binding.root.setOnClickListener {
            action.invoke()
        }
    }
}
