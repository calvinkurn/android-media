package com.tokopedia.shop.open.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.R
import com.tokopedia.shop.open.view.fragment.ShopOpenPostalCodeChooserFragment

/**
 * Created by Yehezkiel on 22/05/18.
 */

class ShopOpenPostalCodeChooserActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    companion object {
        val ARGUMENT_DATA_POSTAL_CODE = "postal_code"
        fun createNewInstance(activity: Activity, postalCode: ArrayList<String>): Intent {
            val intent = Intent(activity, ShopOpenPostalCodeChooserActivity::class.java)
            intent.putStringArrayListExtra(ARGUMENT_DATA_POSTAL_CODE, postalCode)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val postalCode = intent.getStringArrayListExtra(ARGUMENT_DATA_POSTAL_CODE)
        return ShopOpenPostalCodeChooserFragment.newInstance(postalCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.elevation = 0f
            toolbar.setNavigationIcon(R.drawable.ic_close)
        }

    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }
}
