package com.tokopedia.topads.dashboard.view.fragment.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import kotlinx.android.synthetic.main.topads_dash_insight_empty_state.*

/**
 * Created by Pika on 20/7/20.
 */

class TopAdsInsightBaseProductFragment : BaseDaggerFragment() {
    override fun getScreenName(): String {
        return TopAdsInsightBaseProductFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_insight_empty_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image_empty.setImageDrawable(context?.getResDrawable(R.drawable.topads_empty_insight_img))
    }

}