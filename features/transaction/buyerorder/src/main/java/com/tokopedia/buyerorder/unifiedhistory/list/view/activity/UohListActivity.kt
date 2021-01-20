package com.tokopedia.buyerorder.unifiedhistory.list.view.activity

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.FILTER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull

/**
 * Created by fwidjaja on 29/06/20.
 */

// Uoh = Unified Order History
class UohListActivity: BaseSimpleActivity() {

    override fun getNewFragment(): UohListFragment {
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