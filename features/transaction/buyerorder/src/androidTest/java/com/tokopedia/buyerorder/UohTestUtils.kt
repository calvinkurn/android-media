package com.tokopedia.buyerorder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter

/**
 * Created by fwidjaja on 27/10/20.
 */

internal fun RecyclerView?.getUohItemAdapter(): UohItemAdapter {
    val uohItemAdapter = this?.adapter as? UohItemAdapter

    if (uohItemAdapter == null) {
        val detailMessage = "Adapter is not ${UohItemAdapter::class.java.simpleName}"
        throw AssertionError(detailMessage)
    }

    return uohItemAdapter
}