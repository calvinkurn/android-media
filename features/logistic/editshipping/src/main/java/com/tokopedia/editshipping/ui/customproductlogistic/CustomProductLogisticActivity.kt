package com.tokopedia.editshipping.ui.customproductlogistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class CustomProductLogisticActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var fragment: CustomProductLogisticFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = CustomProductLogisticFragment.newInstance(bundle ?: Bundle())
        }
        return fragment
    }

}