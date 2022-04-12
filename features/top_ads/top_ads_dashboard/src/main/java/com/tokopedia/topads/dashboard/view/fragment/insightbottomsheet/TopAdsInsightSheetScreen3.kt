package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.topads_insight_bottomsheet_page_1.view.*


/**
 * Created by Pika on 21/7/20.
 */

class TopAdsInsightSheetScreen3 : Fragment() {

    private var image: ImageUnify? = null
    private var stepIndicator: ImageUnify? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_insight_bottomsheet_page_3),
            container, false)
        image = view.findViewById(R.id.img)
        stepIndicator = view.findViewById(R.id.step_indicator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        image?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_dash_insight_page3))
        stepIndicator?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_indi_3))
    }

}