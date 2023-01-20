package com.tokopedia.topads.credit.history.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.credit.history.view.fragment.TopAdsCreditHistoryFragment
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent

const val PARAM_DATE_PICKER_INDEX = "picker_index"
const val IS_SHOW_OLD_FLOW = "is_show_old_flow"

class TopAdsCreditHistoryActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {
    override fun getComponent(): TopAdsDashboardComponent =
        DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()

    override fun getNewFragment() = TopAdsCreditHistoryFragment.createInstance(
        intent.getBooleanExtra(PARAM_IS_FROM_SELECTION, false),
        intent.getBooleanExtra(IS_SHOW_OLD_FLOW, true),
        intent.getIntExtra(PARAM_DATE_PICKER_INDEX, 0)
    )

    companion object {
        private const val PARAM_IS_FROM_SELECTION = "is_from_selection"
        fun createInstance(
            context: Context, isFromSelection: Boolean = false, showAutoTopUpOldFlow: Boolean = true, datePickerIndex: Int
        ) = Intent(context, TopAdsCreditHistoryActivity::class.java)
                .putExtra(PARAM_IS_FROM_SELECTION, isFromSelection)
                .putExtra(PARAM_DATE_PICKER_INDEX, datePickerIndex)
                .putExtra(IS_SHOW_OLD_FLOW, showAutoTopUpOldFlow)
    }
}
