package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.remoteconfig.ShopAbTestPlatform
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsSetOperationalHoursActivity
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopSettingsOperationalHoursListAdapter
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

/**
 * Created by Rafli Syam on 28/04/2021
 */
class ShopSettingsOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {

        @JvmStatic
        fun createInstance(): ShopSettingsOperationalHoursFragment = ShopSettingsOperationalHoursFragment()

        @LayoutRes
        val FRAGMENT_LAYOUT = R.layout.fragment_shop_settings_operational_hours

        @LayoutRes
        val HOLIDAY_BOTTOMSHEET_LAYOUT = R.layout.bottomsheet_shop_set_holiday

        @LayoutRes
        val ACTION_BOTTOMSHEET_LAYOUT = R.layout.bottomsheet_shop_edit_holiday

        private const val NO_HOLIDAY_DATE = "0"
        private const val REQUEST_CODE_SET_OPS_HOUR = 100
        private const val AB_TEST_EXPERIMENT_KEY_OPS_HOUR = "operational_hour"
    }

    @Inject
    lateinit var shopSettingsOperationalHoursViewModel: ShopSettingsOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private val todayDate = Calendar.getInstance(TimeZone.getDefault()).time
    private val tomorrowDate = Calendar.getInstance(TimeZone.getDefault()).apply { add(Calendar.DAY_OF_YEAR, 1) }.time
    private val defaultMaxDate = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time // next year

    private var headerOpsHour: HeaderUnify? = null
    private var icEditOpsHour: IconUnify? = null
    private var rvOpsHourList: RecyclerView? = null
    private var rvOpsHourListAdapter: ShopSettingsOperationalHoursListAdapter? = null
    private var holidayBottomSheet: BottomSheetUnify? = null
    private var actionBottomSheet: BottomSheetUnify? = null
    private var buttonAddHoliday: UnifyButton? = null
    private var loader: LoaderUnify? = null
    private var opsHourContainer: LinearLayout? = null
    private var holidayScheduleContainer: RelativeLayout? = null
    private var tvShopHolidaySchedule: Typography? = null
    private var autoChatTicker: Ticker? = null
    private var holidayEditActionButton: ImageUnify? = null
    private var startDateTextField: TextFieldUnify? = null
    private var endDateTextField: TextFieldUnify? = null
    private var calendarUnifyStart: UnifyCalendar? = null
    private var calendarUnifyEnd: UnifyCalendar? = null
    private var buttonSaveHolidaySchedule: UnifyButton? = null
    private var shopIsOnHolidayContainer: RelativeLayout? = null
    private var shopIsOnHolidayEndDateText: Typography? = null
    private var shopIsOnHolidaySwitcher: SwitchUnify? = null
    private var checkBoxCloseNow: CheckboxUnify? = null

    private var shopAbTestPlatform: ShopAbTestPlatform? = null
    private var isNeedToShowToaster: Boolean = false
    private var isShopOnScheduledHoliday: Boolean = false
    private var isShouldShowHolidaySchedule: Boolean = false
    private var isActionEdit: Boolean = false
    private var setShopHolidayScheduleStatusMessage: String = ""
    private var setShopHolidayScheduleStatusType: Int = 0
    private var startHolidayDateMilliseconds: Long = 0L
    private var endHolidayDateMilliseconds: Long = 0L
    private var selectedStartDate = tomorrowDate
    private var selectedEndDate = Date()
    private var existingStartDate = Date()
    private var existingEndDate = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAbTestRollenceVariant()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {
            initView(this)
            initRecyclerView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setBackgroundColor()
        initListener()
        observeLiveData()
        showLoader()
        getInitialData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SET_OPS_HOUR && resultCode == Activity.RESULT_OK) {
            showLoader()
            getInitialData()
        }
        else {
            isNeedToShowToaster = true
            setShopHolidayScheduleStatusMessage = getString(R.string.shop_operational_hour_set_holiday_schedule_failed)
            setShopHolidayScheduleStatusType = Toaster.TYPE_ERROR
        }
    }

    override fun getScreenName(): String {
        return ShopSettingsOperationalHoursFragment::class.java.simpleName
    }

    override fun getComponent(): ShopSettingsComponent? {
        return activity?.run {
            DaggerShopSettingsComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun checkAbTestRollenceVariant() {
        shopAbTestPlatform = ShopAbTestPlatform(requireContext()).apply {
            requestParams = ShopAbTestPlatform.createRequestParam(
                    listExperimentName = listOf(AB_TEST_EXPERIMENT_KEY_OPS_HOUR),
                    shopId = userSession.shopId
            )
            fetch(object : RemoteConfig.Listener {
                override fun onComplete(remoteConfig: RemoteConfig?) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val variantType = getString(AB_TEST_EXPERIMENT_KEY_OPS_HOUR, "")
                        if (variantType.isEmpty() || variantType != AB_TEST_EXPERIMENT_KEY_OPS_HOUR) {
                            // redirect to old shop ops hour settings
                            RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
                            activity?.finish()
                        }
                    }
                }

                override fun onError(e: Exception?) {}
            })
        }
    }

    private fun initView(view: View?) {
        headerOpsHour = view?.findViewById(R.id.header_shop_operational_hours)
        icEditOpsHour = view?.findViewById(R.id.ic_edit_ops_hour)
        rvOpsHourList = view?.findViewById(R.id.rv_ops_hour_list)
        buttonAddHoliday = view?.findViewById(R.id.btn_add_holiday_schedule)
        loader = view?.findViewById(R.id.ops_hour_loader)
        opsHourContainer = view?.findViewById(R.id.ops_hour_parent_container)
        holidayScheduleContainer = view?.findViewById(R.id.shop_holiday_schedule_container)
        tvShopHolidaySchedule = view?.findViewById(R.id.tv_shop_holiday_schedule)
        autoChatTicker = view?.findViewById(R.id.ops_hour_chat_auto_ticker)
        holidayEditActionButton = view?.findViewById(R.id.ops_hour_img_schedule_action)
        shopIsOnHolidayContainer = view?.findViewById(R.id.holiday_toggle_container)
        shopIsOnHolidayEndDateText = view?.findViewById(R.id.tv_holiday_end)
        shopIsOnHolidaySwitcher = view?.findViewById(R.id.open_shop_switch)
    }

    private fun getHolidayDatePickerBottomSheetView(): View {
        return View.inflate(context, HOLIDAY_BOTTOMSHEET_LAYOUT, null).apply {
            calendarUnifyStart = findViewById(R.id.ops_hour_holiday_calendar_start)
            calendarUnifyEnd = findViewById(R.id.ops_hour_holiday_calendar_end)
            startDateTextField = findViewById(R.id.text_field_start_date_holiday)
            endDateTextField = findViewById(R.id.text_field_end_date_holiday)
            buttonSaveHolidaySchedule = findViewById(R.id.btn_save_holiday_schedule)
            checkBoxCloseNow = findViewById(R.id.checkbox_close_now)

            // setup text field start date
            setupStartDateTextFieldBottomSheet()

            // setup text field end date
            setupEndDateTextFieldBottomSheet()

            // setup checkbox close now
            setupCloseNowCheckboxBottomSheet()

            // set listener for save schedule button
            buttonSaveHolidaySchedule?.setOnClickListener {
                showConfirmDialogForSaveHolidaySchedule(selectedStartDate, selectedEndDate)
            }
        }
    }

    private fun getActionBottomSheetView(): View {
        return View.inflate(context, ACTION_BOTTOMSHEET_LAYOUT, null).apply {
            val actionEdit = findViewById<LinearLayout>(R.id.action_edit_holiday)
            val actionDelete = findViewById<LinearLayout>(R.id.action_delete_holiday)

            // set action edit schedule click listener
            actionEdit.setOnClickListener {
                isActionEdit = true
                resetSelectedDates(isActionEdit)
                setupEditHolidayCalendarPickerBottomSheet()
                showHolidayBottomSheet()
            }

            // set action delete schedule click listener
            actionDelete.setOnClickListener {
                showConfirmDialogForDeleteHolidaySchedule()
            }
        }
    }

    private fun initRecyclerView() {
        rvOpsHourListAdapter = ShopSettingsOperationalHoursListAdapter()
        rvOpsHourList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = rvOpsHourListAdapter
        }
    }

    private fun initListener() {
        // set click listener for icon edit ops hour
        icEditOpsHour?.setOnClickListener {
            startActivityForResult(
                    Intent(context, ShopSettingsSetOperationalHoursActivity::class.java),
                    REQUEST_CODE_SET_OPS_HOUR
            )
        }

        // set click listener for button add holiday schedule
        buttonAddHoliday?.setOnClickListener {
            isActionEdit = false
            resetSelectedDates(isActionEdit)
            setupHolidayCalendarPickerBottomSheet()
            showHolidayBottomSheet()
        }

        // set click listener holiday edit action button
        holidayEditActionButton?.setOnClickListener {
            setupActionBottomSheet()
            showActionBottomSheet()
        }
    }

    private fun setupToolbar() {
        headerOpsHour?.apply {
            addRightIcon(R.drawable.ic_ops_hour_help)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setBackgroundColor() {
        activity?.run {
            window.decorView.setBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        }
    }

    private fun observeLiveData() {

        // observe get shop operational hours list data
        observe(shopSettingsOperationalHoursViewModel.shopSettingsOperationalHoursListUiModel) { result ->
            if (result is Success) {
                hideLoader()
                val opsHourListUiModel = result.data
                val holidayInfo = opsHourListUiModel.closeInfo

                // should show upcoming holiday section if available
                isShouldShowHolidaySchedule = holidayInfo.closeDetail.startDate != NO_HOLIDAY_DATE && holidayInfo.closeDetail.endDate != NO_HOLIDAY_DATE
                if (isShouldShowHolidaySchedule) {
                    startHolidayDateMilliseconds = holidayInfo.closeDetail.startDate.toLongOrZero() * 1000L
                    endHolidayDateMilliseconds = holidayInfo.closeDetail.endDate.toLongOrZero() * 1000L
                    existingStartDate = Date(startHolidayDateMilliseconds)
                    existingEndDate = Date(endHolidayDateMilliseconds)
                    selectedStartDate = existingStartDate
                    selectedEndDate = existingEndDate
                }

                // holiday schedule is on going
                isShopOnScheduledHoliday = holidayInfo.closeDetail.status == ShopStatusDef.CLOSED

                // render UI for holiday section
                renderHolidaySection()

                // update UI for operational hours list section
                val opsHourList = opsHourListUiModel.operationalHourList
                rvOpsHourListAdapter?.updateOpsHourList(opsHourList)

                // show toaster if refresh after set shop holiday schedule
                if (isNeedToShowToaster) {
                    showToaster(setShopHolidayScheduleStatusMessage, setShopHolidayScheduleStatusType)
                    isNeedToShowToaster = false
                }
            }
        }

        // observe set shop close info
        observe(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule) { result ->
            if (result is Success) {
                setShopHolidayScheduleStatusMessage = getString(R.string.shop_operational_hour_set_holiday_schedule_success)
                setShopHolidayScheduleStatusType = Toaster.TYPE_NORMAL
            }
            if (result is Fail) {
                setShopHolidayScheduleStatusMessage = getString(R.string.shop_operational_hour_set_holiday_schedule_failed)
                setShopHolidayScheduleStatusType = Toaster.TYPE_ERROR
            }
            getInitialData()
        }

    }

    private fun getInitialData() {
        // get shop operational hours list
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData(userSession.shopId)
    }

    private fun deleteShopHolidaySchedule() {
        // delete upcoming/ongoing holiday schedule
        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                action = ShopScheduleActionDef.ABORT,
        )
    }

    private fun setShopHolidaySchedule(startDate: Date, endDate: Date) {
        // create or edit for upcoming shop holiday schedule
        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
                action = ShopScheduleActionDef.CLOSED,
                closeStart = startDate.time.toString(),
                closeEnd = endDate.time.toString(),
                closeNow = DateUtils.isToday(startDate.time)
        )
        isNeedToShowToaster = true
    }

    private fun renderHolidaySection() {
        // render holiday switcher container
        shopIsOnHolidayContainer?.shouldShowWithAction(isShopOnScheduledHoliday) {
            // set holiday container text
            shopIsOnHolidayEndDateText?.text = getString(
                    R.string.shop_operational_hour_is_on_holiday_until,
                    OperationalHoursUtil.toShortDateFormat(selectedEndDate)
            )

            // set holiday switcher
            shopIsOnHolidaySwitcher?.apply {
                isChecked = isShopOnScheduledHoliday
                setOnCheckedChangeListener { _, isChecked ->
                    if (!isChecked) {
                        showConfirmDialogForOpenShopNow()
                    }
                }
            }
        }

        // render holiday schedule section
        buttonAddHoliday?.showWithCondition(!isShouldShowHolidaySchedule)
        holidayScheduleContainer?.shouldShowWithAction(isShouldShowHolidaySchedule) {
            autoChatTicker?.visible()
            tvShopHolidaySchedule?.apply {
                text = getString(
                        R.string.shop_operational_hour_list_textview,
                        OperationalHoursUtil.toIndonesianDateFormat(selectedStartDate),
                        OperationalHoursUtil.toIndonesianDateFormat(selectedEndDate)
                )
            }
        }
    }

    private fun initCalendarView(
            minDate: Date,
            maxDate: Date,
            calendarUnifyStart: UnifyCalendar?,
            calendarUnifyEnd: UnifyCalendar?,
            isChooseStartDate: Boolean,
            selectedDate: Date? = null
    ) {

        // init start date calendar view picker
        calendarUnifyStart?.shouldShowWithAction(isChooseStartDate) {
            calendarUnifyStart.apply {
                calendarPickerView?.run {
                    init(minDate, maxDate, listOf()).inMode(CalendarPickerView.SelectionMode.SINGLE).withSelectedDate(selectedDate ?: minDate)
                    setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                        override fun onDateSelected(date: Date) {
                            onSelectedHolidayStartDate(date)
                            adapter?.notifyDataSetChanged()
                        }
                        override fun onDateUnselected(date: Date) {}
                    })
                }
            }
        }

        // init end date calendar view picker
        calendarUnifyEnd?.shouldShowWithAction(!isChooseStartDate) {
            calendarUnifyEnd.apply {
                calendarPickerView?.run {
                    init(minDate, maxDate, listOf()).inMode(CalendarPickerView.SelectionMode.SINGLE).withSelectedDate(selectedDate ?: minDate)
                    setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                        override fun onDateSelected(date: Date) {
                            onSelectedHolidayEndDate(date)
                            adapter?.notifyDataSetChanged()
                        }
                        override fun onDateUnselected(date: Date) {}
                    })
                }
            }
        }

    }

    private fun setupStartDateTextFieldBottomSheet() {
        startDateTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    initCalendarView(
                            minDate = tomorrowDate,
                            maxDate = defaultMaxDate,
                            calendarUnifyStart = calendarUnifyStart,
                            calendarUnifyEnd = calendarUnifyEnd,
                            isChooseStartDate = true,
                            selectedDate = if (isActionEdit) selectedStartDate else null
                    )
                    calendarUnifyStart?.calendarPickerView?.adapter?.notifyDataSetChanged()
                }
            }

            // if shop is on scheduled holiday, seller can only edit the end date
            // so we disable the start date text field
            if (!isShopOnScheduledHoliday) {
                isEnabled = true
                requestFocus()
            }
            else {
                isEnabled = false
            }
        }
    }

    private fun setupEndDateTextFieldBottomSheet() {
        endDateTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    initCalendarView(
                            minDate = selectedStartDate,
                            maxDate = defaultMaxDate,
                            calendarUnifyStart = calendarUnifyStart,
                            calendarUnifyEnd = calendarUnifyEnd,
                            isChooseStartDate = false,
                            selectedDate = if (isActionEdit && isShopOnScheduledHoliday) selectedEndDate else null
                    )
                    calendarUnifyEnd?.calendarPickerView?.adapter?.notifyDataSetChanged()
                }
            }
            addTextChangedListener { field ->
                buttonSaveHolidaySchedule?.isEnabled = field?.length.isMoreThanZero()
            }

            // if shop is on scheduled holiday, seller can only edit the end date
            // so we enable the text field and requesting for focus
            if (!isShopOnScheduledHoliday) {
                isEnabled = false
            }
            else {
                isEnabled = true
                requestFocus()
            }
        }
    }

    private fun setupCloseNowCheckboxBottomSheet() {
        checkBoxCloseNow?.shouldShowWithAction(!isShopOnScheduledHoliday) {
            checkBoxCloseNow?.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedStartDate = todayDate
                        initCalendarView(
                                minDate = selectedStartDate,
                                maxDate = defaultMaxDate,
                                calendarUnifyStart = calendarUnifyStart,
                                calendarUnifyEnd = calendarUnifyEnd,
                                isChooseStartDate = false
                        )
                        calendarUnifyEnd?.calendarPickerView?.adapter?.notifyDataSetChanged()
                        startDateTextField?.textFieldInput?.apply {
                            isEnabled = false
                            setText(OperationalHoursUtil.toIndonesianDateFormat(todayDate))
                        }
                        endDateTextField?.textFieldInput?.apply {
                            isEnabled = true
                            requestFocus()
                        }
                    }
                    else {
                        startDateTextField?.textFieldInput?.apply {
                            isEnabled = true
                            text.clear()
                            requestFocus()
                        }
                    }
                }
            }
        }
    }

    private fun setupHolidayCalendarPickerBottomSheet() {
        context?.let { ctx ->
            holidayBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
                setChild(getHolidayDatePickerBottomSheetView())
                setCloseClickListener { dismiss() }
            }
        }
    }

    private fun setupEditHolidayCalendarPickerBottomSheet() {
        context?.let { ctx ->
            val bottomSheetEditView = getHolidayDatePickerBottomSheetView().apply {
                // set text field for existing start date
                startDateTextField?.textFieldInput?.setText(OperationalHoursUtil.toIndonesianDateFormat(selectedStartDate))

                // set text field for existing end date
                endDateTextField?.textFieldInput?.setText(OperationalHoursUtil.toIndonesianDateFormat(selectedEndDate))
            }
            holidayBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
                setChild(bottomSheetEditView)
                setCloseClickListener { dismiss() }
            }
        }
    }

    private fun setupActionBottomSheet() {
        context?.let { ctx ->
            actionBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_title_holiday))
                setChild(getActionBottomSheetView())
                showKnob = true
                showCloseIcon = false
            }
        }
    }

    private fun showHolidayBottomSheet() {
        fragmentManager?.let {
            holidayBottomSheet?.show(it, screenName)
            actionBottomSheet?.dismiss()
        }
    }

    private fun showActionBottomSheet() {
        fragmentManager?.let {
            actionBottomSheet?.show(it, screenName)
        }
    }

    private fun onSelectedHolidayStartDate(date: Date) {
        selectedStartDate = OperationalHoursUtil.setDefaultServerTimeForSelectedDate(date)
        startDateTextField?.textFieldInput?.setText(OperationalHoursUtil.toIndonesianDateFormat(date))
        endDateTextField?.textFieldInput?.apply {
            text.clear()
            isEnabled = true
        }
    }

    private fun onSelectedHolidayEndDate(date: Date) {
        selectedEndDate = OperationalHoursUtil.setDefaultServerTimeForSelectedDate(date)
        endDateTextField?.textFieldInput?.setText(OperationalHoursUtil.toIndonesianDateFormat(date))
    }

    private fun resetSelectedDates(isActionEdit: Boolean) {
        if (isActionEdit) {
            selectedStartDate = existingStartDate
            selectedEndDate = existingEndDate
        }
        else {
            selectedStartDate = tomorrowDate
            selectedEndDate = Date()
        }
    }

    private fun showConfirmDialogForSaveHolidaySchedule(startDate: Date, endDate: Date) {
        // confirm dialog to save shop holiday
        buildConfirmDialog(
                dialogTitle = getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_title),
                dialogDescription = getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_desc),
                ctaPrimaryText = getString(R.string.label_save),
                ctaSecondaryText = getString(R.string.label_cancel),
                primaryCTAListener = {
                    holidayBottomSheet?.dismiss()
                    showLoader()
                    setShopHolidaySchedule(startDate, endDate)
                },
                secondaryCTAListener = {}
        )?.show()
    }

    private fun showConfirmDialogForDeleteHolidaySchedule() {
        // confirm dialog to delete shop holiday
        buildConfirmDialog(
                dialogTitle = getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_title),
                dialogDescription = getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_desc),
                ctaPrimaryText = getString(R.string.label_delete_schedule),
                ctaSecondaryText = getString(R.string.label_cancel),
                primaryCTAListener = {
                    actionBottomSheet?.dismiss()
                    showLoader()
                    deleteShopHolidaySchedule()
                },
                secondaryCTAListener = {}
        )?.show()
    }

    private fun showConfirmDialogForOpenShopNow() {
        // confirm dialog to open shop now and abort ongoing holiday
        buildConfirmDialog(
                dialogTitle = getString(R.string.shop_operational_hour_abort_shop_holiday_dialog_title),
                dialogDescription = getString(R.string.shop_operational_hour_abort_shop_holiday_dialog_desc),
                ctaPrimaryText = getString(R.string.label_open_shop),
                ctaSecondaryText = getString(R.string.label_cancel),
                primaryCTAListener = {
                    showLoader()
                    deleteShopHolidaySchedule()
                },
                secondaryCTAListener = {
                    shopIsOnHolidaySwitcher?.isChecked = true
                }
        )?.show()
    }

    private fun buildConfirmDialog(
            dialogTitle: String,
            dialogDescription: String,
            ctaPrimaryText: String,
            ctaSecondaryText: String,
            primaryCTAListener: () -> Unit,
            secondaryCTAListener: () -> Unit
    ): DialogUnify? {
        return context?.let { ctx ->
            DialogUnify(ctx, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(dialogTitle)
                setDescription(dialogDescription)
                setPrimaryCTAText(ctaPrimaryText)
                setSecondaryCTAText(ctaSecondaryText)
                setPrimaryCTAClickListener {
                    dismiss()
                    primaryCTAListener()
                }
                setSecondaryCTAClickListener {
                    secondaryCTAListener()
                    dismiss()
                }
            }
        }
    }

    private fun showToaster(message: String, type: Int) {
        Toaster.build(requireView(), message, Toaster.LENGTH_SHORT, type, "Oke").show()
    }

    private fun showLoader() {
        loader?.visible()
        autoChatTicker?.gone()
        opsHourContainer?.gone()
        shopIsOnHolidayContainer?.gone()
    }

    private fun hideLoader() {
        loader?.gone()
        opsHourContainer?.visible()
    }
}