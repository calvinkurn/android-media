package com.tokopedia.ordermanagement.snapshot.view.activity

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_DETAIL_ID
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_ID
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.ordermanagement.snapshot.view.fragment.SnapshotFragment

class SnapshotActivity : BaseSimpleActivity() {

    override fun getNewFragment(): SnapshotFragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SnapshotFragment.newInstance(bundle)
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val orderId = it.getQueryParameter(PARAM_ORDER_ID).toEmptyStringIfNull()
            val orderDetailId = it.getQueryParameter(PARAM_ORDER_DETAIL_ID).toEmptyStringIfNull()
            val isSnapShotFromSOM = intent.getBooleanExtra(IS_SNAPSHOT_FROM_SOM, false)
            intent.putExtra(IS_SNAPSHOT_FROM_SOM, isSnapShotFromSOM)
            intent.putExtra(PARAM_ORDER_ID, orderId)
            intent.putExtra(PARAM_ORDER_DETAIL_ID, orderDetailId)
        }
    }
}