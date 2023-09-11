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
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendHtmlBoldText
import com.tokopedia.logisticcart.databinding.ItemShipmentNowRevampScheduledOptionBinding
import com.tokopedia.logisticcart.databinding.ShippingNowRevampWidgetBinding
import com.tokopedia.logisticcart.scheduledelivery.analytics.ScheduleDeliveryAnalytics
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryBottomSheetMapper
import com.tokopedia.logisticcart.scheduledelivery.preference.ScheduleDeliveryPreferences
import com.tokopedia.logisticcart.scheduledelivery.view.bottomsheet.ScheduleSlotBottomSheet
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingScheduleWidgetModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShippingScheduleRevampWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ShippingNowRevampWidgetBinding? = null
    private var mListener: ShippingScheduleWidgetListener? = null
    private var scheduleDeliveryPreferences: ScheduleDeliveryPreferences? = null
    private var coachMarkHandler: Handler? = null
    private var coachMarkRunnable: Runnable? = null
    private var delayChangeRadioButton: Job? = null
    private var delayShimmerLabel: Job? = null
    private var scheduleSlotBottomSheet: ScheduleSlotBottomSheet? = null

    interface ShippingScheduleWidgetListener {
        fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel)
        fun getFragmentManager(): FragmentManager?
    }

    init {
        binding = ShippingNowRevampWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        scheduleDeliveryPreferences = ScheduleDeliveryPreferences(context)
    }

    fun bind(
        titleNow2H: CharSequence?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?,
        listener: ShippingScheduleWidgetListener
    ) {
        mListener = listener
        val scheduleUiModel = createWidget(
            titleNow2H = titleNow2H,
            labelNow2H = labelNow2H,
            scheduleDeliveryUiModel = scheduleDeliveryUiModel
        )
        removeScheduleWidgetViews()
        renderScheduleWidgetViews(scheduleUiModel)
        renderLabelWidget(scheduleUiModel)
    }

    private fun renderLabelWidget(scheduleUiModel: List<ShippingScheduleWidgetModel>) {
        binding?.run {
            val selectedShipment = scheduleUiModel.find { model -> model.isSelected }
            if (selectedShipment != null) {
                if (!selectedShipment.label.isNullOrEmpty()) {
                    delayShimmerLabel?.cancel()
                    delayShimmerLabel = GlobalScope.launch(Dispatchers.Main) {
                        delay(DELAY_CHANGE_LABEL)
                        shimmerChangeSchedule.gone()
                        tvLabelSchedule.text = selectedShipment.label
                        tvLabelSchedule.visible()
                    }
                    groupLabelScheduledDelivery.visible()
                    tvLabelSchedule.gone()
                    shimmerChangeSchedule.visible()
                } else {
                    shimmerChangeSchedule.gone()
                    groupLabelScheduledDelivery.gone()
                }
            } else {
                shimmerChangeSchedule.gone()
                groupLabelScheduledDelivery.gone()
            }
        }
    }

    private fun createWidget(
        titleNow2H: CharSequence?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?
    ): List<ShippingScheduleWidgetModel> {
        val shippingScheduleWidgets: ArrayList<ShippingScheduleWidgetModel> = arrayListOf()

        scheduleDeliveryUiModel?.apply {
            shippingScheduleWidgets.add(createOtherOptionWidget())
        }

        shippingScheduleWidgets.add(
            create2HWidget(
                titleNow2H = titleNow2H,
                labelNow2H = labelNow2H,
                scheduleDeliveryUiModel = scheduleDeliveryUiModel
            )
        )

        return shippingScheduleWidgets
    }

    private fun create2HWidget(
        titleNow2H: CharSequence?,
        labelNow2H: CharSequence?,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?
    ): ShippingScheduleWidgetModel {
        val onClick = {
            scheduleDeliveryUiModel?.apply {
                isSelected = false
                mListener?.onChangeScheduleDelivery(this)
                ScheduleDeliveryAnalytics.sendClickJamTibaRadioButtonOnTokopediaNowEvent()
            }
        }
        return ShippingScheduleWidgetModel(
            isEnable = true,
            title = titleNow2H,
            label = labelNow2H,
            isSelected = scheduleDeliveryUiModel?.isSelected != true,
            onSelectedWidgetListener = { onClick() },
            onClickIconListener = { onClick() }
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
            label = deliveryProduct.promoText.convertToSpannedString(),
            isSelected = isSelected,
            isShowCoachMark = scheduleDeliveryPreferences?.isDisplayedCoachmark?.not() ?: true,
            onSelectedWidgetListener = {
                isSelected = true
                mListener?.onChangeScheduleDelivery(this)
                ScheduleDeliveryAnalytics.sendChooseScheduledDeliveryOptionRadioButtonOnTokopediaNowEvent()
            },
            onClickIconListener = onClickIconListener,
            showOtherScheduleButton = available
        )
    }

    private fun ScheduleDeliveryUiModel.getTitleOtherOption(): CharSequence {
        val textTitle = StringBuilder().apply {
            appendHtmlBoldText(title)
            if (available) {
                append(deliveryProduct.getFormattedPrice())
            }
        }.toString()

        return textTitle.convertToSpannedString()
    }

    private fun String.convertToSpannedString(): CharSequence {
        return HtmlLinkHelper(
            context,
            this
        ).spannedString ?: ""
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
        binding?.dividerScheduledDelivery?.gone()
        binding?.groupLabelScheduledDelivery?.gone()
    }

    private fun renderScheduleWidgetViews(shippingNowTimeOptionModels: List<ShippingScheduleWidgetModel>) {
        shippingNowTimeOptionModels.forEach { shippingNowTimeOption ->
            val scheduledDeliveryOptionBinding =
                ItemShipmentNowRevampScheduledOptionBinding.inflate(LayoutInflater.from(context))

            scheduledDeliveryOptionBinding.apply {
                setRadioButton(
                    shippingNowTimeOption.isSelected,
                    shippingNowTimeOption.onSelectedWidgetListener
                )
                setOnViewShipmentTextClickListener(
                    shippingNowTimeOption.onClickIconListener
                )
                setOtherScheduleText(shippingNowTimeOption.showOtherScheduleButton)
                setTitle(shippingNowTimeOption.title ?: "")
                setDescription(shippingNowTimeOption.description?.convertToSpannedString())
                setTimeOptionEnable(shippingNowTimeOption.isEnable)
                showCoachMark(shippingNowTimeOption.isShowCoachMark)
            }
            binding?.shipmentTimeOptionView?.addView(scheduledDeliveryOptionBinding.root)
        }
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.setOnViewShipmentTextClickListener(
        onClickIconListener: (() -> Unit)?
    ) {
        tvTitleShipment.setOnClickDebounceListener { onClickIconListener?.invoke() }
        tvDescriptionShipment.setOnClickDebounceListener { onClickIconListener?.invoke() }
        tvOtherSchedule.setOnClickDebounceListener { onClickIconListener?.invoke() }
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.showCoachMark(isShow: Boolean) {
        if (isShow) {
            coachMarkRunnable = Runnable {
                tvOtherSchedule.apply {
                    scheduleDeliveryPreferences?.isDisplayedCoachmark = true
                    val coachMarkItem = ArrayList<CoachMark2Item>()
                    val coachMark = CoachMark2(context)
                    coachMarkItem.add(
                        CoachMark2Item(
                            this,
                            context.getString(logisticcartR.string.title_coachmark_option_schedule_delivery),
                            context.getString(logisticcartR.string.description_coachmark_option_schedule_delivery),
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

    private fun ItemShipmentNowRevampScheduledOptionBinding.setTimeOptionEnable(isEnable: Boolean) {
        if (isEnable) {
            rbShipment.isEnabled = true
            tvTitleShipment.setTextColor(
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN950
                )
            )
            tvDescriptionShipment.setTextColor(
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN950
                )
            )
        } else {
            rbShipment.isEnabled = false
            val disableTextColor =
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN400
                )

            tvTitleShipment.setTextColor(disableTextColor)
            tvDescriptionShipment.setTextColor(disableTextColor)
        }
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.setRadioButton(
        isSelected: Boolean,
        onSelectedWidgetListener: (() -> Unit)?
    ) {
        rbShipment.isChecked = isSelected
        rbShipment.skipAnimation()
        rbShipment.setOnCheckedChangeListener { _, isChecked ->
            delayChangeRadioButton?.cancel()
            delayChangeRadioButton = GlobalScope.launch(Dispatchers.Main) {
                delay(DEBOUNCE_TIME_SCHEDULE_RADIO_BUTTON)
                if (isChecked) {
                    onSelectedWidgetListener?.invoke()
                }
            }
        }
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.setTitle(title: CharSequence) {
        tvTitleShipment.text = title
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.setDescription(subtitle: CharSequence?) {
        if (subtitle?.isNotBlank() == true) {
            tvDescriptionShipment.visible()
            tvDescriptionShipment.text = subtitle
        } else {
            tvDescriptionShipment.gone()
        }
    }

    private fun ItemShipmentNowRevampScheduledOptionBinding.setOtherScheduleText(showOtherScheduleButton: Boolean) {
        if (showOtherScheduleButton) {
            tvOtherSchedule.visible()
        } else {
            tvOtherSchedule.gone()
        }
    }

    private fun removeCoachmarkHandler() {
        coachMarkRunnable?.apply {
            coachMarkHandler?.removeCallbacks(this)
        }
    }

    private fun removeDelayRadioButton() {
        delayChangeRadioButton?.cancel()
    }

    private fun removeDelayChangeLabel() {
        delayShimmerLabel?.cancel()
    }

    override fun onDetachedFromWindow() {
        removeCoachmarkHandler()
        removeDelayRadioButton()
        removeDelayChangeLabel()
        super.onDetachedFromWindow()
    }

    companion object {
        private const val DELAY_SHOWING_COACHMARK: Long = 500
        private const val DELAY_CHANGE_LABEL: Long = 200
        private const val DEBOUNCE_TIME_SCHEDULE_RADIO_BUTTON = 500L
    }
}
