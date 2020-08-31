package com.tokopedia.sellerorder.list.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickTerapkanOnFilterPage
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_COURIER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_STATUS
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.END_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_CHECKBOX
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_RADIO
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_SEPARATOR
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LIST_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.START_DATE
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_som_filter.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by fwidjaja on 2019-09-11.
 */
class SomFilterFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var listStatusRadioBtn = ArrayList<SomSubFilter>()
    private var listOrderTypeCheckbox = ArrayList<SomSubFilter>()
    private var listCourierCheckbox = ArrayList<SomSubFilter>()
    private var listSelectedCourierId = ArrayList<Int>()
    private var listSelectedOrderTypeId = ArrayList<Int>()
    private var orderTypeList: List<SomListAllFilter.Data.OrderType> = listOf()
    private var courierList: List<SomListAllFilter.Data.ShippingList> = listOf()
    private var statusList: List<SomListAllFilter.Data.OrderFilterSomSingle.StatusList> = listOf()
    private var currentFilterParams: SomListOrderParam? = SomListOrderParam()
    private var tabActive: String = ""
    private val somFilterViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomFilterViewModel::class.java]
    }

    companion object {
        private const val REQUEST_SHIPPING_LIST = 2880
        private const val REQUEST_ORDER_TYPE_LIST = 2881
        private const val REQUEST_ORDER_STATUS_LIST = 2882

        private const val ERROR_GET_FILTER_DATA = "Error When get filters data."
        private const val ERROR_GET_ORDER_TYPE_FILTER = "Error When get order type filters."
        private const val ERROR_GET_COURIER_TYPE_FILTER = "Error When get courier type filters."
        private const val ERROR_GET_STATUS_ORDER_TYPE_FILTER = "Error When get status order type filters."

        @JvmStatic
        fun newInstance(bundle: Bundle): SomFilterFragment {
            return SomFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_LIST_ORDER, bundle.getParcelable(PARAM_LIST_ORDER))
                    putString(PARAM_TAB_ACTIVE, bundle.getString(PARAM_TAB_ACTIVE))
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomListComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentFilterParams = arguments?.getParcelable(PARAM_LIST_ORDER)
            tabActive = arguments?.getString(PARAM_TAB_ACTIVE).toString()
        }
        loadAllFilter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setupDatePickers()
        setupErrorState()
        observingCourierList()
        observingOrderTypeList()
        observingStatusOrderList()
        observingGetFilterStatus()
    }

    private fun setListeners() {
        btn_terapkan?.setOnClickListener {
            eventClickTerapkanOnFilterPage(tabActive)
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_LIST_ORDER, currentFilterParams)
            })
            activity?.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(flag: String) {
        context?.let {  context ->
            val minDate = Calendar.getInstance()
            minDate.set(Calendar.YEAR, 2017)
            val maxDate = Calendar.getInstance()
            val isEndDateFilled = currentFilterParams?.endDate?.isNotEmpty()
            isEndDateFilled?.let { isNotEmpty ->
                if (isNotEmpty && flag.equals(START_DATE, true)) {
                    val splitEndDate = currentFilterParams?.endDate?.split('/')
                    splitEndDate?.let {
                        maxDate.set(it[2].toInt(), it[1].toInt() - 1, it[0].toInt())
                    }
                }
            }
            val isStartDateFilled = currentFilterParams?.startDate?.isNotEmpty()
            isStartDateFilled?.let { isNotEmpty ->
                if (isNotEmpty && flag.equals(END_DATE, true)) {
                    val splitStartDate = currentFilterParams?.startDate?.split('/')
                    splitStartDate?.let {
                        minDate.set(it[2].toInt(), it[1].toInt() - 1, it[0].toInt())
                    }
                }
            }

            val currentDate = Calendar.getInstance()
            val splitDate: List<String>?

            if (flag.equals(START_DATE, true)) {
                splitDate = currentFilterParams?.startDate?.split('/')
            } else {
                splitDate = currentFilterParams?.endDate?.split('/')
            }

            splitDate?.let {
                currentDate.set(it[2].toInt(), it[1].toInt()-1, it[0].toInt())
                val datePicker = DatePickerUnify(context, minDate, currentDate, maxDate)
                fragmentManager?.let { it1 -> datePicker.show(it1, "") }
                datePicker.datePickerButton.setOnClickListener {
                    val resultDate = datePicker.getDate()
                    val monthInt = resultDate[1]+1
                    var monthStr = monthInt.toString()
                    if (monthStr.length == 1) monthStr = "0$monthStr"

                    var dateStr = resultDate[0].toString()
                    if (dateStr.length == 1) dateStr = "0$dateStr"

                    if (flag.equals(START_DATE, true)) {
                        currentFilterParams?.startDate = "$dateStr/$monthStr/${resultDate[2]}"
                        et_start_date?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                    } else {
                        currentFilterParams?.endDate = "$dateStr/$monthStr/${resultDate[2]}"
                        et_end_date?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                    }
                    datePicker.dismiss()
                }
                if (flag.equals(START_DATE, true)) {
                    datePicker.setTitle(getString(R.string.mulai_dari))
                } else {
                    datePicker.setTitle(getString(R.string.sampai))
                }
                datePicker.setCloseClickListener { datePicker.dismiss() }
            }
        }
    }

    private fun setupDatePickers() {
        currentFilterParams?.startDate?.let {
            et_start_date?.setText(convertFormatDate(it, "dd/MM/yyyy", "dd MMM yyyy"))
        }

        currentFilterParams?.endDate?.let {
            et_end_date?.setText(convertFormatDate(it, "dd/MM/yyyy", "dd MMM yyyy"))
        }

        et_start_date?.setOnClickListener { showDatePicker(START_DATE) }
        et_end_date?.setOnClickListener { showDatePicker(END_DATE) }
    }

    private fun setupErrorState() {
        title_empty?.text = getString(R.string.error_list_title)
        desc_empty?.text = getString(R.string.error_list_desc)
        ic_empty?.loadImageDrawable(R.drawable.ic_som_error_list)
        btn_cek_peluang?.apply {
            visibility = View.VISIBLE
            text = getString(R.string.retry_load_list)
            setOnClickListener {
                loadAllFilter()
            }
        }
    }

    private fun loadAllFilter() {
        progressSomFilter?.show()
        layoutEmpty?.hide()
        layoutSuccess?.hide()
        somFilterViewModel.loadSomFilterData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_all_filter))
    }

    private fun observingOrderTypeList() {
        somFilterViewModel.orderTypeListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    orderTypeList = it.data
                    renderOrderType()
                }
                is Fail -> SomErrorHandler.logMessage(it.throwable, ERROR_GET_ORDER_TYPE_FILTER)
            }
        })
    }

    private fun observingCourierList() {
        somFilterViewModel.shippingListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    courierList = it.data
                    renderCourierList()
                }
                is Fail -> SomErrorHandler.logMessage(it.throwable, ERROR_GET_COURIER_TYPE_FILTER)
            }
        })
    }

    private fun observingStatusOrderList() {
        somFilterViewModel.statusOrderListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    statusList = it.data
                    renderStatusList()
                }
                is Fail -> SomErrorHandler.logMessage(it.throwable, ERROR_GET_STATUS_ORDER_TYPE_FILTER)
            }
        })
    }

    private fun observingGetFilterStatus() {
        somFilterViewModel.filterListResult.observe(this, Observer {
            when (it) {
                is Success -> toggleFilterView(true)
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_FILTER_DATA)
                    toggleFilterView(false)
                }
            }
        })
    }

    private fun toggleFilterView(isSuccess: Boolean) {
        progressSomFilter?.hide()
        layoutEmpty?.showWithCondition(!isSuccess)
        layoutSuccess?.showWithCondition(isSuccess)
        rl_button?.showWithCondition(isSuccess)
    }

    private fun renderOrderType() {
        if (listOrderTypeCheckbox.isNotEmpty()) listOrderTypeCheckbox.clear()
        if (orderTypeList.isNotEmpty()) {
            val listOrderType = arrayListOf<QuickFilterItem>()
            orderTypeList.forEach { orderType ->
                val orderTypeItem = CustomViewQuickFilterItem()
                orderTypeItem.name = orderType.name
                orderTypeItem.type = orderType.id.toString()
                currentFilterParams?.orderTypeList?.filter { it == orderType.id }?.map {
                    orderTypeItem.isSelected = true
                    listSelectedOrderTypeId.add(orderType.id)
                }
                listOrderType.add(orderTypeItem)
                listOrderTypeCheckbox.add(SomSubFilter(orderType.id, orderType.name, orderType.key, "", FILTER_TYPE_CHECKBOX))
            }
            quick_filter_order_type?.renderFilter(listOrderType)
            quick_filter_order_type?.setListener {
                val orderTypeId = it.toInt()
                if (orderTypeId in listSelectedOrderTypeId) {
                    listSelectedOrderTypeId.remove(orderTypeId)
                } else {
                    listSelectedOrderTypeId.add(orderTypeId)
                }
                currentFilterParams?.orderTypeList = listSelectedOrderTypeId
            }

            label_see_all_type?.isClickable = true
            label_see_all_type?.setOnClickListener {
                context?.let {
                    val intentStatus = SomSubFilterActivity.createIntent(it, listOrderTypeCheckbox, currentFilterParams, CATEGORY_ORDER_TYPE)
                    startActivityForResult(intentStatus, REQUEST_ORDER_TYPE_LIST)
                }
            }
        }
    }

    private fun renderCourierList() {
        if (listCourierCheckbox.isNotEmpty()) listCourierCheckbox.clear()
        if (courierList.isNotEmpty()) {
            val listCourier = arrayListOf<QuickFilterItem>()
            courierList.forEach { courier ->
                val courierItem = CustomViewQuickFilterItem()
                courierItem.name = courier.shippingName
                courierItem.type = courier.shippingId.toString()
                currentFilterParams?.shippingList?.filter { it == courier.shippingId }?.map {
                    courierItem.isSelected = true
                    listSelectedCourierId.add(courier.shippingId)
                }
                listCourier.add(courierItem)
                listCourierCheckbox.add(SomSubFilter(courier.shippingId, courier.shippingName, courier.shippingCode, "", FILTER_TYPE_CHECKBOX))
            }
            quick_filter_order_courier?.renderFilter(listCourier)
            quick_filter_order_courier?.setListener {
                val shippingId = it.toInt()
                if (shippingId in listSelectedCourierId) {
                    listSelectedCourierId.remove(shippingId)
                } else {
                    listSelectedCourierId.add(shippingId)
                }
                currentFilterParams?.shippingList = listSelectedCourierId
            }

            label_see_all_courier?.isClickable = true
            label_see_all_courier?.setOnClickListener {
                context?.let {
                    val intentStatus = SomSubFilterActivity.createIntent(it, listCourierCheckbox, currentFilterParams, CATEGORY_COURIER_TYPE)
                    startActivityForResult(intentStatus, REQUEST_SHIPPING_LIST)
                }
            }
        }
    }

    private fun renderStatusList() {
        listStatusRadioBtn.clear()
        var statusTextFilled = false
        statusList.forEach { status ->
            listStatusRadioBtn.add(SomSubFilter(status.id, status.text, status.key, status.type, FILTER_TYPE_RADIO, status.orderStatusIdList))
            if (status.orderStatusIdList == currentFilterParams?.statusList && !status.type.equals(FILTER_TYPE_SEPARATOR, true)) {
                label_substatus?.text = status.text
                statusTextFilled = true
            }
        }
        if (!statusTextFilled) {
            label_substatus?.setText(R.string.subtitle_status)
        }

        rl_status?.isClickable = true
        rl_status?.setOnClickListener {
            context?.let {
                val intentStatus = SomSubFilterActivity.createIntent(it, listStatusRadioBtn, currentFilterParams, CATEGORY_ORDER_STATUS)
                startActivityForResult(intentStatus, REQUEST_ORDER_STATUS_LIST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == REQUEST_SHIPPING_LIST || requestCode == REQUEST_ORDER_TYPE_LIST || requestCode == REQUEST_ORDER_STATUS_LIST) && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(PARAM_LIST_ORDER)) {
                    currentFilterParams = data.getParcelableExtra(PARAM_LIST_ORDER)
                    renderCourierList()
                    renderOrderType()
                    renderStatusList()
                }
            }
        }
    }

    fun onResetClicked() {
        SomAnalytics.eventClickResetButtonOnFilterPage(tabActive)
        resetFilters()
        renderCourierList()
        renderOrderType()
    }

    fun onBackClicked() {
        SomAnalytics.eventClickBackButtonOnFilterPage(tabActive)
    }

    @SuppressLint("SetTextI18n")
    private fun resetFilters() {
        if (statusList.isNotEmpty()) {
            val resetStartDate = getCalculatedFormattedDate("dd/MM/yyyy", -90)
            val resetEndDate = Date().toFormattedString("dd/MM/yyyy")
            currentFilterParams = SomListOrderParam(
                    startDate = resetStartDate,
                    endDate = resetEndDate,
                    statusList = statusList.first().orderStatusIdList)

            // start date
            val splitStartDate = resetStartDate.split('/')
            if (splitStartDate.isNotEmpty()) {
                var startDateStr = splitStartDate[0]
                if (startDateStr.length == 1) startDateStr = "0$startDateStr"
                et_start_date?.setText("$startDateStr ${convertMonth((splitStartDate[1].toInt()-1))} ${splitStartDate[2]}")
            }

            // end date
            val splitEndDate = resetEndDate.split('/')
            if (splitEndDate.isNotEmpty()) {
                var endDateStr = splitEndDate[0]
                if (endDateStr.length == 1) endDateStr = "0$endDateStr"
                et_end_date?.setText("$endDateStr ${convertMonth((splitEndDate[1].toInt()-1))} ${splitEndDate[2]}")
            }

            label_substatus?.text = statusList.first().text
        }
    }
}