package com.tokopedia.gifting.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.gifting.presentation.bottomsheet.GiftingBottomSheet
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_service_widget.R

class GiftingBottomsheetActivity: BaseActivity() {

    companion object {
        private const val PRODUCT_ID_SEGMENT_INDEX: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifting_bottomsheet)
        GiftingBottomSheet(getProductIdFromUri()).apply {
            setOnDismissListener {
                finish()
            }
        }.show(supportFragmentManager, "")
    }

    private fun getProductIdFromUri(): Long {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.getOrNull(PRODUCT_ID_SEGMENT_INDEX).toLongOrZero()
    }
}