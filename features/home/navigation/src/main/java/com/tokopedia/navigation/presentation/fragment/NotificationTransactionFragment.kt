package com.tokopedia.navigation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.adapter.NotificationTransactionAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl

class NotificationTransactionFragment: BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_transaction, container, false)
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationTransactionFactoryImpl()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        if (adapterTypeFactory !is NotificationTransactionFactory) throw IllegalStateException()
        val typeFactory = adapterTypeFactory as NotificationTransactionFactoryImpl
        return NotificationTransactionAdapter(typeFactory)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        renderList(TestData.getData(context), false)
    }

    override fun onItemClicked(t: Visitable<*>?) {}
    override fun getScreenName() = ""

}