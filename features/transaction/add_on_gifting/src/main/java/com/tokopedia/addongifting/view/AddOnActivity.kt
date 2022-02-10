package com.tokopedia.addongifting.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.addongifting.R
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData

class AddOnActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.add_on_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            val addOnProductData = it.getParcelable(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA)
                    ?: AddOnProductData()
            val addOnBottomSheet = AddOnBottomSheet(addOnProductData)
            addOnBottomSheet.show(supportFragmentManager, "")
        }
    }
}