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
 * This Activity will be deleted in the future.
 */

@Deprecated("Unused class")
class PlayBroadcastSummaryActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = PlayBroadcastSummaryFragment.newInstance()

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, PlayBroadcastSummaryActivity::class.java)
    }
}