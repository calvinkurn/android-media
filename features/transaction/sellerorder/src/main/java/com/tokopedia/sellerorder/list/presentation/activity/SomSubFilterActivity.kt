package com.tokopedia.sellerorder.list.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_COURIER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_STATUS
import com.tokopedia.sellerorder.common.util.SomConsts.CATEGORY_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LIST_ORDER
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomSubFilter
import com.tokopedia.sellerorder.list.presentation.adapter.SomSubFilterAdapter
import kotlinx.android.synthetic.main.activity_filter_sublist.*
import kotlinx.android.synthetic.main.partial_toolbar_reset_button.*
import kotlin.collections.ArrayList

/**
 * Created by fwidjaja on 2019-09-13.
 */
class SomSubFilterActivity : BaseSimpleActivity() {
    private var listFilter: List<SomSubFilter> = arrayListOf()
    private lateinit var actionListener: ActionListener
    private var currentFilterParam = SomListOrderParam()
    private lateinit var subFilterAdapter: SomSubFilterAdapter
    private var category: String = ""

    /*override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view
    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple*/
    override fun getLayoutRes(): Int = R.layout.activity_filter_sublist
    override fun getNewFragment(): Fragment? = null

    interface ActionListener {
        fun onResetClicked()
        fun saveSubFilter() : SomListOrderParam
    }

    companion object {
        private const val PARAM_LIST_FILTER = "LIST_FILTER"
        private const val CURRENT_FILTER_PARAM = "CURRENT_FILTER_PARAM"
        private const val CATEGORY_PARAM  = "CATEGORY_PARAM"

        @JvmStatic
        fun createIntent(context: Context, listFilter: ArrayList<SomSubFilter>, currentFilterParams: SomListOrderParam?, category: String): Intent =
                 Intent(context, SomSubFilterActivity::class.java)
                        .putParcelableArrayListExtra(PARAM_LIST_FILTER, ArrayList(listFilter))
                         .putExtra(CURRENT_FILTER_PARAM, currentFilterParams)
                         .putExtra(CATEGORY_PARAM, category)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        subFilterAdapter = SomSubFilterAdapter()
        rv_sublist_filter.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subFilterAdapter
            setActionListener(adapter as SomSubFilterAdapter)
        }
        renderListSubFilter()
        toolbar = findViewById<View>(R.id.toolbar_filter) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            when {
                category.equals(CATEGORY_ORDER_STATUS, true) -> it.title = getString(R.string.title_status)
                category.equals(CATEGORY_ORDER_TYPE, true) -> it.title = getString(R.string.title_type)
                category.equals(CATEGORY_COURIER_TYPE, true) -> it.title = getString(R.string.title_courier)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        label_reset.setOnClickListener {
            actionListener.onResetClicked()
        }

        btn_simpan.setOnClickListener {
            val listOrderParam = actionListener.saveSubFilter()
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(PARAM_LIST_ORDER, listOrderParam)
            })
            finish()
        }
    }

    private fun renderListSubFilter() {
        listFilter = arrayListOf()
        listFilter = intent.getParcelableArrayListExtra(PARAM_LIST_FILTER) ?: listOf()
        currentFilterParam = intent.getParcelableExtra(CURRENT_FILTER_PARAM)
        category = intent.getStringExtra(CATEGORY_PARAM)

        subFilterAdapter.listSubFilter = listFilter.toMutableList()
        subFilterAdapter.currentFilterParam = currentFilterParam
        subFilterAdapter.category = category
        subFilterAdapter.notifyDataSetChanged()
    }

    private fun setActionListener(adapter: SomSubFilterAdapter) {
        this.actionListener = adapter
    }
}