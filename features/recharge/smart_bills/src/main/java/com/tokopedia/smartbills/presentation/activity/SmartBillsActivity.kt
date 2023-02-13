package com.tokopedia.smartbills.presentation.activity

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment


class SmartBillsActivity : BaseSimpleActivity(), HasComponent<SmartBillsComponent> {

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras
        val sourceType = bundle?.getString(PARAM_SOURCE_TYPE) ?: ""
        val message = bundle?.getString(EXTRA_ADD_BILLS_MESSAGE) ?: ""
        val category = bundle?.getString(EXTRA_ADD_BILLS_CATEGORY) ?: ""
        return SmartBillsFragment.newInstance(sourceType, message, category)
    }

    override fun getComponent(): SmartBillsComponent {
        return DaggerSmartBillsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_tooltip, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ?: "" == R.id.action_menu_tooltip) {
            onClickToolTip()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickToolTip() {
        if (fragment is SbmActivityListener) {
            (fragment as SbmActivityListener).clickToolTip()
        }
    }

    companion object {
        const val PARAM_SOURCE_TYPE = "source"
        const val EXTRA_ADD_BILLS_MESSAGE = "MESSAGE"
        const val EXTRA_ADD_BILLS_CATEGORY = "CATEGORY"
    }

    interface SbmActivityListener {
        fun clickToolTip()
    }
}