package com.tokopedia.buy_more_get_more.sort.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.sort.fragment.ShopProductSortFragment
import com.tokopedia.buy_more_get_more.sort.listener.ShopProductSortFragmentListener

class ShopProductSortActivity : BaseSimpleActivity(), ShopProductSortFragmentListener {
    private var sortName: String? = null
    override fun getNewFragment(): Fragment? {
        return ShopProductSortFragment.Companion.createInstance(sortName)
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_product_sort
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent != null && savedInstanceState == null) {
            sortName = intent.getStringExtra(SORT_VALUE)
            if (sortName.equals(Integer.toString(Int.MIN_VALUE), ignoreCase = true)) {
                sortName = null
            }
        }
        super.onCreate(savedInstanceState)
        if (null != supportActionBar) {
            supportActionBar?.title = ""
        }
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    companion object {
        const val SORT_VALUE = "SORT_VALUE"
        const val SORT_NAME = "SORT_NAME"
        const val SORT_ID = "SORT_ID"
        fun createIntent(context: Context?, sortName: String?): Intent {
            val intent = Intent(context, ShopProductSortActivity::class.java)
            intent.putExtra(SORT_VALUE, sortName)
            return intent
        }
    }

    override fun onSortItemSelected(sortId: String?, sortValue: String?, name: String?) {
        val intent = Intent()
        intent.putExtra(SORT_ID, sortId)
        intent.putExtra(SORT_VALUE, sortValue)
        intent.putExtra(SORT_NAME, name)
        setResult(RESULT_OK, intent)
        finish()
    }
}
