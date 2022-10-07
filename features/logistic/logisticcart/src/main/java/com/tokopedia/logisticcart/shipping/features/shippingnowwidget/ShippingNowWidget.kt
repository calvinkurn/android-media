package com.tokopedia.logisticcart.shipping.features.shippingnowwidget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ItemShipmentNowTimeOptionBinding
import com.tokopedia.logisticcart.databinding.ShippingNowWidgetBinding
import com.tokopedia.logisticcart.shipping.model.ShippingNowTimeOptionModel
import com.tokopedia.logisticcart.R

class ShippingNowWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ShippingNowWidgetBinding? = null
    private var currentSelectedRadioButton: CompoundButton? = null

    init {
        binding = ShippingNowWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        bind(getDummy())
    }

    fun bind(shippingNowTimeOptionModels: List<ShippingNowTimeOptionModel>) {
        removeTimeOptionViews()
        renderTimeOptionViews(shippingNowTimeOptionModels)
    }

    private fun removeTimeOptionViews() {
        binding?.shipmentTimeOptionView?.removeAllViews()
    }

    private fun renderTimeOptionViews(shippingNowTimeOptionModels: List<ShippingNowTimeOptionModel>) {
        shippingNowTimeOptionModels.forEach { shippingNowTimeOption ->
            val timeOptionBinding =
                ItemShipmentNowTimeOptionBinding.inflate(LayoutInflater.from(context))

            timeOptionBinding.apply {
                setRadioButton(shippingNowTimeOption.isSelected)
                setTitle(shippingNowTimeOption.title)
                setDescription(shippingNowTimeOption.description)
                setWarning(shippingNowTimeOption.warning, shippingNowTimeOption.isError)
                showRightIcon(shippingNowTimeOption.isDefaultNowShipment.not() && shippingNowTimeOption.isEnable)
                setTimeOptionEnable(shippingNowTimeOption.isEnable)
//                showCoachMark(shippingNowTimeOption.isShowCoachMark)
            }

            binding?.shipmentTimeOptionView?.addView(timeOptionBinding.root)
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.showCoachMark(isShow: Boolean) {
        if (isShow) {
            rightIcon.apply {
                val coachMarkItem = ArrayList<CoachMark2Item>()
                val coachMark = CoachMark2(context).apply {
                    simpleCloseIcon?.gone()
                    stepButtonTextLastChild = "Oke"
                    stepPrev?.visible()
                }
                coachMarkItem.add(
                    CoachMark2Item(
                        this,
                        context.getString(R.string.title_coachmark_option_schedule_delivery),
                        context.getString(R.string.description_coachmark_option_schedule_delivery),
                        CoachMark2.POSITION_BOTTOM
                    )
                )
                coachMark.showCoachMark(coachMarkItem)
            }
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setTimeOptionEnable(isEnable: Boolean) {
        if (isEnable) {
            rbShipment.isEnabled = true
            tvTitleShipment.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            )
            tvDescriptionShipment.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N600)
            )
            tvWarningShipment.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N600)
            )
        } else {
            rbShipment.isEnabled = false
            val disableTextColor =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400)

            tvTitleShipment.setTextColor(disableTextColor)
            tvDescriptionShipment.setTextColor(disableTextColor)
            tvWarningShipment.setTextColor(disableTextColor)
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setRadioButton(isSelected: Boolean) {
        rbShipment.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                currentSelectedRadioButton?.isChecked = false
                currentSelectedRadioButton = compoundButton
            }
        }
        rbShipment.isChecked = isSelected
    }

    private fun ItemShipmentNowTimeOptionBinding.setTitle(title: String) {
        tvTitleShipment.text = title
    }

    private fun ItemShipmentNowTimeOptionBinding.setDescription(subtitle: String?) {
        if (subtitle?.isNotBlank() == true) {
            tvDescriptionShipment.visible()
            tvDescriptionShipment.text = subtitle
        } else {
            tvDescriptionShipment.gone()
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setWarning(
        description: String?,
        isError: Boolean
    ) {
        if (description?.isNotBlank() == true) {
            tvWarningShipment.visible()
            tvWarningShipment.text = description

            if (isError) {
                tvWarningShipment.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
            } else {
                tvWarningShipment.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
            }
        } else {
            tvWarningShipment.gone()
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.showRightIcon(isShow: Boolean) {
        if (isShow) {
            rightIcon.visible()
        } else {
            rightIcon.gone()
        }
    }

    private fun getDummy(): List<ShippingNowTimeOptionModel> {
        return arrayListOf(
            ShippingNowTimeOptionModel(
                isEnable = true,
                title = "Tiba dalam 2 jam (Rp0 Rp48.000)",
                isDefaultNowShipment = true,
                isSelected = true
            ),
            ShippingNowTimeOptionModel(
                isEnable = true,
                title = "Jadwal lainnya (Rp20.000)",
                description = "Tiba hari ini, 16:00 - 18:00",
                isDefaultNowShipment = false,
                isSelected = false,
                isShowCoachMark = true
            )
        )
    }
}
