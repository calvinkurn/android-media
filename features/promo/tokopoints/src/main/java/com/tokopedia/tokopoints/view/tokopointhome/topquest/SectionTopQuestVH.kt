package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.quest_widget.view.QuestWidgetView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CarouselItemDecorationNew
import com.tokopedia.tokopoints.view.model.homeresponse.RecommendationWrapper
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.tokopoints.view.util.convertDpToPixel

class SectionTopQuestVH(val view: View, val listener: RewardsRecomListener) : RecyclerView.ViewHolder(view) {

    fun bind(data: RewardsRecommendation) {
         val view:QuestWidgetView = view.findViewById(R.id.topquest)
         view.getQuestList(0,"","myreward")
    }
}