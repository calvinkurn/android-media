package com.tokopedia.unifyorderhistory.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.FILTER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 29/06/20.
 */

// Uoh = Unified Order History
class UohListActivity : BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.activity_uoh

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment(): UohListFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return UohListFragment.newInstance(bundle)
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        this.intent = newIntent
        inflateFragment()
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val filterStatus = it.getQueryParameter(FILTER).toEmptyStringIfNull()
            intent.putExtra(SOURCE_FILTER, filterStatus)
        }
    }
}
