package com.tokopedia.notifcenter.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter
import com.tokopedia.notifcenter.presentation.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.fragment.bottomsheet.BottomSheetFactory
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.notifcenter.widget.NotificationFilterView
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationFragment : BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
        InboxFragment, NotificationItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var rv: VerticalRecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var filter: NotificationFilterView? = null
    private var containerListener: InboxFragmentContainer? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "Notification"
    override fun getAdapterTypeFactory() = NotificationTypeFactoryImpl(this)
    override fun onItemClicked(t: Visitable<*>?) {}

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    override fun loadData(page: Int) {
        viewModel.loadNotification(page, containerListener?.role)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)?.also {
            initView(it)
            setupObserver()
            setupRecyclerView()
            setupFilter()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, NotificationTypeFactory> {
        rvAdapter = NotificationAdapter(adapterTypeFactory)
        return rvAdapter as NotificationAdapter
    }

    private fun initView(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        filter = view.findViewById(R.id.sv_filter)
    }

    private fun setupObserver() {
        viewModel.notificationItems.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                renderList(it.data, false)
            }
        })
    }

    private fun setupRecyclerView() {
        val itemDecoration = NotificationItemDecoration(context)
        rv?.clearItemDecoration()
        rv?.addItemDecoration(itemDecoration)
    }

    private fun setupFilter() {
        filter?.filterListener = object : NotificationFilterView.FilterListener {
            override fun onFilterChanged(@NotificationFilterType filterType: Int) {
                viewModel.filter = filterType
                loadInitialData()
            }
        }
    }

    override fun onRoleChanged(role: Int) {
        loadInitialData()
    }

    override fun initInjector() {
        DaggerNotificationComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .commonModule(context?.let { CommonModule(it) })
                .build()
                .inject(this)
    }

    override fun showLongerContent(element: NotificationUiModel) {
        BottomSheetFactory.showLongerContent(childFragmentManager, element)
    }
}