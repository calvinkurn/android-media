package com.tokopedia.notifcenter.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Menus
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.R.string.notif_no_info_desc
import com.tokopedia.notifcenter.analytics.NotifCenterAnalytics
import com.tokopedia.notifcenter.di.DaggerNotifCenterComponent
import com.tokopedia.notifcenter.view.activity.NotifCenterActivity
import com.tokopedia.notifcenter.view.adapter.NotifCenterAdapter
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactoryImpl
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.presenter.NotifCenterPresenter
import com.tokopedia.notifcenter.view.viewmodel.NotifFilterViewModel
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel
import kotlinx.android.synthetic.main.fragment_notif_center.*
import javax.inject.Inject

/**
 * @author by alvinatin on 21/08/18.
 */

class NotifCenterFragment : BaseDaggerFragment(), NotifCenterContract.View {

    @Inject
    lateinit var presenter: NotifCenterPresenter

    @Inject
    lateinit var analytics : NotifCenterAnalytics

    private lateinit var adapter: NotifCenterAdapter
    private lateinit var menuList: ArrayList<Menus.ItemMenus>
    var canLoadMore = false

    companion object {
        fun createInstance(extras: Bundle?) : NotifCenterFragment {
            val fragment = NotifCenterFragment()
            extras?.let {
                fragment.arguments = it
            }
            return fragment
        }
        const val LOAD_MORE_THRESHOLD = 2
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
        var hitSingle = false
        arguments?.let {
            it.getString(NotifCenterActivity.NOTIF_ID)?.let {
                hitSingle = true
                presenter.fetchSingleData(it)
            }
        }
        if (!hitSingle) {
            presenter.fetchData()
        }
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

    override fun onSuccessFetchData(visitables: List<Visitable<*>>, canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore

        if (canLoadMore && !visitables.isEmpty() && visitables.last() is NotifItemViewModel) {
            presenter.updatePage((visitables.last() as NotifItemViewModel).notifId)
        }

        if (!visitables.isEmpty() && visitables.first() is NotifItemViewModel
                && isTimeSummarySame(visitables.first() as NotifItemViewModel)) {
            (visitables.first() as NotifItemViewModel).showTimeSummary = false
        }
        adapter.addElement(visitables)

        if (adapter.getList().isEmpty()) {
            showEmpty()
        }
    }

    private fun showEmpty() {
        adapter.addEmpty(getString(
                R.string.notif_no_info),
                getString(notif_no_info_desc),
                R.drawable.ic_info_empty
        )
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
        filterBtn.show()
    }

    override fun openRedirectUrl(url: String, templateKey: String) {
        analytics.trackClickList(templateKey)
        activity?.let {
            RouteManager.route(it, url)
        }
    }

    private fun initVar() {
        adapter = NotifCenterAdapter(NotifCenterTypeFactoryImpl(this))
    }

    private fun initView() {
        notifRv.adapter = adapter
        notifRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = notifRv.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition + LOAD_MORE_THRESHOLD > adapter.itemCount
                        && canLoadMore
                        && !isLoading()) {
                    analytics.trackScrollToBottom()

                    presenter.fetchData()
                }
            }
        })
        swipeToRefresh.setOnRefreshListener {
            filterBtn.hide(false)
            resetCursor()
            adapter.clearAllElements()
            presenter.fetchData()
        }
        filterBtn.hide(false)
        filterBtn.setButton1OnClickListener {
            context?.let {
                Menus(it)
            }?.let { menus ->
                menus.itemMenuList = getMenuList()
                menus.setActionText(getString(R.string.notif_cancel))
                menus.setOnActionClickListener {
                    menus.dismiss()
                }
                menus.setOnItemMenuClickListener { itemMenus, _ ->
                    resetParam()
                    adapter.clearAllElements()
                    analytics.trackClickFilter(itemMenus.title)

                    when (itemMenus.title) {
                        NotifFilterViewModel.FILTER_BUYER_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_BUYER_ID)

                        NotifFilterViewModel.FILTER_SELLER_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_SELLER_ID)

                        NotifFilterViewModel.FILTER_FOR_YOU_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_FOR_YOU_ID)

                        NotifFilterViewModel.FILTER_PROMO_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_PROMO_ID)

                        NotifFilterViewModel.FILTER_INSIGHT_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_INSIGHT_ID)

                        NotifFilterViewModel.FILTER_FEATURE_UPDATE_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_FEATURE_UPDATE_ID)

                        NotifFilterViewModel.FILTER_EVENT_TEXT -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_EVENT_ID)
                        else -> presenter
                                .updateFilterId(NotifFilterViewModel.FILTER_ALL_ID)
                    }
                    setMenuSelected(itemMenus.title)
                    presenter.fetchData()
                    menus.dismiss()
                }

                menus.show()
            }
        }
    }

    private fun isLoading() = adapter.isLoading

    private fun resetCursor() = presenter.resetCursor()

    private fun resetParam() = presenter.resetParam()

    private fun isTimeSummarySame(item: NotifItemViewModel): Boolean {
        return !adapter.getList().isEmpty()
                && adapter.getList().last() is NotifItemViewModel
                && TextUtils.equals((adapter.getList().last() as NotifItemViewModel).timeSummary,
                item.timeSummary)
    }

    private fun getMenuList() : ArrayList<Menus.ItemMenus> {
        if (!::menuList.isInitialized) {
            val filterAllMenu = Menus.ItemMenus(NotifFilterViewModel.FILTER_ALL_TEXT)
            filterAllMenu.iconEnd = R.drawable.ic_check

            menuList = ArrayList<Menus.ItemMenus>()
            menuList.add(filterAllMenu)
            menuList.add(Menus.ItemMenus(NotifFilterViewModel.FILTER_FOR_YOU_TEXT))
            menuList.add(Menus.ItemMenus(NotifFilterViewModel.FILTER_PROMO_TEXT))
            menuList.add(Menus.ItemMenus(NotifFilterViewModel.FILTER_INSIGHT_TEXT))
            menuList.add(Menus.ItemMenus(NotifFilterViewModel.FILTER_FEATURE_UPDATE_TEXT))
            menuList.add(Menus.ItemMenus(NotifFilterViewModel.FILTER_EVENT_TEXT))
        }
        return menuList
    }

    private fun setMenuSelected(selectedText: String) {
        for (item in menuList) {
            item.iconEnd = if (item.title.equals(selectedText)) R.drawable.ic_check else 0
        }
    }
}