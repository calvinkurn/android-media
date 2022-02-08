package com.tokopedia.gifting.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.gifting.presentation.bottomsheet.GiftingBottomSheet
import com.tokopedia.product_service_widget.R

class GiftingBottomsheetActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifting_bottomsheet)
        GiftingBottomSheet().show(supportFragmentManager, "")
    }
}