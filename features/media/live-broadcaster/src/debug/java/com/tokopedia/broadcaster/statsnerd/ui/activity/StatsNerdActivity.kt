package com.tokopedia.broadcaster.statsnerd.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.broadcaster.statsnerd.ui.fragment.main.NetworkStatsNerdFragment

class StatsNerdActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return NetworkStatsNerdFragment()
    }

    companion object {
        fun route(context: Context) {
            context.startActivity(Intent(context, StatsNerdActivity::class.java))
        }
    }

}