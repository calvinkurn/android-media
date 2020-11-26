package com.tokopedia.interestpick.view.activity

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.interestpick.view.fragment.InterestPickFragment
import com.tokopedia.interestpick.view.listener.InterestPickContract

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickActivity : BaseSimpleActivity() {

    private lateinit var fragmentView: InterestPickContract.View

    override fun getNewFragment(): Fragment {
        val fragment = InterestPickFragment.createInstance()
        fragmentView = fragment
        return fragment
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
        fragmentView.onBackPressed()
        super.onBackPressed()
    }
}