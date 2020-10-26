package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.inboxcommon.InboxCommonFragment
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import javax.inject.Inject

class NotificationFragment : BaseListFragment<Visitable<*>, NotificationTypeFactory>(), InboxCommonFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
    }

    override fun getScreenName(): String = "Notification"
    override fun getAdapterTypeFactory() = NotificationTypeFactoryImpl()
    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        viewModel.loadNotification(page)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onRoleChanged(role: Int) {
        Toast.makeText(context, "Role Changed, $role", Toast.LENGTH_SHORT).show()
    }

    override fun initInjector() {
        DaggerNotificationComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .commonModule(context?.let { CommonModule(it) })
                .build()
                .inject(this)
    }
}