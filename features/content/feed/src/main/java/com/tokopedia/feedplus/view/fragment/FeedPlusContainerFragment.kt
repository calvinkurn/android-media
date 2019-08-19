package com.tokopedia.feedplus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedContainerComponent
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_feed_plus_container.*
import kotlinx.android.synthetic.main.partial_feed_error.*

import javax.inject.Inject

/**
 * @author by milhamj on 25/07/18.
 */

class FeedPlusContainerFragment : BaseDaggerFragment(), FragmentListener, AllNotificationListener {

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FeedPlusContainerViewModel::class.java]
    }

    private val pagerAdapter : FeedPlusTabAdapter by lazy {
        FeedPlusTabAdapter(childFragmentManager, emptyList(), arguments)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.tabResp.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetTab(it.data)
                is Fail -> onErrorGetTab(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_plus_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        activity?.let { status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it) }
        requestFeedTab()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerFeedContainerComponent.builder()
                .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build().inject(this)
    }

    override fun onScrollToTop() {
        try {
            val fragment = pagerAdapter.getRegisteredFragment(view_pager.currentItem)
            if (fragment is FeedPlusFragment) {
                fragment.scrollToTop()
            } else if (fragment is ContentExploreFragment) {
                fragment.scrollToTop()
            }
        } catch (e: IllegalStateException) {
            //no op
        }
    }

    private fun initView() {
        //status bar background compability
        activity?.let { status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it) }
        status_bar_bg.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }

        setAdapter()
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox) // notify badge after toolbar created
    }

    private fun requestFeedTab(){
        showLoading()
        viewModel.getDynamicTabs()
    }

    private fun showLoading() {
        feed_loading.visibility = View.VISIBLE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.INVISIBLE
        view_pager.visibility = View.INVISIBLE
    }

    private fun onErrorGetTab(throwable: Throwable) {
        message_retry.text = ErrorHandler.getErrorMessage(context, throwable)
        button_retry.setOnClickListener { requestFeedTab() }

        feed_loading.visibility = View.GONE
        feed_error.visibility = View.VISIBLE
        tab_layout.visibility = View.INVISIBLE
        view_pager.visibility = View.INVISIBLE
    }

    private fun onSuccessGetTab(data: FeedTabs) {
        val feedData = data.feedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE }
        pagerAdapter.setItemList(feedData)
        view_pager.currentItem = if (data.meta.selectedIndex < feedData.size) data.meta.selectedIndex else 0
        feed_loading.visibility = View.GONE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.VISIBLE
        view_pager.visibility = View.VISIBLE

        if (hasCategoryIdParam()) {
            goToExplore()
        }
    }

    private fun setAdapter() {
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    @JvmOverloads
    fun goToExplore(shouldResetCategory: Boolean = false) {
        if (canGoToExplore()) {
            view_pager.currentItem = pagerAdapter.contentExploreIndex

            if (shouldResetCategory) {
                pagerAdapter.contentExplore?.onCategoryReset()
            }
        }
    }

    private fun hasCategoryIdParam(): Boolean {
        return !arguments?.getString(ContentExploreFragment.PARAM_CATEGORY_ID).isNullOrBlank()
    }

    private fun canGoToExplore(): Boolean {
        return pagerAdapter.isContextExploreExist
    }

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        toolbar?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
        }
        this.badgeNumberNotification = notificationCount
        this.badgeNumberInbox = inboxCount
    }

    override fun onDestroy() {
        viewModel.tabResp.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = FeedPlusContainerFragment().apply { arguments = bundle }
    }
}
