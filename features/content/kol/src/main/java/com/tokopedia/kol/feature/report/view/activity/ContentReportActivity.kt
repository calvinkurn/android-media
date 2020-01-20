package com.tokopedia.kol.feature.report.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.report.view.fragment.ContentReportFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportActivity : BaseSimpleActivity() {

    companion object {
        const val PARAM_CONTENT_ID = "content_id"
        const val RESULT_SUCCESS = "result_success"
        const val RESULT_ERROR_MSG = "error_msg"

        @Deprecated("Use ApplinkConstInternalContent")
        fun createIntent(context: Context, contentId: Int): Intent {
            val intent = Intent(context, ContentReportActivity::class.java)
            intent.putExtra(PARAM_CONTENT_ID, contentId)
            return intent
        }
    }
 
    override fun getNewFragment(): Fragment {
        val contentId = intent.data?.lastPathSegment?.toIntOrZero() ?: 0
        return ContentReportFragment.createInstance(contentId)
    }
}