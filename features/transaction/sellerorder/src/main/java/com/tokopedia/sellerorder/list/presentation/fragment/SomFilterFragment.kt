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
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_CHECKBOX
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_filter.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-11.
 */
class SomFilterFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var listStatusRadioBtn = ArrayList<SomSubFilter>()
    private var listOrderTypeCheckbox = ArrayList<SomSubFilter>()
    private var listCourierCheckbox = ArrayList<SomSubFilter>()
    private var orderTypeList: List<SomListAllFilter.Data.OrderType> = listOf()
    private var courierList: List<SomListAllFilter.Data.ShippingList> = listOf()
    private var currentFilterParams: SomListOrderParam = SomListOrderParam()

    private val somFilterViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomFilterViewModel::class.java]
    }

    companion object {
        private const val REQUEST_SHIPPING_LIST = 2880
        private const val REQUEST_ORDER_TYPE_LIST = 2881
        private const val REQUEST_ORDER_STATUS_LIST = 2881

        @JvmStatic
        fun newInstance(): SomFilterFragment {
            return SomFilterFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomListComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    private fun setListeners() {
        rl_status?.isClickable = true
        rl_status?.setOnClickListener {
            context?.let {
                val intentStatus = SomSubFilterActivity.createIntent(it, listStatusRadioBtn, currentFilterParams)
                startActivityForResult(intentStatus, REQUEST_ORDER_STATUS_LIST)
            }
        }
    }

    private fun loadAllFilter() {
        somFilterViewModel.loadSomFilterData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_all_filter))
    }

    private fun observingStatusOrderList() {

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

    private fun renderOrderType() {
        val listOrderType = arrayListOf<QuickFilterItem>()
        orderTypeList.forEach {
            val orderType = CustomViewQuickFilterItem()
            orderType.name = it.name
            orderType.type = it.key
            listOrderType.add(orderType)

            listOrderTypeCheckbox.add(SomSubFilter(it.id, it.name, it.key, "", FILTER_TYPE_CHECKBOX))
        }
        quick_filter_order_type?.renderFilter(listOrderType)

        label_see_all_type?.isClickable = true
        label_see_all_type?.setOnClickListener {
            context?.let {
                val intentStatus = SomSubFilterActivity.createIntent(it, listOrderTypeCheckbox, currentFilterParams)
                startActivityForResult(intentStatus, REQUEST_ORDER_TYPE_LIST)
            }
        }
    }

    private fun renderCourierList() {
        val listCourier = arrayListOf<QuickFilterItem>()
        courierList.forEach { courier ->
            val courierItem = CustomViewQuickFilterItem()
            courierItem.name = courier.shippingName
            courierItem.type = courier.shippingCode
            currentFilterParams.shippingList.filter { it == courier.shippingId }.map {
                courierItem.isSelected = true
            }
            listCourier.add(courierItem)

            listCourierCheckbox.add(SomSubFilter(courier.shippingId, courier.shippingName, courier.shippingCode, "", FILTER_TYPE_CHECKBOX))
        }
        quick_filter_order_courier?.renderFilter(listCourier)

        label_see_all_courier?.isClickable = true
        label_see_all_courier?.setOnClickListener {
            context?.let {
                val intentStatus = SomSubFilterActivity.createIntent(it, listCourierCheckbox, currentFilterParams)
                startActivityForResult(intentStatus, REQUEST_SHIPPING_LIST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SHIPPING_LIST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(SomConsts.PARAM_LIST_ORDER)) {
                    currentFilterParams = data.getParcelableExtra(SomConsts.PARAM_LIST_ORDER)
                    renderCourierList()
                }
            }
        }
    }
}