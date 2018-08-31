package com.tokopedia.notifcenter.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.notifcenter.NotifCenterRouter
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.DaggerNotifCenterComponent
import com.tokopedia.notifcenter.view.adapter.NotifCenterAdapter
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactoryImpl
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.presenter.NotifCenterPresenter
import kotlinx.android.synthetic.main.fragment_notif_center.*
import javax.inject.Inject

/**
 * @author by alvinatin on 21/08/18.
 */

class NotifCenterFragment : BaseDaggerFragment(), NotifCenterContract.View {

    @Inject
    lateinit var presenter: NotifCenterPresenter
    lateinit var adapter: NotifCenterAdapter
    lateinit var notifCenterRouter: NotifCenterRouter

    companion object {
        fun createInstance() = NotifCenterFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notif_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
        presenter.attachView(this)
        presenter.fetchData()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun getScreenName() = null

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerNotifCenterComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }

    override fun onSuccessFetchData(visitables: List<Visitable<*>>) {
        adapter.addElement(visitables)
    }

    override fun onErrorFetchData(message: String) {
        if (adapter.itemCount == 0) {
            swipeToRefresh.isEnabled = false
            NetworkErrorHelper.showEmptyState(context, container, message, {
                swipeToRefresh.isEnabled = true
                adapter.clearAllElements()
                presenter.fetchData()
            })
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, message, {
                presenter.fetchData()
            }).showRetrySnackbar()
        }
    }

    override fun showRefreshing() {
        swipeToRefresh.isRefreshing = true
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun hideLoading() {
        if (adapter.isLoading()) adapter.hideLoading()
        if (swipeToRefresh.isRefreshing) swipeToRefresh.isRefreshing = false
    }

    override fun openRedirectUrl(url: String) {
        activity?.let {
            notifCenterRouter.openRedirectUrl(it, url)
        }
    }

    private fun initVar() {
        if (activity?.application is NotifCenterRouter) {
            notifCenterRouter = activity?.application as NotifCenterRouter
        } else {
            throw IllegalStateException("Application must be an instance of "
                    .plus(NotifCenterRouter::class.java.simpleName))
        }
        adapter = NotifCenterAdapter(NotifCenterTypeFactoryImpl(this))
    }

    private fun initView() {
        notifRv.adapter = adapter
        swipeToRefresh.setOnRefreshListener {
            adapter.clearAllElements()
            presenter.fetchData()
        }
    }
}