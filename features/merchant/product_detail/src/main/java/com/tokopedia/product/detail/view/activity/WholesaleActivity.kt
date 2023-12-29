package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.view.adapter.WholesaleAdapter

/**
 * @author Angga.Prasetiyo on 02/11/2015.
 */

class WholesaleActivity : BaseSimpleActivity() {

    private var wholesaleAdapter: WholesaleAdapter? = null

    companion object {
        private const val KEY_WHOLESALE_DATA = "WHOLESALE_DATA"

        @JvmStatic
        fun getIntent(context: Context, wholesaleList: ArrayList<Wholesale>): Intent {
            return Intent(context, WholesaleActivity::class.java).apply {
                putParcelableArrayListExtra(KEY_WHOLESALE_DATA, wholesaleList)
            }
        }
    }

    private var recyclerView: RecyclerView? = null

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
        wholesaleAdapter = WholesaleAdapter()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = wholesaleAdapter
        recyclerView?.addItemDecoration(DividerItemDecoration(this@WholesaleActivity))
    }

    fun showData() {
        val productWholesalePrices = intent.getParcelableArrayListExtra(KEY_WHOLESALE_DATA) ?: ArrayList<Wholesale>()
        wholesaleAdapter!!.setData(productWholesalePrices)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}
