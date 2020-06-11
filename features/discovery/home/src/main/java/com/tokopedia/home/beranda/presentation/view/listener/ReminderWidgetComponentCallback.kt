package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderWidget

class ReminderWidgetComponentCallback (val context: Context?,val viewModel: HomeViewModel,
                                       val homeCategoryListener: HomeCategoryListener): ReminderWidgetListener{

    override fun onContentClickListener(applink: String, source: String, reminderWidget: ReminderWidget) {
    }

    override fun onDeclineClickListener(requestParams: Map<String, String>) {
        viewModel.declineRechargeRecommendationItem(requestParams)
    }

    override fun onImpressionListener(reminderData: ReminderData) {

    }
}