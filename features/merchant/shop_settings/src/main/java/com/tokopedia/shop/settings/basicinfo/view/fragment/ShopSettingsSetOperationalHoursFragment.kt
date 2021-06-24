package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSetOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Rafli Syam on 29/04/2021
 */
class ShopSettingsSetOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {

        @LayoutRes
        val FRAGMENT_LAYOUT = R.layout.fragment_shop_settings_set_operational_hours
        @LayoutRes
        val ACCORDION_CHILD_VIEW = R.layout.item_shop_set_ops_hour_accordion
        @IdRes
        val START_TIME_TEXTFIELD_ID = R.id.text_field_start_time_ops_hour
        @IdRes
        val END_TIME_TEXTFIELD_ID = R.id.text_field_end_time_ops_hour
        @IdRes
        val ALL_DAY_OPTION_ID = R.id.option_all_day
        @IdRes
        val HOLIDAY_OPTION_ID = R.id.option_holiday
        @IdRes
        val CHOOSE_TIME_OPTION_ID = R.id.option_choose

        private const val DEFAULT_FIRST_DAY_INDEX = 0
        private const val MIN_OPEN_HOUR = 0
        private const val MIN_OPEN_MINUTE = 0
        private const val MAX_CLOSE_HOUR = 23
        private const val MAX_CLOSE_MINUTE = 59
        private const val ALL_DAY_OPTION = 1
        private const val HOLIDAY_OPTION = 2
        private const val CHOOSE_HOUR_OPTION = 3

        @JvmStatic
        fun createInstance(): ShopSettingsSetOperationalHoursFragment = ShopSettingsSetOperationalHoursFragment()

    }

    @Inject
    lateinit var shopSetOperationalHoursViewModel: ShopSetOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var opsHourListContainer: ScrollView? = null
    private var headerSetOpsHour: HeaderUnify? = null
    private var opsHourAccordion: AccordionUnify? = null
    private var loader: LoaderUnify? = null
    private var holidayTicker: Ticker? = null
    private var startTimePicker: DateTimePickerUnify? = null
    private var endTimePicker: DateTimePickerUnify? = null
    private var currentSetShopOperationalHourList: MutableList<ShopOperationalHour> = mutableListOf()
    private var currentExpandedAccordionPosition = 0
    private var currentSelectedRadioOption = 0
    private var currentSelectedStartTime = "0"
    private var currentSelectedEndTime = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {
            initView(this)
        }
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

    private fun initView(view: View) = with(view) {
        opsHourListContainer = findViewById(R.id.ops_hour_list_content_view)
        headerSetOpsHour = findViewById(R.id.header_shop_set_operational_hours)
        opsHourAccordion = findViewById(R.id.shop_ops_hour_list_accordion)
        loader = findViewById(R.id.ops_hour_list_loader)
        holidayTicker = findViewById(R.id.ops_hour_holiday_ticker)
    }

    private fun initListener() {
        headerSetOpsHour?.apply {
            // set listener for "Simpan" header action
            actionTextView?.setOnClickListener {
                showConfirmDialog()
            }

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setBackgroundColor() = activity?.run {
        window.decorView.setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
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
        }

        // set new shop operational hours list
        observe(shopSetOperationalHoursViewModel.setShopOperationalHoursData) { result ->
            if (result is Success) {
                val updateShopOperationalHoursListResult = result.data
                if (updateShopOperationalHoursListResult.success) {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
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

    private fun setupAccordion(operationalHoursList: List<ShopOperationalHour>?) {
        operationalHoursList?.let { hourList ->
            if (hourList.isNotEmpty()) {
                currentSetShopOperationalHourList.addAll(hourList)
                opsHourAccordion?.type = AccordionUnify.TYPE_OR
                hourList.forEachIndexed { index, opsHour ->
                    run {
                        val accordionOpsHourItem = AccordionDataUnify(
                                title = OperationalHoursUtil.getDayName(opsHour.day),
                                subtitle = OperationalHoursUtil.generateDatetime(opsHour.startTime, opsHour.endTime),
                                expandableView = generateAccordionChildView(opsHour),
                                isExpanded = index == DEFAULT_FIRST_DAY_INDEX
                        )
                        if (index == hourList.lastIndex) {
                            accordionOpsHourItem.setBorder(borderTop = false, borderBottom = false)
                        }
                        opsHourAccordion?.addGroup(accordionOpsHourItem)
                    }
                }
                opsHourAccordion?.onItemClick = { position, isExpanded ->
                    if (isExpanded) {
                        currentExpandedAccordionPosition = position
                    }
                }
            }
        }
    }

    private fun generateAccordionChildView(opsHour: ShopOperationalHour): View {
        return View.inflate(context, ACCORDION_CHILD_VIEW, null).apply {

            val optionsGroup = findViewById<RadioGroup>(R.id.ops_hour_options)
            val allDayRadioButton = findViewById<RadioButtonUnify>(R.id.option_all_day)
            val holidayRadioButton = findViewById<RadioButtonUnify>(R.id.option_holiday)
            val chooseRadioButton = findViewById<RadioButtonUnify>(R.id.option_choose)
            val startTimeTextField = findViewById<TextFieldUnify>(START_TIME_TEXTFIELD_ID)
            val endTimeTextField = findViewById<TextFieldUnify>(END_TIME_TEXTFIELD_ID)
            val connector = findViewById<Typography>(R.id.ops_hour_connector)
            val icCopyToAllDay = findViewById<IconUnify>(R.id.ic_copy_hours)
            val tvCopyToAllDay = findViewById<Typography>(R.id.tv_copy_to_all_day)

            setupTimeTextField(startTimeTextField, endTimeTextField, opsHour)

            when (OperationalHoursUtil.generateDatetime(opsHour.startTime, opsHour.endTime)) {
                OperationalHoursUtil.ALL_DAY -> {
                    allDayRadioButton.isChecked = true
                    currentSelectedRadioOption = ALL_DAY_OPTION
                    renderAccordionContent(
                            isShowCopyOption = false, // hide copy option if 24 hours
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayTicker = false,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            connector = connector,
                            iconCopyToAll = icCopyToAllDay,
                            textCopyToAll = tvCopyToAllDay
                    )
                }
                OperationalHoursUtil.HOLIDAY -> {
                    holidayRadioButton.isChecked = true
                    currentSelectedRadioOption = HOLIDAY_OPTION
                    renderAccordionContent(
                            isShowCopyOption = false, // hide copy option if holiday option
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            isShowHolidayTicker = true,
                            startTimeTextField = startTimeTextField,
                            endTimeTextField = endTimeTextField,
                            connector = connector,
                            iconCopyToAll = icCopyToAllDay,
                            textCopyToAll = tvCopyToAllDay
                    )
                }
                else -> {
                    chooseRadioButton.isChecked = true
                    currentSelectedRadioOption = CHOOSE_HOUR_OPTION
                    renderAccordionContent(
                            isShowCopyOption = true, // show copy option if choose times
                            isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                            startTimeTextField = startTimeTextField,
                            isShowHolidayTicker = false,
                            endTimeTextField = endTimeTextField,
                            connector = connector,
                            iconCopyToAll = icCopyToAllDay,
                            textCopyToAll = tvCopyToAllDay
                    )
                }
            }

            // set on checked listener radio button
            optionsGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    ALL_DAY_OPTION_ID -> {
                        // set time for selected day to 24 hours
                        renderAccordionContent(
                                isShowCopyOption = false, // show copy option if choose times
                                isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                                isShowHolidayTicker = false,
                                startTimeTextField = startTimeTextField,
                                endTimeTextField = endTimeTextField,
                                connector = connector,
                                iconCopyToAll = icCopyToAllDay,
                                textCopyToAll = tvCopyToAllDay
                        )
                        currentSelectedRadioOption = ALL_DAY_OPTION
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime = OperationalHoursUtil.MAX_END_TIME
                    }

                    HOLIDAY_OPTION_ID -> {
                        // set close time for selected day
                        renderAccordionContent(
                                isShowCopyOption = false, // show copy option if choose times
                                isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                                isShowHolidayTicker = true,
                                startTimeTextField = startTimeTextField,
                                endTimeTextField = endTimeTextField,
                                connector = connector,
                                iconCopyToAll = icCopyToAllDay,
                                textCopyToAll = tvCopyToAllDay
                        )
                        currentSelectedRadioOption = HOLIDAY_OPTION
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].startTime = OperationalHoursUtil.MIN_START_TIME
                        currentSetShopOperationalHourList[currentExpandedAccordionPosition].endTime = OperationalHoursUtil.MIN_START_TIME
                    }

                    CHOOSE_TIME_OPTION_ID -> {
                        // show textField to choose open & close time
                        renderAccordionContent(
                                isShowCopyOption = true, // show copy option if choose times
                                isChooseTimeRadioButtonChecked = chooseRadioButton.isChecked,
                                isShowHolidayTicker = false,
                                startTimeTextField = startTimeTextField,
                                endTimeTextField = endTimeTextField,
                                connector = connector,
                                iconCopyToAll = icCopyToAllDay,
                                textCopyToAll = tvCopyToAllDay
                        )
                        currentSelectedRadioOption = CHOOSE_HOUR_OPTION
                    }
                }
            }
        }
    }

    private fun getAccordionChildViewByPosition(position: Int): View? {
        return opsHourAccordion?.accordionData?.get(position)?.expandableView
    }

    private fun renderAccordionContent(
            isShowCopyOption: Boolean,
            isChooseTimeRadioButtonChecked: Boolean,
            isShowHolidayTicker: Boolean,
            startTimeTextField: TextFieldUnify?,
            endTimeTextField: TextFieldUnify?,
            connector: Typography?,
            iconCopyToAll: IconUnify,
            textCopyToAll: Typography
    ) {
        shouldShowTimeTextField(startTimeTextField, endTimeTextField, connector, isChooseTimeRadioButtonChecked)
        shouldShowHolidayTicker(isShowHolidayTicker)
        shouldShowCopyToAllDayOptions(isShowCopyOption, iconCopyToAll, textCopyToAll)
    }

    private fun setupTimeTextField(startTimeTextField: TextFieldUnify?, endTimeTextField: TextFieldUnify?, opsHour: ShopOperationalHour) {
        // set startTime textField
        startTimeTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            setText(OperationalHoursUtil.formatDateTimeWithDefaultTimezone(opsHour.startTime))
            setOnClickListener { setupStartTimePicker() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && visibility == View.VISIBLE) {
                    setupStartTimePicker()
                }
            }
        }

        // setup endTime textField
        endTimeTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            setText(OperationalHoursUtil.formatDateTimeWithDefaultTimezone(opsHour.endTime))
            setOnClickListener { setupEndTimePicker() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && visibility == View.VISIBLE) {
                    setupEndTimePicker()
                }
            }
        }
    }

    private fun shouldShowHolidayTicker(isShow: Boolean) {
        holidayTicker?.showWithCondition(isShow)
    }

    private fun shouldShowCopyToAllDayOptions(
            isShow: Boolean,
            icon: IconUnify?,
            text: Typography?
    ) {
        if (isShow) {
            icon?.visible()
            text?.visible()
            icon?.setOnClickListener { applySelectedOpsHourToAllDay() }
            text?.setOnClickListener { applySelectedOpsHourToAllDay() }
        }
        else {
            icon?.invisible()
            text?.invisible()
        }
    }

    private fun applySelectedOpsHourToAllDay() {
        if (currentSelectedRadioOption == ALL_DAY_OPTION) {
            // apply 24 hour everyday
            currentSetShopOperationalHourList.forEach { opsHour ->
                opsHour.startTime = OperationalHoursUtil.MIN_START_TIME
                opsHour.endTime = OperationalHoursUtil.MAX_END_TIME
            }
        }
        else if (currentSelectedRadioOption == CHOOSE_HOUR_OPTION) {
            // apply selected time to everyday
            currentSetShopOperationalHourList.forEach { opsHour ->
                opsHour.startTime = currentSelectedStartTime
                opsHour.endTime = currentSelectedEndTime
            }
        }
        showToaster(getString(R.string.shop_operational_hour_copy_to_all_day_text_desc), Toaster.TYPE_NORMAL)
    }

    private fun shouldShowTimeTextField(
            startTimeTextField: TextFieldUnify?,
            endTimeTextField: TextFieldUnify?,
            connector: Typography?,
            isChooseRadioButtonChecked: Boolean
    ) {
        if (isChooseRadioButtonChecked) {
            startTimeTextField?.visible()
            endTimeTextField?.visible()
            connector?.visible()
        }
        else {
            startTimeTextField?.invisible()
            endTimeTextField?.invisible()
            connector?.invisible()
        }
    }

    private fun setupEndTimePicker() {
        context?.let { ctx ->

            // get selected start time from expanded child view
            val childView = getAccordionChildViewByPosition(currentExpandedAccordionPosition)
            val selectedHourFromTextField = OperationalHoursUtil.getHourFromFormattedTime(
                    childView?.findViewById<TextFieldUnify>(START_TIME_TEXTFIELD_ID)?.textFieldInput?.text.toString()
            ).toInt()

            // create instance
            endTimePicker = DateTimePickerUnify(
                    context = ctx,
                    minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        // set minimum end time +1 hour from start time
                        set(Calendar.HOUR_OF_DAY, selectedHourFromTextField)
                        set(Calendar.MINUTE, MIN_OPEN_MINUTE + 5)
                    },
                    defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        // set default selected end time same with minimum
                        set(Calendar.HOUR_OF_DAY, selectedHourFromTextField)
                        set(Calendar.MINUTE, MIN_OPEN_MINUTE + 5)
                    },
                    maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MAX_CLOSE_HOUR)
                        set(Calendar.MINUTE, MAX_CLOSE_MINUTE)
                    },
                    type = DateTimePickerUnify.TYPE_TIMEPICKER
            )

            // set time picker minute interval
            endTimePicker?.minuteInterval = 5

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

    private fun setupStartTimePicker() {
        context?.let { ctx ->

            // create instance
            startTimePicker = DateTimePickerUnify(
                    context = ctx,
                    minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MIN_OPEN_HOUR)
                        set(Calendar.MINUTE, MIN_OPEN_MINUTE)
                    },
                    defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)),
                    maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                        set(Calendar.HOUR_OF_DAY, MAX_CLOSE_HOUR)
                        set(Calendar.MINUTE, MAX_CLOSE_MINUTE)
                    },
                    type = DateTimePickerUnify.TYPE_TIMEPICKER
            )

            // set time picker minute interval
            startTimePicker?.minuteInterval = 5

            // set button wording and listener
            startTimePicker?.datePickerButton?.text = getString(R.string.label_choose)
            startTimePicker?.datePickerButton?.setOnClickListener {
                val selectedHour = startTimePicker?.timePicker?.hourPicker?.activeValue
                val selectedMinute = startTimePicker?.timePicker?.minutePicker?.activeValue
                val selectedChildView = getAccordionChildViewByPosition(currentExpandedAccordionPosition)

                // set new startTime info to selected textField
                setNewStartTimeInfo(selectedChildView, selectedHour, selectedMinute)

                // update endTime to +1 hour after, to avoid endTime < startTime
                val oneHourAheadAfterSelectedStartTime = (selectedHour.toIntOrZero() + 1).toString()
                setNewEndTimeInfo(selectedChildView, oneHourAheadAfterSelectedStartTime, selectedMinute)
                updateEndTimeByPosition(currentExpandedAccordionPosition, oneHourAheadAfterSelectedStartTime, selectedMinute)

                // set new updated startTime for selected day
                updateStartTimeByPosition(currentExpandedAccordionPosition, selectedHour, selectedMinute)

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
        findViewById<TextFieldUnify>(START_TIME_TEXTFIELD_ID)?.apply {
            textFieldInput.setText(getString(
                    R.string.shop_operational_hour_format_with_timezone,
                    newHour,
                    newMinute
            ))
        }
    }

    private fun setNewEndTimeInfo(accordionItemView: View?, newHour: String?, newMinute: String?) = accordionItemView?.run {
        // set new time to endTime textField
        findViewById<TextFieldUnify>(END_TIME_TEXTFIELD_ID)?.apply {
            textFieldInput.setText(getString(
                    R.string.shop_operational_hour_format_with_timezone,
                    newHour,
                    newMinute
            ))
        }
    }

    private fun updateStartTimeByPosition(position: Int, selectedHour: String?, selectedMinutes: String?) {
        // update new startTime for selected day
        currentSelectedStartTime = OperationalHoursUtil.generateServerDateTimeFormat(selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero())
        currentSetShopOperationalHourList[position].startTime = OperationalHoursUtil.generateServerDateTimeFormat(
                selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero()
        )
    }

    private fun updateEndTimeByPosition(position: Int, selectedHour: String?, selectedMinutes: String?) {
        // update new startTime for selected day
        currentSelectedEndTime = OperationalHoursUtil.generateServerDateTimeFormat(selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero())
        currentSetShopOperationalHourList[position].endTime = OperationalHoursUtil.generateServerDateTimeFormat(
                selectedHour.toIntOrZero(), selectedMinutes.toIntOrZero()
        )
    }

    private fun showConfirmDialog() {
        context?.let { ctx ->
            DialogUnify(ctx, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.shop_operational_hour_dialog_title))
                setDescription(getString(R.string.shop_operational_hour_dialog_description))
                setPrimaryCTAText(getString(R.string.label_save))
                setSecondaryCTAText(getString(R.string.label_cancel))
                setPrimaryCTAClickListener {
                    // update to new shop operational hours list
                    updateShopOperationalHoursList()
                    dismiss()
                    opsHourAccordion?.collapseAllGroup()
                    showLoader()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun showToaster(message: String, type: Int) {
        Toaster.build(requireView(), message, Toaster.LENGTH_SHORT, type, "Oke").show()
    }

    private fun showLoader() {
        loader?.show()
        opsHourListContainer?.hide()
        holidayTicker?.hide()
        headerSetOpsHour?.actionTextView?.isEnabled = false
    }

    private fun hideLoader() {
        loader?.hide()
        opsHourListContainer?.show()
        headerSetOpsHour?.actionTextView?.isEnabled = true
    }
}