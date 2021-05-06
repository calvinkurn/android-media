package com.tokopedia.sellerorder.filter.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.sellerorder.filter.presentation.model.SomSubFilterListWrapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import kotlinx.android.synthetic.main.activity_som_sub_filter.*

class SomSubFilterActivity : BaseSimpleActivity(),
        SomSubFilterCheckboxAdapter.SomSubCheckboxFilterListener, SomSubFilterRadioButtonAdapter.SomSubRadioButtonFilterListener {

    companion object {
        const val KEY_FILTER_DATE = "key_filter_date"
        const val KEY_ID_FILTER = "key_id_filter"
        const val KEY_SOM_LIST_FILTER_CHIPS = "key_som_list_filter"
        const val KEY_CACHE_MANAGER_ID = "key_cache_manager_id"
        const val ALL_FILTER = "Semua"

        @JvmStatic
        fun newInstance(context: Context?,
                        filterDate: String,
                        idFilter: String,
                        cacheManagerId: String
        ) = Intent(context, SomSubFilterActivity::class.java).apply {
            putExtra(KEY_FILTER_DATE, filterDate)
            putExtra(KEY_ID_FILTER, idFilter)
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
        cacheManagerId = intent?.getStringExtra(KEY_CACHE_MANAGER_ID) ?: ""
        val manager = SaveInstanceCacheManager(this, savedInstanceState)
        val cacheManager = if (savedInstanceState == null) SaveInstanceCacheManager(this, cacheManagerId) else manager
        somListGetOrderListParam = cacheManager.get(SomFilterBottomSheet.KEY_SOM_LIST_GET_ORDER_PARAM, SomListGetOrderListParam::class.java)
                ?: SomListGetOrderListParam()
        val somSubFilterListWrapper: SomSubFilterListWrapper? = cacheManager.get(KEY_SOM_LIST_FILTER_CHIPS, SomSubFilterListWrapper::class.java)
        somSubFilterList = somSubFilterListWrapper?.somSubFilterList
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        btnSaveSubFilter()
        setToolbarSubFilter()
        setToggleResetSubFilter()
        setupToggleShadowToolbar()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        initAdapter()
    }

    private fun initAdapter() {
        rvSomSubFilter.layoutManager = LinearLayoutManager(this)
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
        btnSaveSubFilter.isEnabled = false
        btnSaveSubFilter?.setOnClickListener {
            val cacheManager = SaveInstanceCacheManager(this, true)
            val cacheManagerId = cacheManager.id
            val intent = Intent()
            intent.putExtra(KEY_FILTER_DATE, filterDate)
            intent.putExtra(KEY_ID_FILTER, idFilter)
            intent.putExtra(KEY_CACHE_MANAGER_ID, cacheManagerId)
            cacheManager.put(SomFilterBottomSheet.KEY_SOM_LIST_GET_ORDER_PARAM, somListGetOrderListParam)
            cacheManager.put(KEY_SOM_LIST_FILTER_CHIPS, somSubFilterList?.let { it1 -> SomSubFilterListWrapper(it1) })
            setResult(SomFilterBottomSheet.RESULT_CODE_FILTER_SEE_ALL, intent)
            this.finish()
        }
    }

    private fun setupToggleShadowToolbar() {
        rvSomSubFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    som_sub_filter_toolbar.isShowShadow = newState != RecyclerView.SCROLL_STATE_IDLE
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        })
    }

    private fun setToolbarSubFilter() {
        setSupportActionBar(som_sub_filter_toolbar)
        supportActionBar?.apply {
            title = "$ALL_FILTER $idFilter"
            setDisplayShowHomeEnabled(true)
        }
        som_sub_filter_toolbar.apply {
            isShowBackButton = true
            isShowShadow = false
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun isSelectedSubFilter(): Boolean {
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
                    when (idFilter) {
                        SomConsts.FILTER_STATUS_ORDER -> {
                            somListGetOrderListParam.statusList = emptyList()
                            subFilterRadioButtonAdapter?.resetRadioButtonFilter()
                            somSubFilterList = subFilterRadioButtonAdapter?.getSubFilterList()
                        }
                        SomConsts.FILTER_TYPE_ORDER -> {
                            somListGetOrderListParam.orderTypeList = mutableSetOf()
                            subFilterCheckboxAdapter?.resetCheckboxFilter()
                            somSubFilterList = subFilterCheckboxAdapter?.getSubFilterList()
                        }
                        SomConsts.FILTER_COURIER -> {
                            somListGetOrderListParam.shippingList = mutableSetOf()
                            subFilterCheckboxAdapter?.resetCheckboxFilter()
                            somSubFilterList = subFilterCheckboxAdapter?.getSubFilterList()
                        }
                        else -> { }
                    }
                    setToggleResetSubFilter()
                }
            } else {
                actionTextView?.hide()
            }
        }
    }

    private fun setSelectedSomSubFilter() {
        when (idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> {
                somSubFilterList = subFilterRadioButtonAdapter?.getSubFilterList()
            }
            SomConsts.FILTER_TYPE_ORDER, SomConsts.FILTER_COURIER -> {
                somSubFilterList = subFilterCheckboxAdapter?.getSubFilterList()
            }
        }
    }

    override fun onCheckboxItemClicked(id: Int, position: Int, checked: Boolean) {
        when (idFilter) {
            SomConsts.FILTER_TYPE_ORDER -> {
                if (checked) {
                    somListGetOrderListParam.orderTypeList.add(id)
                } else {
                    somListGetOrderListParam.orderTypeList.remove(id)
                }
            }
            SomConsts.FILTER_COURIER -> {
                if (checked) {
                    somListGetOrderListParam.shippingList.add(id)
                } else {
                    somListGetOrderListParam.shippingList.remove(id)
                }
            }
        }
        setToggleResetSubFilter()
        setSelectedSomSubFilter()
    }

    override fun onRadioButtonItemClicked(idList: List<Int>, position: Int) {
        when (idFilter) {
            SomConsts.FILTER_STATUS_ORDER -> {
                somListGetOrderListParam.statusList = idList
            }
        }
        setToggleResetSubFilter()
        setSelectedSomSubFilter()
    }
}