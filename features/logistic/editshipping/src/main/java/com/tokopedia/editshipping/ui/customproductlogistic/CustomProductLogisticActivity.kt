package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class CustomProductLogisticActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return CustomProductLogisticFragment()
    }


}