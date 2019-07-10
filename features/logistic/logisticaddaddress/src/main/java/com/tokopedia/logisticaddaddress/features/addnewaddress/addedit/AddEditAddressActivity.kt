package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R

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
            fragment = AddEditAddressFragment.newInstance(bundle)
        }
        return fragment
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (fragment is AddEditAddressFragment) {
            if (intent?.extras != null) {
                val bundle = intent.extras
                AddEditAddressFragment.newInstance(bundle)
            }
        }
    }

    override fun onBackPressed() {
        val fragment =
                this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}