package com.tokopedia.topads.dashboard.view.fragment.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.credit_history.CreditHistory
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditHistoryTypeFactory

class TopAdsCreditHistoryFragment: BaseListFragment<CreditHistory, TopAdsCreditHistoryTypeFactory>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topads_credit_history, container, false)
    }

    override fun getAdapterTypeFactory() =  TopAdsCreditHistoryTypeFactory()

    override fun onItemClicked(t: CreditHistory?) {

    }

    override fun loadData(page: Int) {

    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }
}