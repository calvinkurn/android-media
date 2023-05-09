package com.tokopedia.topads.dashboard.recommendation.common.decoration

import android.content.Context
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration

class RecommendationInsightItemDecoration(context: Context?, orientation: Int) :
    DividerItemDecoration(context, orientation) {
    override fun shouldDrawOnLastItem(): Boolean {
        return false
    }
}
