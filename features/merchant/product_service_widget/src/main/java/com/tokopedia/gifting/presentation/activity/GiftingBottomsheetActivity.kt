package com.tokopedia.gifting.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.gifting.presentation.bottomsheet.GiftingBottomSheet
import com.tokopedia.product_service_widget.R

class GiftingBottomsheetActivity: BaseActivity() {

    companion object {
        private const val ADDON_ID_SEGMENT_INDEX: Int = 1
        private const val GIFTING_BS_ID = "gifting_bs_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifting_bottomsheet)
        GiftingBottomSheet(getAddonIdFromUri()).apply {
            setOnDismissListener {
                finish()
            }
        }.show(supportFragmentManager, GIFTING_BS_ID)
    }

    private fun getAddonIdFromUri(): String {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.getOrNull(ADDON_ID_SEGMENT_INDEX).orEmpty()
    }
}