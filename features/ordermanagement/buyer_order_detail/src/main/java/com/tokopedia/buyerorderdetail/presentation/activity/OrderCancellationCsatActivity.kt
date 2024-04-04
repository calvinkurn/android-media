package com.tokopedia.buyerorderdetail.presentation.activity

import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.buyerorderdetail.presentation.fragment.OrderCancellationCsatFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity

class OrderCancellationCsatActivity : BaseSimpleWebViewActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun createFragmentInstance(): Fragment {
        return OrderCancellationCsatFragment.newInstance(url, needLogin, allowOverride, pullToRefresh)
    }
}
