package com.tokopedia.broadcaster.chucker.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.broadcaster.chucker.ui.fragment.NetworkChuckerFragment

class NetworkChuckerActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return NetworkChuckerFragment()
    }

    companion object {
        fun route(context: Context) {
            context.startActivity(Intent(context, NetworkChuckerActivity::class.java))
        }
    }

}