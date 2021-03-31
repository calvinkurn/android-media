package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.permission.PermissionCheckerHelper

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressActivity: BaseSimpleActivity() {

    override fun getLayoutRes(): Int = R.layout.activity_add_edit_new_address

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): AddEditAddressFragment? {
        var fragment: AddEditAddressFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = AddEditAddressFragment.newInstance(bundle?: Bundle())
        }
        return fragment
    }

    override fun getParentViewResourceID(): Int = R.id.add_new_address_parent

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("++masukk")
    }

    override fun onBackPressed() {
        val fragment =
                this.supportFragmentManager.findFragmentById(R.id.add_new_address_parent)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}