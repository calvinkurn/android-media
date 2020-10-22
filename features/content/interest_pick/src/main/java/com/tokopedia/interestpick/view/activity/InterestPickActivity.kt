package com.tokopedia.interestpick.view.activity

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.interestpick.view.fragment.InterestPickFragment

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return InterestPickFragment.createInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        (fragment as? InterestPickFragment)?.onBackPressed()
        super.onBackPressed()
    }
}