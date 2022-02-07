package com.tokopedia.gifting.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.product_service_common.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.internal.findRootView

class GiftingBottomsheetActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifting_bottomsheet)
        Toaster.build(findRootView(this), "hoho", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }
}