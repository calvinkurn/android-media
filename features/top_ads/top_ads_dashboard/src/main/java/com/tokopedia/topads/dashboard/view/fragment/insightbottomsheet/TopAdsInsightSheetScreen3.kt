package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Pika on 21/7/20.
 */

class TopAdsInsightSheetScreen3 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_insight_bottomsheet_page_3), container, false)
    }

}