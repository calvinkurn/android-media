package com.tokopedia.shop_widget.operationalhour.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.operationalhour.view.bottomsheet.ShopOperationalHoursListBottomSheet

class ShopOperationalHourBottomSheetActivity : BaseSimpleActivity() {
    private var shopOperaHoursListBottomSheet: ShopOperationalHoursListBottomSheet? = null
    private var shopId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        configBottomSheet()
    }

    private fun getIntentData() {
        intent.data?.let {
            shopId = it.pathSegments.lastOrNull().orEmpty()
        }
    }

    private fun configBottomSheet() {
        shopOperaHoursListBottomSheet = ShopOperationalHoursListBottomSheet.createInstance(shopId)
        shopOperaHoursListBottomSheet?.apply {
            setOnDismissListener {
                finish()
            }
            show(supportFragmentManager)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_operational_hour_bottom_sheet
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}