package com.tokopedia.kol.feature.report.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kol.feature.report.view.fragment.ContentReportFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportActivity : BaseSimpleActivity() {
    private var contentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        super.onCreate(savedInstanceState)
    }

    private fun getDataFromIntent() {
        intent.data?.let {
            contentId = it.lastPathSegment.toIntOrZero()
        }
    }

    companion object {
        const val PARAM_CONTENT_ID = "content_id"
        const val RESULT_SUCCESS = "result_success"
        const val RESULT_ERROR_MSG = "error_msg"
    }

    override fun getNewFragment(): Fragment {
        return ContentReportFragment.createInstance(contentId)
    }
}