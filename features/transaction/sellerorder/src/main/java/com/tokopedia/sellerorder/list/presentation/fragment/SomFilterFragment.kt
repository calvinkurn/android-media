package com.tokopedia.sellerorder.list.presentation.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_COURIER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_STATUS
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_CHECKBOX
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_RADIO
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_SEPARATOR
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LIST_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_filter.*
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
        observingCourierList()
        observingOrderTypeList()
        observingStatusOrderList()
    }

    /*private fun setListeners() {
        rl_status?.isClickable = true
        rl_status?.setOnClickListener {
            context?.let {
                val intentStatus = SomSubFilterActivity.createIntent(it, listStatusRadioBtn, currentFilterParams, CATEGORY_ORDER_STATUS)
                startActivityForResult(intentStatus, REQUEST_ORDER_STATUS_LIST)
            }
        }
    }*/

    private fun setListeners() {
        btn_terapkan?.setOnClickListener {
            val listOrderParam = currentFilterParams
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_LIST_ORDER, listOrderParam)
            })
            activity?.finish()
        }
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
        var intervalDays = -90
        statusList.forEach { status ->
            listStatusRadioBtn.add(SomSubFilter(status.id, status.text, status.key, status.type, FILTER_TYPE_RADIO, status.orderStatusIdList))
            if (status.orderStatusIdList == currentFilterParams?.statusList && !status.type.equals(FILTER_TYPE_SEPARATOR, true)) {
                label_substatus.text = status.text
                statusTextFilled = true

                if (!status.key.equals(STATUS_ALL_ORDER, true)) intervalDays = -60
            }
        }
        if (!statusTextFilled) {
            label_substatus.setText(R.string.subtitle_status)
        }
        currentFilterParams?.startDate = getCalculatedFormattedDate("dd/MM/yyyy", intervalDays)

        println("++ currentFilterParams.startDate = ${currentFilterParams?.startDate}")
        println("++ currentFilterParams.endDate = ${currentFilterParams?.endDate}")

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
        currentFilterParams = SomListOrderParam()
        renderCourierList()
        renderOrderType()
        renderStatusList()
    }
}