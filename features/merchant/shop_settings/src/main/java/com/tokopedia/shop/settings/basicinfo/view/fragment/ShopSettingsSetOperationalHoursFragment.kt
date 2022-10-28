package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionItemUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSetOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.databinding.FragmentShopSettingsSetOperationalHoursBinding
import com.tokopedia.shop.settings.databinding.ItemShopSetOpsHourAccordionBinding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.GregorianCalendar
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by Rafli Syam on 29/04/2021
 */
class ShopSettingsSetOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {
        @IdRes
        val START_TIME_TEXTFIELD_ID = R.id.text_field_start_time_ops_hour
        @IdRes
        val END_TIME_TEXTFIELD_ID = R.id.text_field_end_time_ops_hour
        @IdRes
        val ALL_DAY_OPTION_ID = R.id.option_all_day
        @IdRes
        val HOLIDAY_CAN_ATC_OPTION_ID = R.id.option_holiday
        @IdRes
        val HOLIDAY_CANNOT_ATC_OPTION_ID = R.id.option_holiday_cannot_atc
        @IdRes
        val CHOOSE_TIME_OPTION_ID = R.id.option_choose

        const val EXTRA_IS_NEED_TO_OPEN_CALENDAR_KEY = "open_calendar"
        const val EXTRA_SET_OPS_HOUR_RESPONSE_KEY = "set_ops_hour_result"
        private const val DEFAULT_FIRST_INDEX = 0
        private const val MIN_OPEN_HOUR = 0
        private const val MIN_OPEN_MINUTE = 0
        private const val MAX_CLOSE_HOUR = 23
        private const val ADDITIONAL_MINUTE_FOR_ENDTIME = 5
        private const val TWO_DIGIT_TIME_THRESHOLD = 10
        private const val MAX_CLOSE_MINUTE = 59
        private const val DEFAULT_OPERATIONAL_HOURS_RANGE = 9
        private const val DEFAULT_TIME_PICKER_MINUTE_INTERVAL = 5
        private const val MAX_OPEN_MINUTE_BY_TIME_PICKER = 50
        private const val MAX_CLOSE_MINUTE_BY_TIME_PICKER = 55
        private const val WEBVIEW_APPLINK_FORMAT = "%s?url=%s"
        private const val ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_FOR_MONDAY = 255f
        private const val ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_BESIDE_MONDAY = 220f
        private const val ACCORDION_ITEM_VIEW_24_HOURS_HEIGHT = 150f
        private const val ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CAN_ATC_HEIGHT = 210f
        private const val ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CANNOT_ATC_HEIGHT = 190f

        @JvmStatic
        fun createInstance(): ShopSettingsSetOperationalHoursFragment = ShopSettingsSetOperationalHoursFragment()

    }

    @Inject
    lateinit var shopSetOperationalHoursViewModel: ShopSetOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentShopSettingsSetOperationalHoursBinding>()
    private var opsHourListContainer: ScrollView? = null
    private var headerSetOpsHour: HeaderUnify? = null
    private var opsHourAccordion: AccordionUnify? = null
    private var loader: LoaderUnify? = null
    private var startTimePicker: DateTimePickerUnify? = null
    private var endTimePicker: DateTimePickerUnify? = null
    private var opsHourFooter: CardView? = null
    private var opsHourSaveButton: UnifyButton? = null
    private var currentSetShopOperationalHourList: MutableList<ShopOperationalHour> = mutableListOf()
    private var currentExpandedAccordionPosition = 0
    private var currentSelectedStartTime = "0"
    private var currentSelectedEndTime = "0"
    private var isOperationalHourDataChanged = false
    private var isSellerSetHolidayForAWeek = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShopSettingsSetOperationalHoursBinding.inflate(inflater, container, false).apply {
            initView(this)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundColor()
        initListener()
        observeLiveData()
        getShopOperationalHoursList()
    }

    override fun getComponent(): ShopSettingsComponent? = activity?.run {
        DaggerShopSettingsComponent
                .builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getScreenName(): String = ShopSettingsSetOperationalHoursFragment::class.java.simpleName

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onFragmentBackPressed(): Boolean {
        handleBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(shopSetOperationalHoursViewModel.setShopOperationalHoursData)
        removeObservers(shopSetOperationalHoursViewModel.shopOperationalHoursListData)
    }

    private fun initView(binding: FragmentShopSettingsSetOperationalHoursBinding?) {
        binding?.apply {
            opsHourListContainer = opsHourListContentView
            headerSetOpsHour = headerShopSetOperationalHours
            opsHourAccordion = shopOpsHourListAccordion
            loader = opsHourListLoader
            opsHourFooter = cvSaveOpsHourContainer
            opsHourSaveButton = btnOpsHourSave
        }
    }

    private fun initListener() {
        headerSetOpsHour?.apply {
            // set listener for "Reset" header action
            actionTextView?.isEnabled = false
            actionTextView?.setOnClickListener {
                clearAccordion()
                getShopOperationalHoursList()
            }

            setNavigationOnClickListener {
                handleBackPressed()
            }
            isShowShadow = false
        }

        opsHourSaveButton?.setOnClickListener {
            isSellerSetHolidayForAWeek = currentSetShopOperationalHourList.all {
                (it.startTime == OperationalHoursUtil.MIN_START_TIME) && (it.endTime == OperationalHoursUtil.MIN_START_TIME)
            }
            if (isSellerSetHolidayForAWeek) {
                // restrict set holiday for a whole week
                showConfirmDialog(
                        title = getString(R.string.shop_operational_hour_holiday_warning_dialog_title),
                        description = getString(R.string.shop_operational_hour_holiday_warning_dialog_desc),
                        primaryCtaText = getString(R.string.shop_operational_hour_label_set_holiday_sch),
                        secondaryCtaText = getString(R.string.shop_operational_hour_label_back),
                        imageUrl = getString(R.string.shop_operational_hour_ops_hour_holiday_illustration),
                        dialogAction = DialogUnify.VERTICAL_ACTION,
                        imageType = DialogUnify.WITH_ILLUSTRATION,
                        primaryCtaClickListener = {
                            activity?.setResult(Activity.RESULT_CANCELED, Intent().putExtra(EXTRA_IS_NEED_TO_OPEN_CALENDAR_KEY, true))
                            activity?.finish()
                        }
                )
            } else {
                showConfirmDialog(
                    title = getString(R.string.shop_operational_hour_dialog_title),
                    description = getString(R.string.shop_operational_hour_dialog_description),
                    primaryCtaText = getString(R.string.label_save),
                    secondaryCtaText = getString(R.string.shop_operational_hour_label_back),
                    primaryCtaClickListener = {
                        // update to new shop operational hours list
                        updateShopOperationalHoursList()
                        opsHourAccordion?.collapseAllGroup()
                        showLoader()
                    }
                )
            }
        }
    }

    private fun handleBackPressed() {
        if (isOperationalHourDataChanged) {
            showConfirmDialog(
                    title = getString(R.string.shop_operational_hour_dialog_data_change_title),
                    description = getString(R.string.shop_operational_hour_dialog_data_change_description),
                    primaryCtaText = getString(R.string.action_delete),
                    secondaryCtaText = getString(R.string.shop_operational_hour_label_back),
                    primaryCtaClickListener = {
                        activity?.onBackPressed()
                        activity?.finish()
                    }
            )
        } else {
            activity?.finish()
        }
    }

    private fun setBackgroundColor() = activity?.run {
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
    }

    private fun observeLiveData() {
        // get shop operational hours list
        observe(shopSetOperationalHoursViewModel.shopOperationalHoursListData) { result ->
            if (result is Success) {
                val operationalHourList = result.data.getShopOperationalHoursList?.data
                operationalHourList?.let { list ->
                    if (list.isNotEmpty()) {
                        setupAccordion(list)
                    } else {
                        setupAccordion(OperationalHoursUtil.generateDefaultOpsHourList())
                    }
                }
                hideLoader()
            }
            if (result is Fail) {
                hideLoader()
                showToaster(ErrorHandler.getErrorMessage(context, result.throwable), Toaster.TYPE_ERROR)
            }
        }

        // set new shop operational hours list
        observe(shopSetOperationalHoursViewModel.setShopOperationalHoursData) { result ->
            if (result is Success) {
                val updateShopOperationalHoursListResult = result.data
                if (updateShopOperationalHoursListResult.success) {
                    val toasterSuccessMessage = updateShopOperationalHoursListResult.message.takeIf {
                        it.isNotEmpty()
                    } ?: getString(R.string.shop_operational_success_update_operational_hours)
                    activity?.intent?.putExtra(EXTRA_SET_OPS_HOUR_RESPONSE_KEY, toasterSuccessMessage)
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                } else {
                    hideLoader()
                    val toasterErrorMessage = updateShopOperationalHoursListResult.message.takeIf {
                        it.isNotEmpty()
                    } ?: getString(R.string.shop_operational_failed_update_operational_hours)
                    showToaster(toasterErrorMessage, Toaster.TYPE_ERROR)
                    opsHourAccordion?.collapseAllGroup()
                }
            }
            if (result is Fail) {
                hideLoader()
                showToaster(getString(R.string.shop_operational_failed_update_operational_hours), Toaster.TYPE_ERROR)
            }
        }
    }

    private fun updateShopOperationalHoursList() {
        // update new shop operational hours list
        shopSetOperationalHoursViewModel.updateOperationalHoursList(
                userSession.shopId,
                currentSetShopOperationalHourList
        )
    }

    private fun getShopOperationalHoursList() {
        showLoader()
        shopSetOperationalHoursViewModel.getOperationalHoursList(userSession.shopId)
    }

    private fun clearAccordion() {
        while (opsHourAccordion?.accordionData?.isNotEmpty().orFalse()) {
            opsHourAccordion?.removeGroup(DEFAULT_FIRST_INDEX)
        }
    }

    private fun setupAccordion(operationalHoursList: List<ShopOperationalHour>?) {
        operationalHoursList?.let { hourList ->
            if (hourList.isNotEmpty()) {
                currentSetShopOperationalHourList.clear()
                currentSetShopOperationalHourList.addAll(hourList)
                opsHourAccordion?.type = AccordionUnify.TYPE_OR
                hourList.forEachIndexed { index, opsHour ->
                    run {
                        currentExpandedAccordionPosition = index
                        val accordionOpsHourItem = AccordionDataUnify(
                                title = OperationalHoursUtil.getDayName(opsHour.day),
                                subtitle = OperationalHoursUtil.generateDatetime(
                                        opsHour.startTime,
                                        opsHour.endTime,
                                        opsHour.status
                                ),
                                expandableView = generateAccordionChildView(opsHour),
                                isExpanded = index == DEFAULT_FIRST_INDEX
                        )
                        if (index == hourList.lastIndex) {
                            accordionOpsHourItem.setBorder(borderTop = false, borderBottom = true)
                        }
                        if (index == DEFAULT_FIRST_INDEX) {
                            currentSelectedStartTime = opsHour.startTime
                            currentSelectedEndTime = opsHour.endTime
                        }

                        context?.apply {
                            opsHourAccordion?.addGroup(accordionOpsHourItem)?.accordionSubtitle?.setTextColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600))
                        }
                    }
                }
                setAllAccordionsItemViewCustomHeight()
                opsHourAccordion?.onItemClick = { position, isExpanded ->
                    if (isExpanded) {
                        currentSelectedStartTime = currentSetShopOperationalHourList[position].startTime
                        currentSelectedEndTime = currentSetShopOperationalHourList[position].endTime
                        currentExpandedAccordionPosition = position
                    }
                }
                currentExpandedAccordionPosition = DEFAULT_FIRST_INDEX
            }
        }
    }

    private fun setAllAccordionsItemViewCustomHeight() {
        opsHourAccordion?.accordionData?.forEachIndexed { index, accordionDataUnify ->
            val contentViewHeight = when (OperationalHoursUtil.generateDatetime(
                    currentSetShopOperationalHourList[index].startTime,
                    currentSetShopOperationalHourList[index].endTime,
                    currentSetShopOperationalHourList[index].status
            )) {
                OperationalHoursUtil.ALL_DAY -> ACCORDION_ITEM_VIEW_24_HOURS_HEIGHT
                OperationalHoursUtil.HOLIDAY_CAN_ATC -> ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CAN_ATC_HEIGHT
                OperationalHoursUtil.HOLIDAY_CANNOT_ATC -> ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CANNOT_ATC_HEIGHT
                else -> {
                    if (index == DEFAULT_FIRST_INDEX)
                        ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_FOR_MONDAY
                    else
                        ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_BESIDE_MONDAY
                }
            }
            accordionDataUnify.apply {
                val viewContext = expandableView.context
                val accordionItemViewLayoutParams = expandableView.layoutParams
                val density = viewContext?.resources?.displayMetrics?.density.orZero()
                accordionItemViewLayoutParams?.height = (density * contentViewHeight).roundToInt()
                expandableView.layoutParams = accordionItemViewLayoutParams
            }
        }
    }

    private fun setItemAccordionViewCustomHeightByPosition(position: Int, customHeight: Float) {
        // update child view
        (opsHourAccordion?.getChildAt(position) as AccordionItemUnify).accordionContent.apply {
            val viewContext = this.context
            val accordionItemViewLayoutParams = this.layoutParams
            val density = viewContext?.resources?.displayMetrics?.density.orZero()
            accordionItemViewLayoutParams?.height = (density * customHeight).roundToInt()
            this.layoutParams = accordionItemViewLayoutParams
        }

        // update accordion expandable view data
        opsHourAccordion?.accordionData?.get(position)?.apply {
            val viewContext = expandableView.context
            val accordionItemViewLayoutParams = expandableView.layoutParams
            val density = viewContext?.resources?.displayMetrics?.density.orZero()
            accordionItemViewLayoutParams?.height = (density * customHeight).roundToInt()
            expandableView.layoutParams = accordionItemViewLayoutParams
        }
    }

    private fun generateAccordionChildView(opsHour: ShopOperationalHour): View {
        return ItemShopSetOpsHourAccordionBinding.inflate(LayoutInflater.from(context)).apply {
            val optionsGroup = opsHourOptions
            val allDayRadioButton = optionAllDay
            val holidayRadioButton = optionHoliday
            val holidayCannotAtcRadioButton = optionHolidayCannotAtc
            val chooseRadioButton = optionChoose
            val startTimeTextField = textFieldStartTimeOpsHour
            val endTimeTextField = textFieldEndTimeOpsHour
            val holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer
            val holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
            val icCopyToAll = icCopyToAllDay
            val tvCopyToAll = tvCopyToAllDay
            val tvCanAtcDescription = tvCanAtcDescription
            val tvCannotAtcDescription = tvCannotAtcDescription

            setupTimeTextField(startTimeTextField, endTimeTextField, opsHour)
            setupApplyToAllButton(icCopyToAll, tvCopyToAll)
            tvCanAtcDescription.apply {
                text = OperationalHoursUtil.getClickableSpanText(
                    fulltext = getString(R.string.shop_operational_hour_holiday_can_atc_description),
                    keyword = getString(R.string.shop_operational_hour_read_tnc_label),
                    clickableSpan = object : ClickableSpan() {
                        override fun onClick(textView: View) {
                            // go to seller education page
                            RouteManager.route(context, String.format(
                                WEBVIEW_APPLINK_FORMAT,
                                ApplinkConst.WEBVIEW,
                                getString(R.string.shop_operational_hour_desc_ticker_holiday_url)
                            ))
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.isUnderlineText = false
                            ds.typeface = Typeface.DEFAULT_BOLD
                            ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                        }
                    }
                )
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
            }

            tvCannotAtcDescription.apply {
                text = OperationalHoursUtil.getClickableSpanText(
                    fulltext = getString(R.string.shop_operational_hour_holiday_cannot_atc_description),
                    keyword = getString(R.string.shop_operational_hour_read_tnc_label),
                    clickableSpan = object : ClickableSpan() {
                        override fun onClick(textView: View) {
                            // go to seller education page
                            RouteManager.route(context, String.format(
                                WEBVIEW_APPLINK_FORMAT,
                                ApplinkConst.WEBVIEW,
                                getString(R.string.shop_operational_hour_desc_ticker_holiday_url)
                            ))
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.isUnderlineText = false
                            ds.typeface = Typeface.DEFAULT_BOLD
                            ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                        }
                    }
                )
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
            }

            when (OperationalHoursUtil.generateDatetime(opsHour.startTime, opsHour.endTime, opsHour.status)) {
                OperationalHoursUtil.ALL_DAY -> allDayRadioButton.isChecked = true
                OperationalHoursUtil.HOLIDAY_CAN_ATC -> holidayRadioButton.isChecked = true
                OperationalHoursUtil.HOLIDAY_CANNOT_ATC -> holidayCannotAtcRadioButton.isChecked = true
                else -> chooseRadioButton.isChecked = true
            }

            renderAccordionContent(
                opsHourDay = opsHour.day,
                isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                isShowHolidayCanAtcDescription = holidayRadioButton.isChecked,
                isShowHolidayCannotAtcDescription = holidayCannotAtcRadioButton.isChecked,
                startTimeTextField = startTimeTextField,
                endTimeTextField = endTimeTextField,
                icCopyToAll = icCopyToAll,
                tvCopyToAll = tvCopyToAll,
                holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer,
                holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
            )

            // set on checked listener radio button
            optionsGroup.setOnCheckedChangeListener { _, checkedId ->
                isOperationalHourDataChanged = true
                changeResetButtonState()
                when (checkedId) {
                    ALL_DAY_OPTION_ID -> {
                        // set time for selected day to 24 hours
                        renderAccordionContent(
                            opsHourDay = currentSetShopOperationalHourList[currentExpandedAccordionPosition].day,
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayCanAtcDescription = holidayRadioButton.isChecked,
                            isShowHolidayCannotAtcDescription = holidayCannotAtcRadioButton.isChecked,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            icCopyToAll = icCopyToAll,
                            tvCopyToAll = tvCopyToAll,
                            holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer,
                            holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
                        )
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime = OperationalHoursUtil.MAX_END_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].status = OperationalHoursUtil.CAN_ATC_STATUS
                        setItemAccordionViewCustomHeightByPosition(currentExpandedAccordionPosition, ACCORDION_ITEM_VIEW_24_HOURS_HEIGHT)
                    }

                    HOLIDAY_CAN_ATC_OPTION_ID -> {
                        // set close time for selected day
                        renderAccordionContent(
                            opsHourDay = currentSetShopOperationalHourList[currentExpandedAccordionPosition].day,
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayCanAtcDescription = holidayRadioButton.isChecked,
                            isShowHolidayCannotAtcDescription = holidayCannotAtcRadioButton.isChecked,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            icCopyToAll = icCopyToAll,
                            tvCopyToAll = tvCopyToAll,
                            holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer,
                            holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
                        )
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].status = OperationalHoursUtil.CAN_ATC_STATUS
                        setItemAccordionViewCustomHeightByPosition(currentExpandedAccordionPosition, ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CAN_ATC_HEIGHT)
                    }

                    HOLIDAY_CANNOT_ATC_OPTION_ID -> {
                        // set close time for selected day
                        renderAccordionContent(
                            opsHourDay = currentSetShopOperationalHourList[currentExpandedAccordionPosition].day,
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayCanAtcDescription = holidayRadioButton.isChecked,
                            isShowHolidayCannotAtcDescription = holidayCannotAtcRadioButton.isChecked,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            icCopyToAll = icCopyToAll,
                            tvCopyToAll = tvCopyToAll,
                            holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer,
                            holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
                        )
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].status = OperationalHoursUtil.CANNOT_ATC_STATUS
                        setItemAccordionViewCustomHeightByPosition(currentExpandedAccordionPosition, ACCORDION_ITEM_VIEW_WEEKLY_HOLIDAY_CANNOT_ATC_HEIGHT)
                    }

                    CHOOSE_TIME_OPTION_ID -> {
                        // show textField to choose open & close time
                        resetTimeTextField()
                        renderAccordionContent(
                            opsHourDay = currentSetShopOperationalHourList[currentExpandedAccordionPosition].day,
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayCanAtcDescription = holidayRadioButton.isChecked,
                            isShowHolidayCannotAtcDescription = holidayCannotAtcRadioButton.isChecked,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            icCopyToAll = icCopyToAll,
                            tvCopyToAll = tvCopyToAll,
                            holidayCanAtcDescriptionContainer = holidayCanAtcDescriptionContainer,
                            holidayCannotAtcDescriptionContainer = holidayCannotAtcDescriptionContainer
                        )
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].status = OperationalHoursUtil.CAN_ATC_STATUS
                        val contentHeight = if (currentExpandedAccordionPosition == DEFAULT_FIRST_INDEX) {
                            ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_FOR_MONDAY
                        } else {
                            ACCORDION_ITEM_VIEW_CUSTOM_HOURS_HEIGHT_BESIDE_MONDAY
                        }
                        setItemAccordionViewCustomHeightByPosition(currentExpandedAccordionPosition, contentHeight)
                    }
                }
                updateAccordionDescriptionByPosition(
                    newDescription = OperationalHoursUtil.generateDatetime(
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime,
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime,
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].status,
                    ),
                    position = currentExpandedAccordionPosition
                )
            }
        }.root
    }

    private fun getAccordionChildViewByPosition(position: Int): View? {
        return opsHourAccordion?.accordionData?.get(position)?.expandableView
    }

    private fun updateAccordionDescriptionByPosition(newDescription: String, position: Int) {
        opsHourAccordion?.apply {
            (getChildAt(position) as AccordionItemUnify).accordionSubtitle.text = newDescription
            accordionData[position].subtitle = newDescription
        }
    }

    private fun renderAccordionContent(
            opsHourDay: Int,
            isChooseTimeRadioButtonChecked: Boolean,
            isShowHolidayCanAtcDescription: Boolean,
            isShowHolidayCannotAtcDescription: Boolean,
            startTimeTextField: TextFieldUnify2?,
            endTimeTextField: TextFieldUnify2?,
            icCopyToAll: IconUnify?,
            tvCopyToAll: Typography?,
            holidayCanAtcDescriptionContainer: ConstraintLayout?,
            holidayCannotAtcDescriptionContainer: ConstraintLayout?
    ) {
        shouldShowTimeTextField(opsHourDay, startTimeTextField, endTimeTextField, icCopyToAll, tvCopyToAll, isChooseTimeRadioButtonChecked)
        shouldShowHolidayCanAtcContainer(holidayCanAtcDescriptionContainer, isShowHolidayCanAtcDescription)
        shouldShowHolidayCannotAtcContainer(holidayCannotAtcDescriptionContainer, isShowHolidayCannotAtcDescription)
    }

    private fun setupTimeTextField(startTimeTextField: TextFieldUnify2?, endTimeTextField: TextFieldUnify2?, opsHour: ShopOperationalHour) {
        // set startTime textField
        startTimeTextField?.textInputLayout?.editText?.apply {
            inputType = InputType.TYPE_NULL
            setText(OperationalHoursUtil.formatDateTimeWithDefaultTimezone(opsHour.startTime))
            setOnClickListener { setupStartTimePicker(opsHour.startTime) }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && visibility == View.VISIBLE) {
                    setupStartTimePicker(opsHour.startTime)
                }
            }
        }

        // setup endTime textField
        endTimeTextField?.textInputLayout?.editText?.apply {
            inputType = InputType.TYPE_NULL
            setText(OperationalHoursUtil.formatDateTimeWithDefaultTimezone(opsHour.endTime))
            setOnClickListener { setupEndTimePicker(opsHour.startTime, opsHour.endTime) }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && visibility == View.VISIBLE) {
                    setupEndTimePicker(opsHour.startTime, opsHour.endTime)
                }
            }
        }
    }

    private fun setupApplyToAllButton(icCopy: IconUnify?, tvCopyToAll: Typography?) {
        icCopy?.setOnClickListener { applyHoursToAllDay() }
        tvCopyToAll?.setOnClickListener { applyHoursToAllDay() }
    }

    private fun applyHoursToAllDay() {
        // get monday startTime and endTime then apply it for whole week
        val selectedStartTime = currentSetShopOperationalHourList[DEFAULT_FIRST_INDEX].startTime
        val selectedEndTime = currentSetShopOperationalHourList[DEFAULT_FIRST_INDEX].endTime
        val copiedOpsHourList = mutableListOf<ShopOperationalHour>()
        var totalChangedData = 0
        currentSetShopOperationalHourList.forEach {
            if (!(it.startTime == selectedStartTime && it.endTime == selectedEndTime)) {
                totalChangedData++
            }
            it.startTime = selectedStartTime
            it.endTime = selectedEndTime
            it.status = OperationalHoursUtil.CAN_ATC_STATUS
            copiedOpsHourList.add(it)
        }
        if (totalChangedData.isMoreThanZero()) {
            isOperationalHourDataChanged = true
            changeResetButtonState()
        }
        clearAccordion()
        setupAccordion(copiedOpsHourList)
        showToaster(message = getString(R.string.shop_operational_copy_to_all_day_text_description), type = Toaster.TYPE_NORMAL)
    }

    private fun resetTimeTextField() {
        val accordionItemView = getAccordionChildViewByPosition(currentExpandedAccordionPosition)
        setNewStartTimeInfo(accordionItemView, OperationalHoursUtil.DEFAULT_OPEN_HOUR_STRING, OperationalHoursUtil.DEFAULT_MINUTE_STRING)
        setNewEndTimeInfo(accordionItemView, OperationalHoursUtil.DEFAULT_CLOSE_HOUR_STRING, OperationalHoursUtil.DEFAULT_MINUTE_STRING)
        updateStartTimeByPosition(currentExpandedAccordionPosition, OperationalHoursUtil.DEFAULT_OPEN_HOUR_STRING, OperationalHoursUtil.DEFAULT_MINUTE_STRING)
        updateEndTimeByPosition(currentExpandedAccordionPosition, OperationalHoursUtil.DEFAULT_CLOSE_HOUR_STRING, OperationalHoursUtil.DEFAULT_MINUTE_STRING)
    }

    private fun shouldShowHolidayCanAtcContainer(canAtcDescriptionContainer: ConstraintLayout?, isShow: Boolean) {
        canAtcDescriptionContainer?.showWithCondition(isShow)
    }

    private fun shouldShowHolidayCannotAtcContainer(cannotAtcDescriptionContainer: ConstraintLayout?, isShow: Boolean) {
        cannotAtcDescriptionContainer?.showWithCondition(isShow)
    }

    private fun shouldShowTimeTextField(
            opsHourDay: Int,
            startTimeTextField: TextFieldUnify2?,
            endTimeTextField: TextFieldUnify2?,
            icCopyToAll: IconUnify?,
            tvCopyToAll: Typography?,
            isChooseRadioButtonChecked: Boolean
    ) {
        // only show "Terapkan ke semua hari" if selected day is Monday
        val isMonday = opsHourDay == (DEFAULT_FIRST_INDEX + 1)
        icCopyToAll?.showWithCondition(isChooseRadioButtonChecked && isMonday)
        tvCopyToAll?.showWithCondition(isChooseRadioButtonChecked && isMonday)
        startTimeTextField?.showWithCondition(isChooseRadioButtonChecked)
        endTimeTextField?.showWithCondition(isChooseRadioButtonChecked)
    }

    private fun setupEndTimePicker(currentStartTime: String, currentEndTime: String) {
        context?.let { ctx ->

            val startTimeHour = OperationalHoursUtil.getHourFromFormattedTime(currentStartTime).toIntOrZero()
            val startTimeMinute = OperationalHoursUtil.getMinuteFromFormattedTime(currentStartTime).toIntOrZero()
            val endTimeHour = OperationalHoursUtil.getHourFromFormattedTime(currentEndTime).toIntOrZero()
            val endTimeMinute = OperationalHoursUtil.getMinuteFromFormattedTime(currentEndTime).toIntOrZero()

            // create instance
            endTimePicker = DateTimePickerUnify(
                    context = ctx,
                    minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        // set minimum end time +1 hour from start time
                        set(Calendar.HOUR_OF_DAY, startTimeHour)
                        set(Calendar.MINUTE, startTimeMinute + ADDITIONAL_MINUTE_FOR_ENDTIME)
                    },
                    defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        // set default selected end time same with minimum
                        set(Calendar.HOUR_OF_DAY, endTimeHour)
                        set(Calendar.MINUTE, endTimeMinute)
                    },
                    maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MAX_CLOSE_HOUR)
                        set(Calendar.MINUTE, MAX_CLOSE_MINUTE)
                    },
                    type = DateTimePickerUnify.TYPE_TIMEPICKER
            )

            // set time picker minute interval
            endTimePicker?.minuteInterval = DEFAULT_TIME_PICKER_MINUTE_INTERVAL

            // set button wording and listener
            endTimePicker?.datePickerButton?.text = getString(R.string.label_choose)
            endTimePicker?.datePickerButton?.setOnClickListener {
                val selectedHour = endTimePicker?.timePicker?.hourPicker?.activeValue
                val selectedMinute = endTimePicker?.timePicker?.minutePicker?.activeValue
                val selectedChildView = getAccordionChildViewByPosition(currentExpandedAccordionPosition)

                // set new endTime info to selected textField
                setNewEndTimeInfo(selectedChildView, selectedHour, selectedMinute)

                // set new updated endTime for server
                updateEndTimeByPosition(currentExpandedAccordionPosition, selectedHour, selectedMinute)

                endTimePicker?.dismiss()
            }

            fragmentManager?.let {
                endTimePicker?.setTitle(getString(R.string.shop_operational_hour_choose_close_shop))
                endTimePicker?.setInfo(getString(R.string.shop_operational_hour_endtime_info))
                endTimePicker?.show(it, screenName)
            }

        }
    }

    private fun setupStartTimePicker(currentStartTime: String) {
        context?.let { ctx ->

            val startTimeHour = OperationalHoursUtil.getHourFromFormattedTime(currentStartTime).toIntOrZero()
            val startTimeMinute = OperationalHoursUtil.getMinuteFromFormattedTime(currentStartTime).toIntOrZero()

            // create instance
            startTimePicker = DateTimePickerUnify(
                    context = ctx,
                    minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MIN_OPEN_HOUR)
                        set(Calendar.MINUTE, MIN_OPEN_MINUTE)
                    },
                    defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        // set default selected end time same with minimum
                        set(Calendar.HOUR_OF_DAY, startTimeHour)
                        set(Calendar.MINUTE, startTimeMinute)
                    },
                    maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MAX_CLOSE_HOUR)
                        set(Calendar.MINUTE, MAX_OPEN_MINUTE_BY_TIME_PICKER)
                    },
                    type = DateTimePickerUnify.TYPE_TIMEPICKER
            )

            // set time picker minute interval
            startTimePicker?.minuteInterval = DEFAULT_TIME_PICKER_MINUTE_INTERVAL

            // set button wording and listener
            startTimePicker?.datePickerButton?.text = getString(R.string.label_choose)
            startTimePicker?.datePickerButton?.setOnClickListener {
                val selectedHour = startTimePicker?.timePicker?.hourPicker?.activeValue
                var selectedMinute = startTimePicker?.timePicker?.minutePicker?.activeValue
                val selectedChildView = getAccordionChildViewByPosition(currentExpandedAccordionPosition)

                // set new startTime info to selected textField
                setNewStartTimeInfo(selectedChildView, selectedHour, selectedMinute)

                // set new updated startTime for selected day
                updateStartTimeByPosition(currentExpandedAccordionPosition, selectedHour, selectedMinute)

                // update endTime to +9 hour after, to avoid endTime < startTime
                val nineHourAfterSelectedStartTime = selectedHour.toIntOrZero() + DEFAULT_OPERATIONAL_HOURS_RANGE
                val autoSelectedEndTime = if (nineHourAfterSelectedStartTime < MAX_CLOSE_HOUR) {
                    nineHourAfterSelectedStartTime
                } else {
                    MAX_CLOSE_HOUR
                }

                val autoSelectedEndTimeString = if (nineHourAfterSelectedStartTime < TWO_DIGIT_TIME_THRESHOLD) {
                    "0${autoSelectedEndTime}"
                } else {
                    autoSelectedEndTime.toString()
                }

                selectedMinute = if (nineHourAfterSelectedStartTime > MAX_CLOSE_HOUR) {
                    MAX_CLOSE_MINUTE_BY_TIME_PICKER.toString()
                } else {
                    selectedMinute
                }

                setNewEndTimeInfo(selectedChildView, autoSelectedEndTimeString, selectedMinute)
                updateEndTimeByPosition(currentExpandedAccordionPosition, autoSelectedEndTimeString, selectedMinute)

                startTimePicker?.dismiss()
            }

            fragmentManager?.let {
                startTimePicker?.setTitle(getString(R.string.shop_operational_hour_choose_open_shop))
                startTimePicker?.show(it, screenName)
            }
            
        }
    }

    private fun setNewStartTimeInfo(accordionItemView: View?, newHour: String?, newMinute: String?) = accordionItemView?.run {
        // set new time to startTime textField
        findViewById<TextFieldUnify2>(START_TIME_TEXTFIELD_ID)?.textInputLayout?.editText?.apply {
            setText(getString(
                    R.string.shop_operational_hour_format_with_timezone,
                    newHour,
                    newMinute
            ))
        }
    }

    private fun setNewEndTimeInfo(accordionItemView: View?, newHour: String?, newMinute: String?) = accordionItemView?.run {
        // set new time to endTime textField
        findViewById<TextFieldUnify2>(END_TIME_TEXTFIELD_ID)?.textInputLayout?.editText?.apply {
            setText(getString(
                    R.string.shop_operational_hour_format_with_timezone,
                    newHour,
                    newMinute
            ))
        }
    }

    private fun updateStartTimeByPosition(position: Int, selectedHour: String?, selectedMinutes: String?) {
        // update new startTime for selected day
        currentSelectedStartTime = OperationalHoursUtil.generateServerDateTimeFormat(selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero())
        isOperationalHourDataChanged = currentSelectedStartTime != currentSetShopOperationalHourList[position].startTime
        currentSetShopOperationalHourList[position].startTime = currentSelectedStartTime
        changeResetButtonState()
        updateAccordionDescriptionByPosition(
                newDescription = OperationalHoursUtil.generateDatetime(
                        currentSetShopOperationalHourList[position].startTime,
                        currentSetShopOperationalHourList[position].endTime,
                        currentSetShopOperationalHourList[position].status
                ),
                position = position
        )
    }

    private fun updateEndTimeByPosition(position: Int, selectedHour: String?, selectedMinutes: String?) {
        // update new startTime for selected day
        currentSelectedEndTime = OperationalHoursUtil.generateServerDateTimeFormat(selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero())
        isOperationalHourDataChanged = currentSelectedEndTime != currentSetShopOperationalHourList[position].endTime
        currentSetShopOperationalHourList[position].endTime = currentSelectedEndTime
        changeResetButtonState()
        updateAccordionDescriptionByPosition(
                newDescription = OperationalHoursUtil.generateDatetime(
                        currentSetShopOperationalHourList[position].startTime,
                        currentSetShopOperationalHourList[position].endTime,
                        currentSetShopOperationalHourList[position].status
                ),
                position = position
        )
    }

    private fun changeResetButtonState() {
        headerSetOpsHour?.actionTextView?.isEnabled = isOperationalHourDataChanged
        opsHourFooter?.showWithCondition(isOperationalHourDataChanged)
    }

    private fun showConfirmDialog(
            title: String,
            description: String,
            primaryCtaText: String,
            secondaryCtaText: String,
            imageUrl: String = "",
            dialogAction: Int = DialogUnify.HORIZONTAL_ACTION,
            imageType: Int = DialogUnify.NO_IMAGE,
            primaryCtaClickListener: () -> Unit
    ) {
        context?.let { ctx ->
            DialogUnify(ctx, dialogAction, imageType).apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCtaText)
                setSecondaryCTAText(secondaryCtaText)
                setPrimaryCTAClickListener {
                    primaryCtaClickListener.invoke()
                    dismiss()
                }
                setImageUrl(imageUrl)
                setSecondaryCTAClickListener { dismiss() }
                show()
            }

        }
    }

    private fun showToaster(message: String, type: Int, toasterLength: Int = Toaster.LENGTH_SHORT) {
        Toaster.build(requireView(), message, toasterLength, type, getString(R.string.label_ok)).show()
    }

    private fun showLoader() {
        loader?.show()
        opsHourListContainer?.hide()
        opsHourFooter?.hide()
        headerSetOpsHour?.actionTextView?.isEnabled = false
    }

    private fun hideLoader() {
        loader?.hide()
        opsHourListContainer?.show()
    }
}
