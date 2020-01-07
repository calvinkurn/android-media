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
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickButtonPeluangInEmptyState
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_COURIER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_STATUS
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.END_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_CHECKBOX
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_RADIO
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_SEPARATOR
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LIST_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.START_DATE
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.usecase.coroutines.Success
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
    private val somFilterViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomFilterViewModel::class.java]
    }

    companion object {
        private const val REQUEST_SHIPPING_LIST = 2880
        private const val REQUEST_ORDER_TYPE_LIST = 2881
        private const val REQUEST_ORDER_STATUS_LIST = 2882

        @JvmStatic
        fun newInstance(bundle: Bundle): SomFilterFragment {
            return SomFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_LIST_ORDER, bundle.getParcelable(PARAM_LIST_ORDER))
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
        observingCourierList()
        observingOrderTypeList()
        observingStatusOrderList()
    }

    private fun setListeners() {
        btn_terapkan?.setOnClickListener {
            eventClickButtonPeluangInEmptyState()
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_LIST_ORDER, currentFilterParams)
            })
            activity?.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(flag: String) {
        context?.let {  context ->
            val minDate = GregorianCalendar(2010, 0, 1)
            var dateMax = GregorianCalendar(LocaleUtils.getCurrentLocale(context))
            val isEndDateFilled = currentFilterParams?.endDate?.isNotEmpty()
            isEndDateFilled?.let { isNotEmpty ->
                if (isNotEmpty && flag.equals(START_DATE, true)) {
                    val splitEndDate = currentFilterParams?.endDate?.split('/')
                    splitEndDate?.let {
                        dateMax = GregorianCalendar(it[2].toInt(), it[1].toInt(), it[0].toInt())
                    }
                }
            }

            if (flag.equals(START_DATE, true)) {
                val splitStartDate = currentFilterParams?.startDate?.split('/')
                splitStartDate?.let {
                    val dateNow = GregorianCalendar(it[2].toInt(), it[1].toInt()-1, it[0].toInt())
                    val datePickerStart = DatePickerUnify(context, minDate, dateNow, dateMax)
                    datePickerStart.setTitle(getString(R.string.mulai_dari))
                    fragmentManager?.let { it1 -> datePickerStart.show(it1, "") }
                    datePickerStart.datePickerButton.setOnClickListener {
                        val resultDate = datePickerStart.getDate()
                        val monthInt = resultDate[1]+1
                        var monthStr = monthInt.toString()
                        if (monthStr.length == 1) monthStr = "0$monthStr"

                        var dateStr = resultDate[0].toString()
                        if (dateStr.length == 1) dateStr = "0$dateStr"

                        currentFilterParams?.startDate = "$dateStr/$monthStr/${resultDate[2]}"
                        et_start_date.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        datePickerStart.dismiss()
                    }
                    datePickerStart.setCloseClickListener { datePickerStart.dismiss() }
                }
            } else {
                val splitEndDate = currentFilterParams?.endDate?.split('/')
                splitEndDate?.let {
                    val dateNow = GregorianCalendar(it[2].toInt(), it[1].toInt()-1, it[0].toInt())
                    val datePickerEnd = DatePickerUnify(context, minDate, dateNow, dateMax)
                    datePickerEnd.setTitle(getString(R.string.sampai))
                    fragmentManager?.let { it1 -> datePickerEnd.show(it1, "") }
                    datePickerEnd.datePickerButton.setOnClickListener {
                        val resultDate = datePickerEnd.getDate()
                        val monthInt = resultDate[1]+1
                        var monthStr = monthInt.toString()
                        if (monthStr.length == 1) monthStr = "0$monthStr"

                        var dateStr = resultDate[0].toString()
                        if (dateStr.length == 1) dateStr = "0$dateStr"

                        currentFilterParams?.endDate = "$dateStr/$monthStr/${resultDate[2]}"
                        et_end_date.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        datePickerEnd.dismiss()
                    }
                    datePickerEnd.setCloseClickListener { datePickerEnd.dismiss() }
                }
            }
        }
    }

    private fun setupDatePickers() {
        currentFilterParams?.startDate?.let {
            et_start_date.setText(convertFormatDate(it, "dd/MM/yyyy", "dd MMM yyyy"))
        }

        currentFilterParams?.endDate?.let {
            et_end_date.setText(convertFormatDate(it, "dd/MM/yyyy", "dd MMM yyyy"))
        }

        et_start_date?.setOnClickListener { showDatePicker(START_DATE) }
        et_end_date?.setOnClickListener { showDatePicker(END_DATE) }
    }

    private fun loadAllFilter() {
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
            }
        })
    }

    private fun renderOrderType() {
        listOrderTypeCheckbox.clear()
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

    private fun renderCourierList() {
        listCourierCheckbox.clear()
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

    private fun renderStatusList() {
        listStatusRadioBtn.clear()
        var statusTextFilled = false
        statusList.forEach { status ->
            listStatusRadioBtn.add(SomSubFilter(status.id, status.text, status.key, status.type, FILTER_TYPE_RADIO, status.orderStatusIdList))
            if (status.orderStatusIdList == currentFilterParams?.statusList && !status.type.equals(FILTER_TYPE_SEPARATOR, true)) {
                label_substatus.text = status.text
                statusTextFilled = true
            }
        }
        if (!statusTextFilled) {
            label_substatus.setText(R.string.subtitle_status)
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
        resetFilters()
        renderCourierList()
        SomAnalytics.eventClickResetButtonOnFilterPage()
    }

    @SuppressLint("SetTextI18n")
    private fun resetFilters() {
        val resetStartDate = getCalculatedFormattedDate("dd/MM/yyyy", -90)
        val resetEndDate = Date().toFormattedString("dd/MM/yyyy")
        currentFilterParams = SomListOrderParam(
                startDate = resetStartDate,
                endDate = resetEndDate,
                statusList = statusList.first().orderStatusIdList)

        // start date
        val splitStartDate = resetStartDate.split('/')
        var startDateStr = splitStartDate[0]
        if (startDateStr.length == 1) startDateStr = "0$startDateStr"

        et_start_date.setText("$startDateStr ${convertMonth((splitStartDate[1].toInt()-1))} ${splitStartDate[2]}")

        // end date
        val splitEndDate = resetEndDate.split('/')
        var endDateStr = splitEndDate[0]
        if (endDateStr.length == 1) endDateStr = "0$endDateStr"

        et_end_date.setText("$endDateStr ${convertMonth((splitEndDate[1].toInt()-1))} ${splitEndDate[2]}")
        label_substatus.text = statusList.first().text
    }
}