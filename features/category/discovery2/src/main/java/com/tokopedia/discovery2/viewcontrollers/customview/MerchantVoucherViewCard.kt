package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.graphics.Color
import android.os.Build
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
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MerchantVoucherViewCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = ItemMerchantVoucherCarouselLayoutBinding
        .inflate(LayoutInflater.from(context), this)

    init {
        binding.divider.addOneTimeGlobalLayoutListener {
            val shapePathModel = constructShapeAppearance()

            val materialShapeDrawable = MaterialShapeDrawable(shapePathModel)
            val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
            val strokeColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN600)

            materialShapeDrawable.apply {
                setTint(defaultColor)
                setStroke(0.5f, strokeColor)
            }

            background = materialShapeDrawable
            binding.cardBackground.shapeAppearanceModel = shapePathModel
        }
    }

    private fun constructShapeAppearance(): ShapeAppearanceModel {
        val shapeAppearanceBuilder = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, CORNER_SIZE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val rightEdge = getRightEdge(binding.divider.y, binding.divider.height)
            val leftEdge = getLeftEdge(
                binding.divider.y,
                binding.divider.height,
                binding.root.height
            )

            shapeAppearanceBuilder
                .setLeftEdge(leftEdge)
                .setRightEdge(rightEdge)
        }

        return shapeAppearanceBuilder.build()
    }

    fun setData(item: DataItem) {
        item.shopInfo?.run {
            binding.tvShopName.text = name.orEmpty()
            binding.shopLogo.setImageUrl(iconUrl.orEmpty())
        }

        loadCardBackground(item)

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

        binding.actionBtn.setOnClickListener {
            action.invoke()
        }
    }

    private fun getRightEdge(
        dividerPosition: Float,
        dividerHeight: Int
    ): NotchEdgeTreatment {
        val rightOffset = convertDpToPixel((dividerHeight).toFloat(), context)
        val rightPosition = dividerPosition - rightOffset

        return NotchEdgeTreatment(
            horizontalOffset = rightPosition
        )
            .apply {
                scallopDiameter = SCALLOP_DIAMETER
            }
    }

    private fun getLeftEdge(
        dividerPosition: Float,
        dividerHeight: Int,
        cardHeight: Int
    ): NotchEdgeTreatment {
        val heightPortion = 0.35f
        val leftOffset = convertDpToPixel((heightPortion * dividerHeight), context)
        val leftPosition = dividerPosition + leftOffset

        return NotchEdgeTreatment(
            horizontalOffset = leftPosition,
            cardHeight = cardHeight.toFloat(),
            isLeftEdge = true
        ).apply {
            scallopDiameter = SCALLOP_DIAMETER
        }
    }

    private fun loadCardBackground(item: DataItem) {
        binding.tvTitle.addOneTimeGlobalLayoutListener {
            binding.cardBackground.apply {
                val textViewHeight = convertDpToPixel(binding.tvTitle.height.toFloat(), context)
                layoutParams.height = binding.tvTitle.y.toInt() + textViewHeight
                requestLayout()
            }
        }

        binding.cardBackground.loadImage(item.backgroundImageUrl.orEmpty())
    }

    companion object {
        private const val SCALLOP_DIAMETER = 40f
        private const val CORNER_SIZE = 40f
    }
}
