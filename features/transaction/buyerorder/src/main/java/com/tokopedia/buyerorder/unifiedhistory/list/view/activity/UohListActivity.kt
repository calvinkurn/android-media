package com.tokopedia.buyerorder.unifiedhistory.list.view.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.FILTER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifyprinciples.R
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by fwidjaja on 29/06/20.
 */

// Uoh = Unified Order History
class UohListActivity: BaseSimpleActivity() {

    override fun setupStatusBar() {
        super.setupStatusBar()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!this.isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.Unify_N0)
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        toolbar.visibility = View.GONE
    }

    override fun getNewFragment(): UohListFragment {
        toolbar.visibility = View.GONE
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return UohListFragment.newInstance(bundle)
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val filterStatus = it.getQueryParameter(FILTER).toEmptyStringIfNull()
            intent.putExtra(SOURCE_FILTER, filterStatus)
        }
    }
}