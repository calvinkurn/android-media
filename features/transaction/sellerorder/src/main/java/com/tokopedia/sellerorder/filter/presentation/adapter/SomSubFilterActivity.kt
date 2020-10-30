package com.tokopedia.sellerorder.filter.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import kotlinx.android.synthetic.main.activity_som_sub_filter.*

class SomSubFilterActivity : BaseSimpleActivity(), SomSubFilterAdapter.SomSubFilterClickListener {

    companion object {
        const val KEY_FILTER_DATE = "key_filter_date"
        const val KEY_ID_FILTER = "key_id_filter"
        const val KEY_SOM_LIST_ORDER_PARAM = "key_som_list_order_param"
        const val KEY_SOM_LIST_FILTER_CHIPS = "key_som_list_filter"

        @JvmStatic
        fun newInstance(context: Context,
                        somListGetOrderListParam: SomListGetOrderListParam,
                        filterDate: String,
                        idFilter: String,
                        somSubFilterList: List<SomFilterChipsUiModel>
        ) = Intent(Intent(context, SomSubFilterActivity::class.java).apply {
                    putExtra(KEY_FILTER_DATE, filterDate)
                    putExtra(KEY_SOM_LIST_ORDER_PARAM, somListGetOrderListParam)
                    putExtra(KEY_ID_FILTER, idFilter)
                    putParcelableArrayListExtra(KEY_SOM_LIST_FILTER_CHIPS, ArrayList(somSubFilterList))
                }
        )
    }

    private var filterDate: String = ""
    private var idFilter: String = ""

    private var somListGetOrderListParam = SomListGetOrderListParam()
    private var somSubFilterList: List<SomFilterChipsUiModel>? = null
    private var subFilterAdapter: SomSubFilterAdapter? = null
    private var subFilterListener: SubFilterListener? = null

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_som_sub_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterDate = intent?.getStringExtra(KEY_FILTER_DATE) ?: ""
        idFilter = intent?.getStringExtra(KEY_ID_FILTER) ?: ""
        somListGetOrderListParam = intent?.getParcelableExtra(KEY_SOM_LIST_ORDER_PARAM)
                ?: SomListGetOrderListParam()
        somSubFilterList = intent?.getParcelableArrayListExtra(KEY_SOM_LIST_FILTER_CHIPS)
                ?: arrayListOf()
        btnSaveSubFilter()
        setToolbarSubFilter()
        setToggleResetSubFilter()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        subFilterAdapter = SomSubFilterAdapter(this)
        rvSomSubFilter?.apply {
            layoutManager = LinearLayoutManager(this@SomSubFilterActivity)
            adapter = subFilterAdapter
        }
        somSubFilterList?.let { subFilterAdapter?.setSubFilterList(it, idFilter) }
    }

    private fun btnSaveSubFilter() {
        btnSaveSubFilter?.setOnClickListener {
            subFilterListener?.onSaveSubFilter(somListGetOrderListParam, filterDate, idFilter,
                    somSubFilterList ?: listOf())
        }
    }

    private fun setToolbarSubFilter() {
        setSupportActionBar(som_sub_filter_toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            title = idFilter
        }
    }

    private fun setToggleResetSubFilter() {
        val isSelected = somSubFilterList?.filter { it.idFilter == idFilter }?.filter {
            it.isSelected
        }?.size ?: 0 > 0

        som_sub_filter_toolbar.apply {
            if (isSelected) {
                som_sub_filter_toolbar.actionTextView?.setOnClickListener {
                    somSubFilterList?.filter { it.idFilter == idFilter }?.map {
                        it.isSelected = false
                    }
                    somSubFilterList?.find { it.idFilter == idFilter }?.let {
                        when (it.idFilter) {
                            SomConsts.FILTER_TYPE_ORDER -> {
                                somListGetOrderListParam.orderTypeList = emptyList()
                            }
                            SomConsts.FILTER_COURIER -> {
                                somListGetOrderListParam.shippingList = emptyList()
                            }
                            SomConsts.FILTER_STATUS_ORDER -> {
                                somListGetOrderListParam.statusList = emptyList()
                            }
                        }
                    }
                }

                somSubFilterList?.let { it1 ->
                    subFilterAdapter?.setSubFilterList(it1, idFilter)
                }
            }
        }
    }

    override fun onCheckboxItemClicked(checkboxChecked: Boolean, idList: List<Int>, position: Int) {
        when (idFilter) {
            SomConsts.FILTER_TYPE_ORDER -> {
                somListGetOrderListParam.orderTypeList = idList
            }
            SomConsts.FILTER_COURIER -> {
                somListGetOrderListParam.shippingList = idList
            }
            SomConsts.FILTER_STATUS_ORDER -> {
                somListGetOrderListParam.statusList = idList
            }
        }
        somSubFilterList?.filter { it.idFilter == idFilter }?.getOrNull(position)?.isSelected = !checkboxChecked
        somSubFilterList?.let { subFilterAdapter?.setSubFilterList(it, idFilter) }
    }

    fun setupFilterListener(subFilterListener: SubFilterListener) {
        this.subFilterListener = subFilterListener
    }

    interface SubFilterListener {
        fun onSaveSubFilter(
                somListGetOrderListParam: SomListGetOrderListParam,
                filterDate: String,
                idFilter: String,
                somSubFilterList: List<SomFilterChipsUiModel>)
    }
}