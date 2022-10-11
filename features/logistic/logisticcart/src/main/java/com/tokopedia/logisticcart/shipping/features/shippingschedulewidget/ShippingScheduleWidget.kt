package com.tokopedia.logisticcart.shipping.features.shippingschedulewidget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.Notice
import com.tokopedia.logisticcart.databinding.ItemShipmentNowTimeOptionBinding
import com.tokopedia.logisticcart.databinding.ShippingNowWidgetBinding
import com.tokopedia.logisticcart.shipping.model.ShippingScheduleWidgetModel
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.schedule_slot.bottomsheet.ScheduleSlotBottomSheet
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.unifyprinciples.Typography

class ShippingScheduleWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ShippingNowWidgetBinding? = null
    private var mListener: ShippingScheduleWidgetListener? = null

    interface ShippingScheduleWidgetListener {
        fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel)
        fun getFragmentManager(): FragmentManager?
    }

    init {
        binding = ShippingNowWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(
        titleNow2H: CharSequence?,
        descriptionNow2H: String?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?,
        listener: ShippingScheduleWidgetListener
    ) {
        mListener = listener
        removeScheduleWidgetViews()
        renderScheduleWidgetViews(
            createWidget(
                titleNow2H = titleNow2H,
                descriptionNow2H = descriptionNow2H,
                labelNow2H = labelNow2H,
                scheduleDeliveryUiModel = scheduleDeliveryUiModel
            )
        )
    }

    private fun createWidget(
        titleNow2H: CharSequence?,
        descriptionNow2H: String?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?
    ): List<ShippingScheduleWidgetModel> {
        val shippingScheduleWidgets: ArrayList<ShippingScheduleWidgetModel> = arrayListOf()

        shippingScheduleWidgets.add(
            ShippingScheduleWidgetModel(
                isEnable = true,
                title = titleNow2H,
                description = descriptionNow2H,
                titleWeightType = Typography.BOLD,
                label = labelNow2H,
                isNowTwoHours = true,
                isSelected = scheduleDeliveryUiModel?.isSelected != true,
                onSelectedWidgetListener = {
                    scheduleDeliveryUiModel?.apply {
                        isSelected = false
                        mListener?.onChangeScheduleDelivery(this)
                    }
                }
            )
        )

        scheduleDeliveryUiModel?.apply {
            val onClickIconListener: (() -> Unit)? = if (available) {
                {
                    openScheduleDeliveryBottomSheet(scheduleDeliveryUiModel)
                }
            } else null

            shippingScheduleWidgets.add(
                ShippingScheduleWidgetModel(
                    isEnable = available,
                    title = "$title (${deliveryProduct?.textFinalPrice} ${deliveryProduct?.textRealPrice})",
                    titleWeightType = Typography.BOLD,
                    description = deliveryProduct?.textEta,
                    label = text,
                    isNowTwoHours = false,
                    isSelected = isSelected,
                    isShowCoachMark = available && isNeedShowCoachMark,
                    onSelectedWidgetListener = {
                        scheduleDeliveryUiModel.isSelected = true
                        mListener?.onChangeScheduleDelivery(scheduleDeliveryUiModel)
                    },
                    onClickIconListener = onClickIconListener
                )
            )

            isNeedShowCoachMark = false
        }

        return shippingScheduleWidgets
    }

    private fun openScheduleDeliveryBottomSheet(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        // open schedule delivery bottom sheet
        scheduleDeliveryUiModel?.let {
            val bottomsheetUiModel = ScheduleDeliveryMapper.mapResponseToUiModel(
                it.deliveryServices,
                it.scheduleDate,
                it.deliveryProduct ?: DeliveryProduct(),
                it.notice
            )
            mListener?.getFragmentManager()?.let {
                fragmentManager ->  ScheduleSlotBottomSheet.show(fragmentManager, bottomsheetUiModel)
            }
        }
    }

    private fun removeScheduleWidgetViews() {
        binding?.shipmentTimeOptionView?.removeAllViews()
    }

    private fun renderScheduleWidgetViews(shippingNowTimeOptionModels: List<ShippingScheduleWidgetModel>) {
        shippingNowTimeOptionModels.forEach { shippingNowTimeOption ->
            val timeOptionBinding =
                ItemShipmentNowTimeOptionBinding.inflate(LayoutInflater.from(context))

            timeOptionBinding.apply {
                setRadioButton(
                    shippingNowTimeOption.isSelected,
                    shippingNowTimeOption.onSelectedWidgetListener
                )
                setTitle(shippingNowTimeOption.title ?: "", shippingNowTimeOption.titleWeightType)
                setDescription(shippingNowTimeOption.description)
                setLabel(shippingNowTimeOption.label)
                showRightIcon(shippingNowTimeOption.isNowTwoHours.not() && shippingNowTimeOption.isEnable)
                showRightIcon(shippingNowTimeOption.onClickIconListener != null)
                setRightIconAction(shippingNowTimeOption.onClickIconListener)
                setTimeOptionEnable(shippingNowTimeOption.isEnable)
                showCoachMark(shippingNowTimeOption.isShowCoachMark)
            }

            binding?.shipmentTimeOptionView?.addView(timeOptionBinding.root)
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setRightIconAction(onClickIconListener: (() -> Unit)?) {
        if (onClickIconListener != null) {
            rightIcon.setOnClickListener {
                onClickIconListener()
            }
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.showCoachMark(isShow: Boolean) {
        if (isShow) {
            rightIcon.apply {
                val coachMarkItem = ArrayList<CoachMark2Item>()
                val coachMark = CoachMark2(context).apply {
                    simpleCloseIcon?.gone()
                    isOutsideTouchable = true
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
            tvLabelShipment.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N600)
            )
        } else {
            rbShipment.isEnabled = false
            val disableTextColor =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400)

            tvTitleShipment.setTextColor(disableTextColor)
            tvDescriptionShipment.setTextColor(disableTextColor)
            tvLabelShipment.setTextColor(disableTextColor)
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setRadioButton(
        isSelected: Boolean,
        onSelectedWidgetListener: (() -> Unit)?
    ) {
        rbShipment.isChecked = isSelected
        rbShipment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSelectedWidgetListener?.invoke()
            }
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setTitle(
        title: CharSequence,
        titleWeightType: Int
    ) {
        tvTitleShipment.weightType = titleWeightType
//        tvTitleShipment.setTypeface(null, Typeface.BOLD)
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

    private fun ItemShipmentNowTimeOptionBinding.setLabel(
        label: CharSequence?
    ) {
        if (label?.isNotBlank() == true) {
            tvLabelShipment.apply {
                visible()
                text = label
            }
            tvLabelShipment.visible()
            tvLabelShipment.text = label
        } else {
            tvLabelShipment.gone()
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.showRightIcon(isShow: Boolean) {
        if (isShow) {
            rightIcon.visible()
        } else {
            rightIcon.gone()
        }
    }
}
