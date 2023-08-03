package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsSetOperationalHoursActivity
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopSettingsOperationalHoursListAdapter
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.databinding.BottomsheetNewShopSetHolidayBinding
import com.tokopedia.shop.settings.databinding.BottomsheetShopEditHolidayBinding
import com.tokopedia.shop.settings.databinding.FragmentShopSettingsOperationalHoursBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Calendar
import java.util.TimeZone
import java.util.Date
import javax.inject.Inject

/**
 * Created by Rafli Syam on 28/04/2021
 */
class ShopSettingsOperationalHoursFragment : BaseDaggerFragment(), HasComponent<ShopSettingsComponent> {

    companion object {

        @JvmStatic
        fun createInstance(): ShopSettingsOperationalHoursFragment = ShopSettingsOperationalHoursFragment()

        private const val NO_HOLIDAY_DATE = "0"
        private const val REQUEST_CODE_SET_OPS_HOUR = 100
        private const val WEBVIEW_APPLINK_FORMAT = "%s?url=%s"
    }

    @Inject
    lateinit var shopSettingsOperationalHoursViewModel: ShopSettingsOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private val todayDate = Calendar.getInstance(TimeZone.getDefault()).time
    private val tomorrowDate = Calendar.getInstance(TimeZone.getDefault()).apply { add(Calendar.DAY_OF_YEAR, 1) }.time
    private val defaultMaxDate = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time // next year

    private var binding by autoClearedNullable<FragmentShopSettingsOperationalHoursBinding>()
    private var headerOpsHour: HeaderUnify? = null
    private var icEditOperationalHour: IconUnify? = null
    private var rvOperationalHourList: RecyclerView? = null
    private var rvOpsHourListAdapter: ShopSettingsOperationalHoursListAdapter? = null
    private var holidayBottomSheet: BottomSheetUnify? = null
    private var actionBottomSheet: BottomSheetUnify? = null
    private var buttonAddHoliday: UnifyButton? = null
    private var loader: LoaderUnify? = null
    private var opsHourContainer: LinearLayout? = null
    private var holidayScheduleContainer: RelativeLayout? = null
    private var tvHolidaySchedule: Typography? = null
    private var autoChatTicker: Ticker? = null
    private var holidayEditActionButton: IconUnify? = null
    private var startDateTextField: TextFieldUnify? = null
    private var endDateTextField: TextFieldUnify? = null
    private var calendarUnify: UnifyCalendar? = null
    private var holidayCalendarBottomSheetFooter: LinearLayout? = null
    private var buttonSaveHolidaySchedule: UnifyButton? = null
    private var shopIsOnHolidayContainer: CardUnify? = null
    private var shopIsOnHolidayEndDateText: Typography? = null
    private var openShopButton: UnifyButton? = null
    private var imageOngoingHoliday: ImageUnify? = null
    private var containerScroller: NestedScrollView? = null

    private var isNeedToShowToaster: Boolean = false
    private var isNeedToShowOpenShopToaster: Boolean = false
    private var isShopClosed: Boolean = false
    private var isShopOnScheduledHoliday: Boolean = false
    private var isShouldShowHolidaySchedule: Boolean = false
    private var isActionEdit: Boolean = false
    private var isChooseStartDate: Boolean = true
    private var isDateChanged: Boolean = false
    private var setShopHolidayScheduleStatusMessage: String = ""
    private var setShopHolidayScheduleStatusType: Int = 0
    private var startHolidayDateMilliseconds: Long = 0L
    private var endHolidayDateMilliseconds: Long = 0L
    private var selectedStartDate = tomorrowDate
    private var selectedEndDate = Date()
    private var existingStartDate = Date()
    private var existingEndDate = Date()
    private var isHolidayBottomSheetShown = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShopSettingsOperationalHoursBinding.inflate(inflater, container, false).apply {
            initView(this)
            initRecyclerView()
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitialData()
        setupToolbar()
        setBackgroundColor()
        initListener()
        observeLiveData()
        showLoader()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SET_OPS_HOUR) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    setShopHolidayScheduleStatusMessage = data?.extras?.getString(
                        ShopSettingsSetOperationalHoursFragment.EXTRA_SET_OPS_HOUR_RESPONSE_KEY,
                        getString(R.string.shop_operational_success_update_operational_hours)
                    ) ?: getString(R.string.shop_operational_success_update_operational_hours)
                    setShopHolidayScheduleStatusType = Toaster.TYPE_NORMAL
                    isNeedToShowToaster = true
                    showLoader()
                    getInitialData()
                }
                Activity.RESULT_CANCELED -> {
                    data?.let {
                        if (it.getBooleanExtra(ShopSettingsSetOperationalHoursFragment.EXTRA_IS_NEED_TO_OPEN_CALENDAR_KEY, false)) {
                            isActionEdit = false
                            resetSelectedDates(isActionEdit)
                            setupHolidayCalendarPickerBottomSheet()
                            showHolidayBottomSheet()
                        }
                    }
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(shopSettingsOperationalHoursViewModel.shopSettingsOperationalHoursListUiModel)
        removeObservers(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule)
        removeObservers(shopSettingsOperationalHoursViewModel.shopInfoAbortSchedule)
    }

    private fun initView(binding: FragmentShopSettingsOperationalHoursBinding?) {
        binding?.apply {
            headerOpsHour = headerShopOperationalHours
            icEditOperationalHour = icEditOpsHour
            rvOperationalHourList = rvOpsHourList
            buttonAddHoliday = btnAddHolidaySchedule
            loader = opsHourLoader
            opsHourContainer = opsHourParentContainer
            holidayScheduleContainer = shopHolidayScheduleContainer
            tvHolidaySchedule = tvShopHolidaySchedule
            autoChatTicker = opsHourChatAutoTicker
            holidayEditActionButton = opsHourImgScheduleAction
            shopIsOnHolidayContainer = holidayToggleContainer
            shopIsOnHolidayEndDateText = tvHolidayEnd
            openShopButton = btnOpenShop
            imageOngoingHoliday = imgShopHoliday
            containerScroller = opsHourScrollerContainer
        }

        // setup image ongoing holiday container image
        imageOngoingHoliday?.loadImage(getString(R.string.shop_operational_hour_image_ongoing_holiday_container_url))
    }

    private fun getHolidayDatePickerBottomSheetView(): View {
        return BottomsheetNewShopSetHolidayBinding.inflate(LayoutInflater.from(context)).apply {
            calendarUnify = opsHourHolidayCalendarStart
            startDateTextField = textFieldStartDateHoliday
            endDateTextField = textFieldEndDateHoliday
            holidayCalendarBottomSheetFooter = holidayCalendarFooter
            buttonSaveHolidaySchedule = btnSaveHolidaySchedule

            // init calendar range view
            initCalendarRangeView(
                minDate = todayDate,
                maxDate = defaultMaxDate,
                isActionEdit = isActionEdit
            )

            // setup text field start date
            setupStartDateTextFieldBottomSheet()

            // setup text field end date
            setupEndDateTextFieldBottomSheet()

            // set listener for save schedule button
            buttonSaveHolidaySchedule?.setOnClickListener {
                showConfirmDialogForSaveHolidaySchedule(selectedStartDate, selectedEndDate)
            }
        }.root
    }

    private fun getActionBottomSheetView(): View {
        return BottomsheetShopEditHolidayBinding.inflate(LayoutInflater.from(context)).apply {
            val actionEdit = actionEditHoliday
            val actionDelete = actionDeleteHoliday

            // set action edit schedule click listener
            actionEdit.setOnClickListener {
                isActionEdit = true
                resetSelectedDates(isActionEdit)
                setupEditHolidayCalendarPickerBottomSheet()
                showHolidayBottomSheet()
            }

            // set action delete schedule click listener
            actionDelete.setOnClickListener {
                actionBottomSheet?.dismiss()
                showConfirmDialogForDeleteHolidaySchedule()
            }
        }.root
    }

    private fun initRecyclerView() {
        rvOpsHourListAdapter = ShopSettingsOperationalHoursListAdapter()
        rvOperationalHourList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = rvOpsHourListAdapter
        }
    }

    private fun initListener() {
        // set click listener for icon edit ops hour
        icEditOperationalHour?.setOnClickListener {
            startActivityForResult(
                Intent(context, ShopSettingsSetOperationalHoursActivity::class.java),
                REQUEST_CODE_SET_OPS_HOUR
            )
        }

        // set click listener for button add holiday schedule
        buttonAddHoliday?.apply {
            setOnClickListener {
                if(!isHolidayBottomSheetShown){
                    isHolidayBottomSheetShown =  true
                    isActionEdit = false
                    resetSelectedDates(isActionEdit)
                    setupHolidayCalendarPickerBottomSheet()
                    showHolidayBottomSheet()
                }
            }
            text = getString(R.string.shop_operational_hour_set_holiday_schedule_title).split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
        }

        // set click listener holiday edit action button
        holidayEditActionButton?.setOnClickListener {
            setupActionBottomSheet()
            showActionBottomSheet()
        }

        // setup holiday ticker
        autoChatTicker?.setHtmlDescription(
            getString(
                R.string.shop_operational_hour_ticker_description_auto_chat,
                getString(R.string.shop_operational_hour_ticker_auto_chat_reply)
            )
        )
        autoChatTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl == getString(R.string.shop_operational_hour_ticker_auto_chat_reply)) {
                    RouteManager.route(context, ApplinkConst.TOKOPEDIA_CHAT_AUTO_REPLY_SETTINGS)
                }
            }

            override fun onDismiss() {}
        })

        // setup nested scroll listener
        containerScroller?.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                headerOpsHour?.isShowShadow = scrollY.isMoreThanZero()
            }
        )
    }

    private fun setupToolbar() {
        headerOpsHour?.apply {
            val rightIconDrawableColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_help).setColorFilter(rightIconDrawableColor)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            rightIcons?.get(0)?.setOnClickListener {
                // go to seller education page
                RouteManager.route(
                    context,
                    String.format(
                        WEBVIEW_APPLINK_FORMAT,
                        ApplinkConst.WEBVIEW,
                        getString(R.string.shop_operational_hour_desc_ticker_holiday_url)
                    )
                )
            }
            isShowShadow = false
        }
    }

    private fun setBackgroundColor() {
        activity?.run {
            window.decorView.setBackgroundColor(
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
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
                val statusInfo = opsHourListUiModel.statusInfo

                // holiday schedule is on going
                isShopClosed = statusInfo.shopStatus == ShopStatusDef.CLOSED

                // should show upcoming holiday section if available
                isShouldShowHolidaySchedule = holidayInfo.closeDetail.startDate != NO_HOLIDAY_DATE && holidayInfo.closeDetail.endDate != NO_HOLIDAY_DATE
                isShopOnScheduledHoliday = holidayInfo.closeDetail.status == ShopStatusDef.CLOSED

                if (isShouldShowHolidaySchedule) {
                    startHolidayDateMilliseconds = holidayInfo.closeDetail.startDate.toLongOrZero() * 1000L
                    endHolidayDateMilliseconds = holidayInfo.closeDetail.endDate.toLongOrZero() * 1000L
                    existingStartDate = Date(startHolidayDateMilliseconds)
                    existingEndDate = Date(endHolidayDateMilliseconds)
                    selectedStartDate = existingStartDate
                    selectedEndDate = existingEndDate
                } else {
                    // prevent edit flag if there is no existing upcoming date
                    isActionEdit = false
                }

                if (!isShopClosed) {
                    isShopOnScheduledHoliday = false
                }
                renderHolidaySection(isHolidayBySchedule = isShopOnScheduledHoliday)

                // update UI for operational hours list section
                val opsHourList = opsHourListUiModel.operationalHourList
                rvOpsHourListAdapter?.updateOpsHourList(opsHourList)
                autoChatTicker?.showWithCondition(
                    isShopClosed || isShouldShowHolidaySchedule || opsHourList.any {
                        // show ticker when shop closed or contains holiday in a week
                        it.startTime == it.endTime
                    }
                )

                // show toaster if refresh after set shop holiday schedule
                if (isNeedToShowToaster || isNeedToShowOpenShopToaster) {
                    showToaster(setShopHolidayScheduleStatusMessage, setShopHolidayScheduleStatusType)
                    isNeedToShowToaster = false
                    isNeedToShowOpenShopToaster = false
                }
            }
            if (result is Fail) {
                hideLoader()
                showToaster(ErrorHandler.getErrorMessage(context, result.throwable), Toaster.TYPE_ERROR)
            }
        }

        // observe abort shop close schedule
        observe(shopSettingsOperationalHoursViewModel.shopInfoAbortSchedule) { result ->
            if (result is Success) {
                setShopHolidayScheduleStatusMessage = result.data.takeIf {
                    it.isNotEmpty()
                } ?: getString(R.string.shop_operational_hour_abort_holiday_schedule_success)
                setShopHolidayScheduleStatusType = Toaster.TYPE_NORMAL
                getInitialData()
            }
            if (result is Fail) {
                setShopHolidayScheduleStatusMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                setShopHolidayScheduleStatusType = Toaster.TYPE_ERROR
                hideLoader()
                showToaster(setShopHolidayScheduleStatusMessage, setShopHolidayScheduleStatusType)
            }
        }

        // observe create shop close schedule
        observe(shopSettingsOperationalHoursViewModel.shopInfoCloseSchedule) { result ->
            if (result is Success) {
                setShopHolidayScheduleStatusMessage = result.data.takeIf {
                    it.isNotEmpty()
                } ?: if (!isShopOnScheduledHoliday) {
                    // create holiday schedule success
                    getString(R.string.shop_operational_hour_set_holiday_schedule_success)
                } else {
                    // open shop immediately success
                    getString(R.string.shop_operational_hour_abort_ongoing_holiday_schedule_success)
                }
                setShopHolidayScheduleStatusType = Toaster.TYPE_NORMAL
                getInitialData()
            }
            if (result is Fail) {
                setShopHolidayScheduleStatusMessage = getString(R.string.shop_operational_hour_create_holiday_schedule_fail)
                setShopHolidayScheduleStatusType = Toaster.TYPE_ERROR
                hideLoader()
                showToaster(setShopHolidayScheduleStatusMessage, setShopHolidayScheduleStatusType)
            }
        }
    }

    private fun getInitialData() {
        // get shop operational hours list
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData(userSession.shopId)
    }

    private fun deleteShopHolidaySchedule() {
        // delete upcoming/ongoing holiday schedule
        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
            action = ShopScheduleActionDef.ABORT
        )
        isNeedToShowToaster = true
    }

    private fun openShopNow() {
        // open shop immediately when seller is ongoing holiday period.
        shopSettingsOperationalHoursViewModel.setShopCloseSchedule(
            action = ShopScheduleActionDef.OPEN
        )
        isNeedToShowOpenShopToaster = isShopOnScheduledHoliday
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

    private fun renderHolidaySection(isHolidayBySchedule: Boolean = false) {
        // render holiday switcher container
        shopIsOnHolidayContainer?.shouldShowWithAction(isHolidayBySchedule) {
            // set "sampai" end date text
            shopIsOnHolidayEndDateText?.text = getString(
                R.string.shop_operational_hour_is_on_holiday_until,
                OperationalHoursUtil.toIndonesianDateFormat(selectedEndDate, isRequireSimpleFormat = false)
            )

            // set open shop button
            openShopButton?.setOnClickListener {
                showConfirmDialogForOpenShopNow()
            }
        }

        // render holiday schedule section
        buttonAddHoliday?.showWithCondition(!isShouldShowHolidaySchedule)
        holidayScheduleContainer?.shouldShowWithAction(isShouldShowHolidaySchedule) {
            tvHolidaySchedule?.text = OperationalHoursUtil.toIndonesianDateRangeFormat(selectedStartDate, selectedEndDate)
        }
    }

    private fun initCalendarRangeView(
        minDate: Date,
        maxDate: Date,
        isActionEdit: Boolean
    ) {
        // init calendar range view
        calendarUnify?.apply {
            calendarPickerView?.run {
                if (!isActionEdit) {
                    init(minDate, maxDate, listOf()).inMode(CalendarPickerView.SelectionMode.RANGE)
                } else {
                    val actualMinimalDate = if (selectedStartDate.before(minDate)) {
                        minDate
                    } else {
                        selectedStartDate
                    }
                    init(minDate, maxDate, listOf()).inMode(CalendarPickerView.SelectionMode.RANGE).withSelectedDates(
                        listOf(
                            actualMinimalDate,
                            selectedEndDate
                        )
                    )
                }
                setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                    override fun onDateSelected(date: Date) {
                        isDateChanged = true
                        if (isChooseStartDate) {
                            onSelectedHolidayStartDate(date)
                            isChooseStartDate = false
                        } else {
                            when {
                                OperationalHoursUtil.setDefaultServerTimeForSelectedDate(date) == selectedStartDate -> {
                                    // choose same date for holiday just for one day
                                    onSelectedHolidayEndDate(date)
                                    isChooseStartDate = true
                                }
                                date.before(selectedStartDate) -> {
                                    onSelectedHolidayStartDate(date)
                                }
                                else -> {
                                    onSelectedHolidayEndDate(date)
                                    isChooseStartDate = true
                                }
                            }
                        }

                        // show footer if seller have picked both start & end date
                        holidayCalendarBottomSheetFooter?.shouldShowWithAction(endDateTextField?.textFieldInput?.text?.isNotEmpty().orFalse()) {
                            isDateChanged = true
                        }
                    }
                    override fun onDateUnselected(date: Date) {}
                })
            }
        }
    }

    private fun setupStartDateTextFieldBottomSheet() {
        startDateTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            isClickable = false
            requestFocus()
        }
    }

    private fun setupEndDateTextFieldBottomSheet() {
        endDateTextField?.textFieldInput?.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            isClickable = false
        }
    }

    private fun handleDismissCalendarPickerBottomSheet() {
        if (isDateChanged) {
            showConfirmDialogForDataChanged()
        } else {
            holidayBottomSheet?.dismiss()
        }
        isHolidayBottomSheetShown = false
    }

    private fun setupHolidayCalendarPickerBottomSheet() {
        context?.let { ctx ->
            isDateChanged = false
            isChooseStartDate = true
            holidayBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
                setChild(getHolidayDatePickerBottomSheetView())
                setCloseClickListener {
                    handleDismissCalendarPickerBottomSheet()
                }
            }
            holidayBottomSheet?.setShowListener {
                holidayBottomSheet?.dialog?.setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        // handle dialog if bottomSheet dismissed through device back button
                        handleDismissCalendarPickerBottomSheet()
                    }
                    true
                }
            }
        }
    }

    private fun setupEditHolidayCalendarPickerBottomSheet() {
        context?.let { ctx ->
            isDateChanged = false
            isChooseStartDate = true
            val bottomSheetEditView = getHolidayDatePickerBottomSheetView().apply {
                // set text field for existing start date
                startDateTextField?.textFieldInput?.setText(
                    getString(
                        R.string.shop_operational_hour_selected_holiday_date_text,
                        OperationalHoursUtil.toSimpleIndonesianDayFormat(selectedStartDate),
                        OperationalHoursUtil.toIndonesianDateFormat(selectedStartDate, isRequireSimpleFormat = true, isShowYear = false)
                    )
                )

                // set text field for existing end date
                endDateTextField?.textFieldInput?.setText(
                    getString(
                        R.string.shop_operational_hour_selected_holiday_date_text,
                        OperationalHoursUtil.toSimpleIndonesianDayFormat(selectedEndDate),
                        OperationalHoursUtil.toIndonesianDateFormat(selectedEndDate, isRequireSimpleFormat = true, isShowYear = false)
                    )
                )
            }
            holidayBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
                setChild(bottomSheetEditView)
                setCloseClickListener {
                    handleDismissCalendarPickerBottomSheet()
                }
            }
            holidayBottomSheet?.setShowListener {
                holidayBottomSheet?.dialog?.setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                        // handle dialog if bottomSheet dismissed through device back button
                        handleDismissCalendarPickerBottomSheet()
                    }
                    true
                }
            }
        }
    }

    private fun setupActionBottomSheet() {
        actionBottomSheet = BottomSheetUnify().apply {
            setChild(getActionBottomSheetView())
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
        startDateTextField?.textFieldInput?.apply {
            setText(
                getString(
                    R.string.shop_operational_hour_selected_holiday_date_text,
                    OperationalHoursUtil.toSimpleIndonesianDayFormat(date),
                    OperationalHoursUtil.toIndonesianDateFormat(date, isRequireSimpleFormat = true, isShowYear = false)
                )
            )
            isFocusable = false
            isFocusableInTouchMode = false
        }
        endDateTextField?.textFieldInput?.apply {
            text.clear()
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    private fun onSelectedHolidayEndDate(date: Date) {
        selectedEndDate = OperationalHoursUtil.setDefaultServerTimeForSelectedDate(date)
        endDateTextField?.textFieldInput?.apply {
            setText(
                getString(
                    R.string.shop_operational_hour_selected_holiday_date_text,
                    OperationalHoursUtil.toSimpleIndonesianDayFormat(date),
                    OperationalHoursUtil.toIndonesianDateFormat(date, isRequireSimpleFormat = true, isShowYear = false)
                )
            )
            clearFocus()
            isFocusable = false
            isFocusableInTouchMode = false
        }
    }

    private fun resetSelectedDates(isActionEdit: Boolean) {
        if (isActionEdit) {
            selectedStartDate = existingStartDate
            selectedEndDate = existingEndDate
        } else {
            selectedStartDate = tomorrowDate
            selectedEndDate = tomorrowDate
        }
    }

    private fun showConfirmDialogForDataChanged() {
        // confirm dialog for detect any changes
        buildConfirmDialog(
            dialogTitle = getString(R.string.shop_operational_hour_dialog_data_change_title),
            dialogDescription = getString(R.string.shop_operational_hour_dialog_data_change_holiday_description),
            ctaPrimaryText = getString(R.string.action_delete),
            ctaSecondaryText = getString(R.string.shop_operational_hour_label_back),
            primaryCTAListener = {
                holidayBottomSheet?.dismiss()
                isHolidayBottomSheetShown = false
            },
            secondaryCTAListener = {}
        )?.show()
    }

    private fun showConfirmDialogForSaveHolidaySchedule(startDate: Date, endDate: Date) {
        // confirm dialog to save shop holiday
        buildConfirmDialog(
            dialogTitle = getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_title),
            dialogDescription = getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_desc),
            ctaPrimaryText = getString(R.string.label_save),
            ctaSecondaryText = getString(R.string.shop_operational_hour_label_back),
            primaryCTAListener = {
                holidayBottomSheet?.dismiss()
                showLoader()
                setShopHolidaySchedule(startDate, endDate)
                isHolidayBottomSheetShown = false
            },
            secondaryCTAListener = {}
        )?.show()
    }

    private fun showConfirmDialogForDeleteHolidaySchedule() {
        // confirm dialog to delete shop holiday
        buildConfirmDialog(
            dialogTitle = getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_title),
            dialogDescription = getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_desc),
            ctaPrimaryText = getString(R.string.action_delete),
            ctaSecondaryText = getString(R.string.shop_operational_hour_label_back),
            primaryCTAListener = {
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
            ctaSecondaryText = getString(R.string.shop_operational_hour_label_back),
            primaryCTAListener = {
                showLoader()
                openShopNow()
            },
            secondaryCTAListener = {}
        )?.apply { show() }
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
        headerOpsHour?.gone()
    }

    private fun hideLoader() {
        loader?.gone()
        opsHourContainer?.visible()
        headerOpsHour?.visible()
        autoChatTicker?.visible()
        shopIsOnHolidayContainer?.showWithCondition(isShopOnScheduledHoliday)
    }
}
