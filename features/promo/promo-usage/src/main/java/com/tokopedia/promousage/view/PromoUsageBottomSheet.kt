package com.tokopedia.promousage.view

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.promousage.view.adapter.PromoUsageAdapter
import com.tokopedia.promousage.view.adapter.PromoUsageDiffUtilCallback
import com.tokopedia.unifycomponents.BottomSheetUnify

class PromoUsageBottomSheet: BottomSheetUnify() {

    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderContent()
    }

    private fun renderContent() {
        val asyncDifferConfig = AsyncDifferConfig.Builder(PromoUsageDiffUtilCallback()).build()
        val adapter = PromoUsageAdapter(asyncDifferConfig, object : PromoUsageAdapter.Listener {
            // TODO: Implementation
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listener?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}

interface Listener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
