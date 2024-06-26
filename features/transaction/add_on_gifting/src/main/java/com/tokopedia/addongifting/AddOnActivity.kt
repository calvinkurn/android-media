package com.tokopedia.addongifting

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnBottomSheet
import com.tokopedia.addongifting.addonunavailablebottomsheet.AddOnUnavailableBottomSheet
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData

class AddOnActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.add_on_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()

        intent.extras?.let {
            val addOnProductData = it.getParcelable(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA)
                    ?: AddOnProductData()
            val source = it.getString(AddOnConstant.EXTRA_ADD_ON_SOURCE) ?: ""
            when (addOnProductData.bottomSheetType) {
                AddOnProductData.ADD_ON_BOTTOM_SHEET -> {
                    val addOnBottomSheet = AddOnBottomSheet().apply {
                        this.addOnProductData = addOnProductData
                        this.source = source
                    }
                    addOnBottomSheet.show(supportFragmentManager, "")
                }
                AddOnProductData.ADD_ON_UNAVAILABLE_BOTTOM_SHEET -> {
                    val addOnUnavailableBottomSheet = AddOnUnavailableBottomSheet().apply {
                        this.addOnProductData = addOnProductData
                    }
                    addOnUnavailableBottomSheet.show(supportFragmentManager, "")
                }
                else -> {
                    finish()
                }
            }
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
