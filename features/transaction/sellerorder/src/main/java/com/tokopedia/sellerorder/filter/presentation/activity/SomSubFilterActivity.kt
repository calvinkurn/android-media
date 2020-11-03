package com.tokopedia.sellerorder.filter.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.adapter.SomSubFilterCheckboxAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomSubFilterRadioButtonAdapter
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import kotlinx.android.synthetic.main.activity_som_sub_filter.*

class SomSubFilterActivity : BaseSimpleActivity(),
        SomSubFilterCheckboxAdapter.SomSubCheckboxFilterListener, SomSubFilterRadioButtonAdapter.SomSubRadioButtonFilterListener {

    companion object {
        const val KEY_FILTER_DATE = "key_filter_date"
        const val KEY_ID_FILTER = "key_id_filter"
        const val KEY_SOM_LIST_ORDER_PARAM = "key_som_list_order_param"
        const val KEY_SOM_ORDER_PARAM_CACHE = "key_som_order_param_cache"
        const val KEY_SOM_LIST_FILTER_CHIPS = "key_som_list_filter"
        const val KEY_CACHE_MANAGER_ID = "key_cache_manager_id"

        @JvmStatic
        fun newInstance(context: FragmentActivity?,
                        filterDate: String,
                        idFilter: String,
                        somSubFilterList: List<SomFilterChipsUiModel>,
                        cacheManagerId: String
        ) = Intent(context, SomSubFilterActivity::class.java).apply {
            putExtra(KEY_FILTER_DATE, filterDate)
            putExtra(KEY_ID_FILTER, idFilter)
            putParcelableArrayListExtra(KEY_SOM_LIST_FILTER_CHIPS, ArrayList(somSubFilterList))
            putExtra(KEY_CACHE_MANAGER_ID, cacheManagerId)
        }
    }

    private var filterDate: String = ""
    private var idFilter: String = ""
    private var cacheManagerId: String = ""

    private var somListGetOrderListParam = SomListGetOrderListParam()
    private var somSubFilterList: List<SomFilterChipsUiModel>? = null
    private var subFilterCheckboxAdapter: SomSubFilterCheckboxAdapter? = null

    private var subFilterRadioButtonAdapter: SomSubFilterRadioButtonAdapter? = null

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_som_sub_filter

    override fun onCreate(savedInstanceState: Bundle?) {
        filterDate = intent?.getStringExtra(KEY_FILTER_DATE) ?: ""
        idFilter = intent?.getStringExtra(KEY_ID_FILTER) ?: ""
        val manager = SaveInstanceCacheManager(this, savedInstanceState)
        val cacheManager =
                if (savedInstanceState == null) SaveInstanceCacheManager(this, cacheManagerId) else manager
        somListGetOrderListParam = cacheManager.get(KEY_SOM_LIST_ORDER_PARAM, SomListGetOrderListParam::class.java)
                ?: SomListGetOrderListParam()
        somSubFilterList = intent?.getParcelableArrayListExtra(KEY_SOM_LIST_FILTER_CHIPS)
                ?: arrayListOf()
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color.WHITE)
        btnSaveSubFilter()
        setToolbarSubFilter()
        setToggleResetSubFilter()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        initAdapter()
    }

    private fun initAdapter() {
        rvSomSubFilter.layoutManager = LinearLayoutManager(this@SomSubFilterActivity)
        when (idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> {
                subFilterRadioButtonAdapter = SomSubFilterRadioButtonAdapter(this)
                rvSomSubFilter.adapter = subFilterRadioButtonAdapter
                somSubFilterList?.let { subFilterRadioButtonAdapter?.setSubFilterList(it, idFilter) }
            }
            SomConsts.FILTER_COURIER, SomConsts.FILTER_TYPE_ORDER -> {
                subFilterCheckboxAdapter = SomSubFilterCheckboxAdapter(this)
                rvSomSubFilter.adapter = subFilterCheckboxAdapter
                somSubFilterList?.let {
                    subFilterCheckboxAdapter?.setSubFilterList(it, idFilter)
                }
            }
        }
    }

    private fun btnSaveSubFilter() {
        btnSaveSubFilter?.setOnClickListener {
            val cacheManager = SaveInstanceCacheManager(this, true)
            val cacheManagerId = cacheManager.id
            val intent = Intent()
            intent.putExtra(KEY_FILTER_DATE, filterDate)
            intent.putExtra(KEY_ID_FILTER, idFilter)
            intent.putParcelableArrayListExtra(KEY_SOM_LIST_FILTER_CHIPS, somSubFilterList?.let { it1 -> ArrayList(it1) })
            intent.putExtra(KEY_CACHE_MANAGER_ID, cacheManagerId)
            cacheManager.put(KEY_SOM_ORDER_PARAM_CACHE, somListGetOrderListParam)
            setResult(SomFilterBottomSheet.RESULT_CODE_FILTER_SEE_ALL, intent)
            this.finish()
        }
    }

    private fun setToolbarSubFilter() {
        som_sub_filter_toolbar.apply {
            isShowBackButton = true
            isShowShadow = false
            title = idFilter
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun isSelectedSubFilter(): Boolean {
        when (idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> {
                somSubFilterList = subFilterRadioButtonAdapter?.getSubFilterList()
            }
            SomConsts.FILTER_TYPE_ORDER, SomConsts.FILTER_COURIER -> {
                somSubFilterList = subFilterCheckboxAdapter?.getSubFilterList()
            }
        }
        somSubFilterList?.filter { it.idFilter == idFilter }?.forEach {
            if (it.isSelected) {
                return true
            }
        }
        return false
    }

    private fun setToggleResetSubFilter() {
        som_sub_filter_toolbar?.apply {
            if (isSelectedSubFilter()) {
                btnSaveSubFilter.isEnabled = true
                actionTextView?.show()
                actionText = getString(R.string.reset)
                actionTextView?.setOnClickListener {
                    somSubFilterList?.find { it.idFilter == idFilter }?.let {
                        when (it.idFilter) {
                            SomConsts.FILTER_STATUS_ORDER -> {
                                somListGetOrderListParam.statusList = emptyList()
                                subFilterRadioButtonAdapter?.resetRadioButtonFilter()
                            }
                            SomConsts.FILTER_TYPE_ORDER -> {
                                somListGetOrderListParam.orderTypeList = emptyList()
                                subFilterCheckboxAdapter?.resetCheckboxFilter()
                            }
                            SomConsts.FILTER_COURIER -> {
                                somListGetOrderListParam.shippingList = emptyList()
                                subFilterCheckboxAdapter?.resetCheckboxFilter()
                            }
                            else -> { }
                        }
                    }
                }
            } else {
                actionTextView?.hide()
                btnSaveSubFilter.isEnabled = false
            }
        }
    }

    override fun onCheckboxItemClicked(idList: List<Int>, position: Int) {
        when (idFilter) {
            SomConsts.FILTER_TYPE_ORDER -> {
                somListGetOrderListParam.orderTypeList = idList
            }
            SomConsts.FILTER_COURIER -> {
                somListGetOrderListParam.shippingList = idList
            }
        }
        setToggleResetSubFilter()
    }

    override fun onRadioButtonItemClicked(idList: List<Int>, position: Int) {
        when (idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> {
                somListGetOrderListParam.statusList = idList
            }
        }
        setToggleResetSubFilter()
    }

    interface SubFilterListener {
        fun onSaveSubFilter(
                somListGetOrderListParam: SomListGetOrderListParam,
                filterDate: String,
                idFilter: String,
                somSubFilterList: List<SomFilterChipsUiModel>)
    }
}