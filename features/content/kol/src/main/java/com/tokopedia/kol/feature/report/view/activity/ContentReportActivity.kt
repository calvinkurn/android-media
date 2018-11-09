package com.tokopedia.kol.feature.report.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.report.view.fragment.ContentReportFragment

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportActivity : BaseSimpleActivity() {

    companion object {
        const val PARAM_CONTENT_ID = "content_id"

        fun createIntent(context: Context, contentId: Int): Intent {
            val intent = Intent(context, ContentReportActivity::class.java)
            intent.putExtra(PARAM_CONTENT_ID, contentId)
            return intent
        }
    }
 
    override fun getNewFragment(): Fragment {
        val contentId = intent.extras?.getInt(PARAM_CONTENT_ID, 0) ?: 0
        return ContentReportFragment.createInstance(contentId)
    }
}