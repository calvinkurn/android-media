package com.tokopedia.play.broadcaster.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastSummaryFragment

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = PlayBroadcastSummaryFragment.newInstance()

    override fun getParentViewResourceID(): Int = R.id.play_summary_parent_view
    override fun getLayoutRes(): Int = R.layout.activity_play_broadcast_summary

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, PlayBroadcastSummaryActivity::class.java)
    }
}