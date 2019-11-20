package com.tokopedia.navigation.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.navigation.R
import com.tokopedia.navigation.data.consts.buyerMenu
import com.tokopedia.navigation.data.consts.sellerMenu
import com.tokopedia.navigation.data.mapper.NotificationMapper
import com.tokopedia.navigation.presentation.adapter.NotificationTransactionAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationTransactionComponent
import com.tokopedia.navigation.presentation.viewmodel.NotificationTransactionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationTransactionFragment: BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelFragmentProvider.get(NotificationTransactionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNotification()
        viewModel.notification.observe(this, Observer {
            when(it) {
                is Success -> {
                    if (NotificationMapper.isHasShop(it.data)) {
                        adapter.addElement(sellerMenu())
                    }
                }
                is Fail -> {

                }
            }
        })
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

    override fun initInjector() {
        DaggerNotificationTransactionComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {
        renderList(buyerMenu(), false)
        (adapter as NotificationTransactionAdapter).updateValue()
    }

    override fun onItemClicked(t: Visitable<*>?) {}
    override fun getScreenName() = ""

}