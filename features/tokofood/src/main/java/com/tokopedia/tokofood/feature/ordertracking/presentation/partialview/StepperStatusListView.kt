package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStepperStatusBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel
import com.tokopedia.unifycomponents.ImageUnify


class StepperStatusListView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private var stepperStatusList: List<StepperStatusUiModel>? = null

    fun setStepperStatusListView(stepperStatusList: List<StepperStatusUiModel>) {
        removeAllViews()
        weightSum = MAX_WEIGHT_SUM
        this.stepperStatusList = stepperStatusList
        stepperStatusList.forEachIndexed { index, stepperStatusUiModel ->
            createStepperItemView(stepperStatusUiModel, index + Int.ONE, stepperStatusList.size)
        }
    }

    fun updateStepperStatus(newStepperStatusList: List<StepperStatusUiModel>) {
        if (newStepperStatusList != stepperStatusList) {

            stepperStatusList?.forEachIndexed { index, stepperStatusUiModel ->
                if (stepperStatusUiModel.isIconActive !=
                    newStepperStatusList.getOrNull(index)?.isIconActive
                ) {
                    newStepperStatusList.getOrNull(index)?.let {
                        val icOrderTrackingStatus: IconUnify? =
                            (getChildAt(index) as? ViewGroup)?.getChildAt(Int.ZERO) as? IconUnify
                        updateStepperIcon(icOrderTrackingStatus, it)
                    }
                }
                if (stepperStatusUiModel.isLineActive !=
                    newStepperStatusList.getOrNull(index)?.isLineActive
                ) {
                    newStepperStatusList.getOrNull(index)?.let {
                        val viewOrderTrackingStatusLine: ImageUnify? =
                            (getChildAt(index) as? ViewGroup)?.getChildAt(Int.ONE) as? ImageUnify
                        updateStepperLine(viewOrderTrackingStatusLine, it)
                    }
                }
            }
            this.stepperStatusList = newStepperStatusList
        }
    }

    private fun createStepperItemView(
        stepperStatus: StepperStatusUiModel,
        position: Int,
        itemCount: Int
    ) {
        val binding = if (position == itemCount - Int.ZERO) {
            createLastItem(stepperStatus)
        } else {
            createDefaultItem(stepperStatus)
        }
        addView(binding.root)
    }

    private fun createDefaultItem(stepperStatus: StepperStatusUiModel):
            ItemTokofoodOrderTrackingStepperStatusBinding {
        return ItemTokofoodOrderTrackingStepperStatusBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        ).apply {
            root.layoutParams.apply {
                (this as? LayoutParams)?.weight = WEIGHT_DEFAULT
            }
            icOrderTrackingStatus.run {
                show()
                setImage(
                    stepperStatus.iconName,
                    newLightEnable = getColorBasedOnActive(stepperStatus.isIconActive),
                    newDarkEnable = getColorBasedOnActive(stepperStatus.isIconActive)                )
            }
            viewOrderTrackingStatusLine.run {
                show()
                setColorFilter(
                    getColorBasedOnActive(stepperStatus.isLineActive)
                )
            }
        }
    }

    private fun createLastItem(stepperStatus: StepperStatusUiModel): ItemTokofoodOrderTrackingStepperStatusBinding {
        return ItemTokofoodOrderTrackingStepperStatusBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        ).apply {
            root.layoutParams.apply {
                (this as? LayoutParams)?.weight = WEIGHT_LAST_ITEM
            }
            viewOrderTrackingStatusLine.hide()
            icOrderTrackingStatus.run {
                show()
                setImage(
                    stepperStatus.iconName,
                    newLightEnable = getColorBasedOnActive(stepperStatus.isIconActive),
                    newDarkEnable = getColorBasedOnActive(stepperStatus.isIconActive)
                )
            }
        }
    }

    private fun updateStepperIcon(
        icOrderTrackingStatus: IconUnify?,
        stepperStatus: StepperStatusUiModel
    ) {
        icOrderTrackingStatus?.run {
            show()
            setImage(
                stepperStatus.iconName,
                newLightEnable = getColorBasedOnActive(stepperStatus.isIconActive),
                newDarkEnable = getColorBasedOnActive(stepperStatus.isIconActive)
            )
            invalidate()
        }
    }

    private fun updateStepperLine(
        viewOrderTrackingStatusLine: ImageUnify?,
        stepperStatus: StepperStatusUiModel
    ) {
        viewOrderTrackingStatusLine?.run {
            show()
            setColorFilter(
                getColorBasedOnActive(stepperStatus.isLineActive)
            )
            invalidate()
        }
    }

    private fun getStepperColor(): Pair<Int, Int> {
        val gn400Color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN400
        )
        val nn500Color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN500
        )
        return Pair(gn400Color, nn500Color)
    }

    private fun getColorBasedOnActive(stepperStatusActive: Boolean): Int {
        val (gn400Color, nn500Color) = getStepperColor()
        return if (stepperStatusActive) gn400Color else nn500Color
    }

    companion object {
        const val WEIGHT_DEFAULT = 4.16f
        const val WEIGHT_LAST_ITEM = 0.5f
        const val MAX_WEIGHT_SUM = 13f
    }
}