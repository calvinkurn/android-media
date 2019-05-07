package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-05-07.
 */
class AddNewAddressActivity: BaseSimpleActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_add_new_address
    }


    override fun getNewFragment(): Fragment? = null
}