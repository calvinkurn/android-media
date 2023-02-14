package com.tokopedia.logisticcart.scheduledelivery.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendHtmlBoldText
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.ItemShipmentNowTimeOptionBinding
import com.tokopedia.logisticcart.databinding.ShippingNowWidgetBinding
import com.tokopedia.logisticcart.scheduledelivery.analytics.ScheduleDeliveryAnalytics
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryBottomSheetMapper
import com.tokopedia.logisticcart.scheduledelivery.preference.ScheduleDeliveryPreferences
import com.tokopedia.logisticcart.scheduledelivery.view.bottomsheet.ScheduleSlotBottomSheet
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingScheduleWidgetModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

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
    private var scheduleDeliveryPreferences: ScheduleDeliveryPreferences? = null
    private var coachMarkHandler: Handler? = null
    private var coachMarkRunnable: Runnable? = null
    private var scheduleSlotBottomSheet: ScheduleSlotBottomSheet? = null

    interface ShippingScheduleWidgetListener {
        fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel)
        fun getFragmentManager(): FragmentManager?
    }

    init {
        binding = ShippingNowWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        scheduleDeliveryPreferences = ScheduleDeliveryPreferences(context)
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
            create2HWidget(
                titleNow2H = titleNow2H,
                descriptionNow2H = descriptionNow2H,
                labelNow2H = labelNow2H,
                scheduleDeliveryUiModel = scheduleDeliveryUiModel
            )
        )

        scheduleDeliveryUiModel?.apply {
            shippingScheduleWidgets.add(createOtherOptionWidget())
        }

        return shippingScheduleWidgets
    }

    private fun create2HWidget(
        titleNow2H: CharSequence?,
        descriptionNow2H: String?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?
    ): ShippingScheduleWidgetModel {
        return ShippingScheduleWidgetModel(
            isEnable = true,
            title = titleNow2H,
            description = descriptionNow2H,
            label = labelNow2H,
            isSelected = scheduleDeliveryUiModel?.isSelected != true,
            onSelectedWidgetListener = {
                scheduleDeliveryUiModel?.apply {
                    isSelected = false
                    mListener?.onChangeScheduleDelivery(this)
                    ScheduleDeliveryAnalytics.sendClickJamTibaRadioButtonOnTokopediaNowEvent()
                }
            }
        )
    }

    private fun ScheduleDeliveryUiModel.createOtherOptionWidget(): ShippingScheduleWidgetModel {
        val onClickIconListener: (() -> Unit)? = if (available) {
            {
                if (scheduleSlotBottomSheet == null) {
                    openScheduleDeliveryBottomSheet(this)
                    ScheduleDeliveryAnalytics.sendClickArrowInScheduledDeliveryOptionsOnTokopediaNowEvent()
                }
            }
        } else {
            null
        }

        return ShippingScheduleWidgetModel(
            isEnable = available,
            title = getTitleOtherOption(),
            description = if (available) deliveryProduct.textEta else text,
            label = deliveryProduct.promoText,
            isSelected = isSelected,
            isShowCoachMark = scheduleDeliveryPreferences?.isDisplayedCoachmark?.not() ?: true,
            onSelectedWidgetListener = {
                isSelected = true
                mListener?.onChangeScheduleDelivery(this)
                ScheduleDeliveryAnalytics.sendChooseScheduledDeliveryOptionRadioButtonOnTokopediaNowEvent()
            },
            onClickIconListener = onClickIconListener
        )
    }

    private fun ScheduleDeliveryUiModel.getTitleOtherOption(): CharSequence {
        val text = StringBuilder().apply {
            appendHtmlBoldText(title)
            if (available) {
                append(deliveryProduct.getFormattedPrice())
            }
        }.toString()

        return HtmlLinkHelper(context, text).spannedString ?: ""
    }

    private fun openScheduleDeliveryBottomSheet(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        scheduleDeliveryUiModel?.let {
            val bottomsheetUiModel = ScheduleDeliveryBottomSheetMapper.mapResponseToUiModel(
                it.deliveryServices,
                it.scheduleDate,
                it.deliveryProduct,
                it.notice
            )
            mListener?.getFragmentManager()?.let { fragmentManager ->

                scheduleSlotBottomSheet =
                    ScheduleSlotBottomSheet.show(fragmentManager, bottomsheetUiModel)
                scheduleSlotBottomSheet?.apply {
                    setListener(object :
                            ScheduleSlotBottomSheet.ScheduleSlotBottomSheetListener {
                            override fun onChooseTimeListener(timeId: Long, dateId: String) {
                                scheduleDeliveryUiModel.setScheduleDateAndTimeslotId(
                                    scheduleDate = dateId,
                                    timeslotId = timeId
                                )
                                mListener?.onChangeScheduleDelivery(it)
                            }
                        })
                    setOnDismissListener {
                        scheduleSlotBottomSheet = null
                    }
                }
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
                setOnViewShipmentTextClickListener(
                    shippingNowTimeOption.isSelected,
                    shippingNowTimeOption.isEnable,
                    shippingNowTimeOption.onSelectedWidgetListener
                )
                setTitle(shippingNowTimeOption.title ?: "")
                setDescription(shippingNowTimeOption.description)
                setLabel(shippingNowTimeOption.label, shippingNowTimeOption.isSelected)
                showRightIcon(shippingNowTimeOption.onClickIconListener)
                setTimeOptionEnable(shippingNowTimeOption.isEnable)
                showCoachMark(shippingNowTimeOption.isShowCoachMark)
            }

            binding?.shipmentTimeOptionView?.addView(timeOptionBinding.root)
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setOnViewShipmentTextClickListener(
        isSelected: Boolean,
        isEnable: Boolean,
        onSelectedWidgetListener: (() -> Unit)?
    ) {
        viewShipmentText.setOnClickListener {
            if (isEnable && !isSelected) {
                onSelectedWidgetListener?.invoke()
            }
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.showCoachMark(isShow: Boolean) {
        if (isShow) {
            coachMarkRunnable = Runnable {
                rightIcon.apply {
                    scheduleDeliveryPreferences?.isDisplayedCoachmark = true

                    val coachMarkItem = ArrayList<CoachMark2Item>()
                    val coachMark = CoachMark2(context)
                    coachMarkItem.add(
                        CoachMark2Item(
                            this,
                            context.getString(R.string.title_coachmark_option_schedule_delivery),
                            context.getString(R.string.description_coachmark_option_schedule_delivery),
                            CoachMark2.POSITION_BOTTOM
                        )
                    )
                    coachMark.isOutsideTouchable = false
                    coachMark.showCoachMark(coachMarkItem)
                }
            }

            coachMarkHandler = Handler()
            coachMarkRunnable?.apply {
                coachMarkHandler?.postDelayed(this, DELAY_SHOWING_COACHMARK)
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
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
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
        rbShipment.skipAnimation()
        rbShipment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSelectedWidgetListener?.invoke()
            }
        }
    }

    private fun ItemShipmentNowTimeOptionBinding.setTitle(title: CharSequence) {
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
        label: CharSequence?,
        showLabel: Boolean
    ) {
        if (label?.isNotBlank() == true && showLabel) {
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

    private fun ItemShipmentNowTimeOptionBinding.showRightIcon(onClickIconListener: (() -> Unit)?) {
        if (onClickIconListener != null) {
            rightIcon.visible()
            rightIcon.setOnClickListener {
                onClickIconListener.invoke()
            }
        } else {
            rightIcon.invisible()
        }
    }

    private fun removeCoachmarkHandler() {
        coachMarkRunnable?.apply {
            coachMarkHandler?.removeCallbacks(this)
        }
    }

    override fun onDetachedFromWindow() {
        removeCoachmarkHandler()
        super.onDetachedFromWindow()
    }

    companion object {
        private const val DELAY_SHOWING_COACHMARK: Long = 500
    }
}
