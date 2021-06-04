package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsSetOperationalHoursActivity
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopSettingsOperationalHoursListAdapter
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        val HOLIDAY_EDIT_BOTTOMSHEET_LAYOUT = R.layout.bottomsheet_shop_edit_holiday

        private const val NO_HOLIDAY_DATE = "0"
    }

    @Inject
    lateinit var shopSettingsOperationalHoursViewModel: ShopSettingsOperationalHoursViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

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

    private var isNeedToShowToaster: Boolean = false
    private var setShopHolidayScheduleStatusMessage: String = ""
    private var setShopHolidayScheduleStatusType: Int = 0
    private var startHolidayDateMilliseconds: Long = 0L
    private var endHolidayDateMilliseconds: Long = 0L
    private var selectedStartDate: Date = Date()
    private var selectedEndDate: Date = Date()
    private var todayDate = Calendar.getInstance(TimeZone.getDefault()).time

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
            activity?.run {
                startActivity(Intent(this, ShopSettingsSetOperationalHoursActivity::class.java))
            }
        }

        // set click listener for button add holiday schedule
        buttonAddHoliday?.setOnClickListener {
            setupHolidayCalendarBottomSheet()
            showHolidayBottomSheet()
        }

        // set click listener holiday edit action button
        holidayEditActionButton?.setOnClickListener {
            setupActionBottomSheet()
            showActionBottomSheet()
        }
    }

    private fun setupToolbar() {
        headerOpsHour?.addRightIcon(R.drawable.ic_ops_hour_help)
    }

    private fun setupActionBottomSheet() {
        context?.let { ctx ->
            val bottomSheetView = View.inflate(context, HOLIDAY_EDIT_BOTTOMSHEET_LAYOUT, null).apply {
                val actionEdit = findViewById<LinearLayout>(R.id.action_edit_holiday)
                val actionDelete = findViewById<LinearLayout>(R.id.action_delete_holiday)

                actionEdit.setOnClickListener {
                    //setupEditHolidayCalendarBottomSheet()
                    //showHolidayBottomSheet()
                }

                actionDelete.setOnClickListener {
                    showConfirmDialogForDeleteHolidaySchedule()
                }
            }

            actionBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_title_holiday))
                setChild(bottomSheetView)
                showKnob = true
                showCloseIcon = false
            }
        }
    }

    private fun showActionBottomSheet() {
        fragmentManager?.let {
            actionBottomSheet?.show(it, screenName)
        }
    }

//    private fun setupEditHolidayCalendarBottomSheet() {
//
//        val currentStartHolidayDate = Date(startHolidayDateMilliseconds)
//        val currentEndHolidayDate = Date(endHolidayDateMilliseconds)
//        val nextYear = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time
//
//        context?.let { ctx ->
//            val bottomSheetView = View.inflate(context, HOLIDAY_BOTTOMSHEET_LAYOUT, null).apply {
//                val calendarUnify = findViewById<UnifyCalendar>(R.id.ops_hour_holiday_calendar)
//                val buttonSaveHolidaySchedule = findViewById<UnifyButton>(R.id.btn_save_holiday_schedule)
//                val calendarView = calendarUnify.calendarPickerView
//
//                // init calendar view
//                calendarView?.run {
//                    calendarView
//                            .init(todayDate, nextYear, listOf())
//                            .inMode(CalendarPickerView.SelectionMode.RANGE)
//                            .withSelectedDates(listOf(currentStartHolidayDate, currentEndHolidayDate))
//                }
//
//                // set listener for save schedule button
//                buttonSaveHolidaySchedule.setOnClickListener {
//                    showConfirmDialogForSaveHolidaySchedule(selectedStartDate, selectedEndDate)
//                }
//            }
//
//            holidayBottomSheet = BottomSheetUnify().apply {
//                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
//                setChild(bottomSheetView)
//                setCloseClickListener { dismiss() }
//            }
//        }
//    }

    private fun initCalendarView(
            minDate: Date,
            maxDate: Date,
            calendarView: CalendarPickerView?,
            startTextField: TextFieldUnify,
            endTextField: TextFieldUnify,
            isChooseStartDate: Boolean
    ) {
        calendarView?.run {
            calendarView
                    .init(minDate, maxDate, listOf())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
            setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                override fun onDateSelected(date: Date) {
                    if (isChooseStartDate) {
                        selectedStartDate = date
                        startTextField.textFieldInput.setText(OperationalHoursUtil.toIndonesianDateFormat(date))
                        endTextField.textFieldInput.text.clear()
                        endTextField.textFieldInput.isEnabled = true
                    }
                    else {
                        selectedEndDate = date
                        endTextField.textFieldInput.setText(OperationalHoursUtil.toIndonesianDateFormat(date))
                    }
                }

                override fun onDateUnselected(date: Date) {
                    // no op
                }
            })
        }
    }

    private fun setupHolidayCalendarBottomSheet() {
        val calendar = Calendar.getInstance()
        val todayDate = calendar.time
        val nextYear = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time

        context?.let { ctx ->
            val bottomSheetView = View.inflate(context, HOLIDAY_BOTTOMSHEET_LAYOUT, null).apply {

                val calendarUnifyStart = findViewById<UnifyCalendar>(R.id.ops_hour_holiday_calendar_start)
                val calendarUnifyEnd = findViewById<UnifyCalendar>(R.id.ops_hour_holiday_calendar_end)

                val buttonSaveHolidaySchedule = findViewById<UnifyButton>(R.id.btn_save_holiday_schedule)
                val startDateTextField = findViewById<TextFieldUnify>(R.id.text_field_start_date_holiday)
                val endDateTextField = findViewById<TextFieldUnify>(R.id.text_field_end_date_holiday)

                // setup text field start date
                startDateTextField.apply {
                    textFieldInput.inputType = InputType.TYPE_NULL
                    textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            calendarUnifyStart.visible()
                            calendarUnifyEnd.gone()
                            initCalendarView(todayDate, nextYear, calendarUnifyStart.calendarPickerView, this, endDateTextField, true)
                        }
                    }
                    textFieldInput.requestFocus()
                }

                // setup text field end date
                endDateTextField.apply {
                    isEnabled = false
                    textFieldInput.inputType = InputType.TYPE_NULL
                    textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            calendarUnifyStart.gone()
                            calendarUnifyEnd.visible()
                            initCalendarView(selectedStartDate, nextYear, calendarUnifyEnd.calendarPickerView, startDateTextField, this, false)
                        }
                    }
                    textFieldInput.addTextChangedListener {
                        buttonSaveHolidaySchedule.isEnabled = it?.length.isMoreThanZero()
                    }
                }

                // set listener for save schedule button
                buttonSaveHolidaySchedule.setOnClickListener {
                    showConfirmDialogForSaveHolidaySchedule(selectedStartDate, selectedEndDate)
                }
            }

            holidayBottomSheet = BottomSheetUnify().apply {
                setTitle(ctx.getString(R.string.shop_operational_hour_set_holiday_schedule_title))
                setChild(bottomSheetView)
                setCloseClickListener { dismiss() }
            }
        }
    }

    private fun showConfirmDialogForSaveHolidaySchedule(startDate: Date, endDate: Date) {
        context?.let { ctx ->
            DialogUnify(ctx, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_title))
                setDescription(getString(R.string.shop_operational_hour_set_holiday_schedule_dialog_desc))
                setPrimaryCTAText(getString(R.string.label_save))
                setSecondaryCTAText(getString(R.string.label_cancel))
                setPrimaryCTAClickListener {
                    holidayBottomSheet?.dismiss()
                    dismiss()
                    showLoader()
                    setShopHolidaySchedule(startDate, endDate)
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun showConfirmDialogForDeleteHolidaySchedule() {
        context?.let { ctx ->
            DialogUnify(ctx, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_title))
                setDescription(getString(R.string.shop_operational_hour_delete_holiday_schedule_dialog_desc))
                setPrimaryCTAText(getString(R.string.label_delete_schedule))
                setSecondaryCTAText(getString(R.string.label_cancel))
                setPrimaryCTAClickListener {
                    actionBottomSheet?.dismiss()
                    dismiss()
                    showLoader()
                    deleteShopHolidaySchedule()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun showHolidayBottomSheet() {
        fragmentManager?.let {
            holidayBottomSheet?.show(it, screenName)
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

                // update UI for holiday section
                val holidayInfo = opsHourListUiModel.closeInfo
                startHolidayDateMilliseconds = holidayInfo.closeDetail.startDate.toLongOrZero()
                endHolidayDateMilliseconds = holidayInfo.closeDetail.endDate.toLongOrZero()
                renderHolidaySection(holidayInfo)

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
                setShopHolidayScheduleStatusMessage = "Jadwal libur berhasil ditambahkan"
                setShopHolidayScheduleStatusType = Toaster.TYPE_NORMAL
            }
            if (result is Fail) {
                setShopHolidayScheduleStatusMessage = "Oops, jadwal tidak tersimpan. Cek koneksi dan ulangi lagi, ya."
                setShopHolidayScheduleStatusType = Toaster.TYPE_ERROR
            }
            getInitialData()
        }

    }

    private fun getInitialData() {
        shopSettingsOperationalHoursViewModel.getShopOperationalHoursInitialData(
                userSession.shopId
        )
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
                closeNow = startDate == todayDate
        )
        isNeedToShowToaster = true
    }

    private fun renderHolidaySection(holidayInfo: ShopInfo.ClosedInfo) {
        val closedInfo = holidayInfo.closeDetail
        val isShowHolidaySchedule = closedInfo.startDate != NO_HOLIDAY_DATE && closedInfo.endDate != NO_HOLIDAY_DATE

        // render section UI
        buttonAddHoliday?.showWithCondition(!isShowHolidaySchedule)
        holidayScheduleContainer?.shouldShowWithAction(isShowHolidaySchedule) {
            autoChatTicker?.visible()
            tvShopHolidaySchedule?.apply {
                val startDate = Date(closedInfo.startDate.toLong() * 1000L)
                val endDate = Date(closedInfo.endDate.toLong() * 1000L)
                text = getString(
                        R.string.shop_operational_hour_list_textview,
                        OperationalHoursUtil.toIndonesianDateFormat(startDate),
                        OperationalHoursUtil.toIndonesianDateFormat(endDate)
                )
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
    }

    private fun hideLoader() {
        loader?.gone()
        opsHourContainer?.visible()
    }
}