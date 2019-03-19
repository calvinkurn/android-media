package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.view.adapter.WholesaleAdapter
import kotlinx.android.synthetic.main.activity_wholesale_detail.*

/**
 * @author Angga.Prasetiyo on 02/11/2015.
 */

class WholesaleActivity : BaseSimpleActivity() {

    private var wholesaleAdapter: WholesaleAdapter? = null

    companion object {
        val KEY_WHOLESALE_DATA = "WHOLESALE_DATA"

        @JvmStatic
        fun getIntent(context: Context, wholesaleList: ArrayList<Wholesale>): Intent {
            return Intent(context, WholesaleActivity::class.java).apply {
                putParcelableArrayListExtra(KEY_WHOLESALE_DATA, wholesaleList)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_wholesale_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        showData()
    }

    override fun isShowCloseButton() = true

    private fun setupAdapter() {
        wholesaleAdapter = WholesaleAdapter(this)
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = wholesaleAdapter
        recycler_view.addItemDecoration(DividerItemDecoration(this@WholesaleActivity))
    }

    fun showData() {
        val productWholesalePrices = intent.getParcelableArrayListExtra<Wholesale>(KEY_WHOLESALE_DATA)
        wholesaleAdapter!!.setData(productWholesalePrices)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this@WholesaleActivity.overridePendingTransition(0, R.anim.push_down)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}