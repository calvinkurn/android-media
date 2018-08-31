package com.tokopedia.notifcenter.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.notifcenter.view.fragment.NotifCenterFragment

/**
 * @author by alvinatin on 21/08/18.
 */
class NotifCenterActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, NotifCenterActivity::class.java)
    }

    override fun getNewFragment(): Fragment {
        return NotifCenterFragment.createInstance()
    }
}