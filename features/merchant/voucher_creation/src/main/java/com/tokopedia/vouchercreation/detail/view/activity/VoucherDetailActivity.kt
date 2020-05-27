package com.tokopedia.vouchercreation.detail.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.getIntIntentExtra
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.view.fragment.DuplicateVoucherFragment
import com.tokopedia.vouchercreation.detail.view.fragment.VoucherDetailFragment

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailActivity : BaseActivity() {

    companion object {
        private const val PAGE_TYPE = "page_type"
        const val SHOP_ID = "shop_id"
        const val DETAIL_PAGE = 0
        const val DUPLICATE_PAGE = 1

        fun createDetailIntent(context: Context, pageType: Int): Intent {
            return Intent(context, VoucherDetailActivity::class.java).apply {
                putExtra(PAGE_TYPE, pageType)
            }
        }

        fun createDuplicateIntent(context: Context, pageType: Int): Intent {
            return Intent(context, VoucherDetailActivity::class.java).apply {
                putExtra(PAGE_TYPE, pageType)
            }
        }
    }

    private val pageType by getIntIntentExtra(PAGE_TYPE, DETAIL_PAGE)

    private val shopId by getIntIntentExtra(SHOP_ID, 0)

    override fun getScreenName(): String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_detail)

        showFragment()
        setWhiteStatusBar()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.voucherDetailContainer, getPageFragment())
                .commitNowAllowingStateLoss()
    }

    private fun getPageFragment(): Fragment {
        return if (pageType == DETAIL_PAGE) {
            VoucherDetailFragment.newInstance(shopId)
        } else {
            DuplicateVoucherFragment.newInstance()
        }
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.setBackgroundColor(Color.WHITE)
            setStatusBarColor(Color.TRANSPARENT)
            setLightStatusBar(true)
        }
    }
}