package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.topads_insight_bottomsheet_page_1.view.*


/**
 * Created by Pika on 21/7/20.
 */

class TopAdsInsightSheetScreen2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_insight_bottomsheet_page_2), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.img.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dash_insight_page2))
        view.step_indicator.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_indi_2))
    }

}