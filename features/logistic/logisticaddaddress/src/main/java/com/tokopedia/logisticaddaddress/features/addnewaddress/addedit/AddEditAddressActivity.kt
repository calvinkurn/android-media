package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressActivity: BaseSimpleActivity() {
    private val FINISH_FLAG = 1212

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }

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

    override fun onStop() {
        setResult(FINISH_FLAG)
        super.onStop()
    }

    override fun onDestroy() {
        setResult(FINISH_FLAG)
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fragment =
                this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}