package com.tokopedia.interestpick.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.interestpick.view.fragment.InterestPickFragment
import com.tokopedia.interestpick.view.listener.InterestPickContract

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickActivity : BaseSimpleActivity() {

    private lateinit var fragmentView: InterestPickContract.View

    companion object {
        fun createIntent(context: Context) = Intent(context, InterestPickActivity::class.java)
    }

    object DeeplinkIntent {
        @JvmStatic
        @DeepLink(ApplinkConst.INTEREST_PICK)
        fun createIntent(context: Context, extras: Bundle) = Companion.createIntent(context)
    }

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