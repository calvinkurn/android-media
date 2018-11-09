package com.tokopedia.kol.feature.report.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity

/**
 * @author by milhamj on 08/11/18.
 */
class ContentReportFragment : BaseDaggerFragment() {

    var contentId = 0

    companion object {
        fun createInstance(contentId: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(ContentReportActivity.PARAM_CONTENT_ID, contentId)

            val fragment = ContentReportFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName() = null

    override fun initInjector() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
    }

    private fun initVar() {
        arguments?.run {
            contentId = getInt(ContentReportActivity.PARAM_CONTENT_ID, 0)
        }
    }
}