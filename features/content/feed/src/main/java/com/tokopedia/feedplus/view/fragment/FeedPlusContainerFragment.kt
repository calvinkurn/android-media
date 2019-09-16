package com.tokopedia.feedplus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliatecommon.DISCOVERY_BY_ME
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedContainerComponent
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.feedplus.view.viewmodel.FeedPlusTabItem
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel
import com.tokopedia.kolcommon.data.pojo.Author
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feed_plus_container.*
import kotlinx.android.synthetic.main.partial_feed_error.*

import javax.inject.Inject

/**
 * @author by milhamj on 25/07/18.
 */

class FeedPlusContainerFragment : BaseDaggerFragment(), FragmentListener, AllNotificationListener {

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var isFabExpanded = false

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    internal lateinit var affiliatePreference: AffiliatePreference

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
        hideAllFab(true)
        if (!userSession.isLoggedIn) {
            fab_feed.show()
            fab_feed.setOnClickListener { v -> onGoToLogin() }
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
        val feedData = data.feedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE || it.type == FeedTabs.TYPE_CUSTOM }
        pagerAdapter.setItemList(feedData)
        view_pager.currentItem = if (data.meta.selectedIndex < feedData.size) data.meta.selectedIndex else 0
        feed_loading.visibility = View.GONE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.VISIBLE
        view_pager.visibility = View.VISIBLE

        if (hasCategoryIdParam()) {
            goToExplore()
        }
        if (userSession.isLoggedIn) {
            viewModel.getWhitelist(object : FeedPlusContainerViewModel.OnGetWhitelistData {
                override fun onSuccessGetWhitelistData(whitelistDomain: WhitelistDomain) {
                    renderFab(whitelistDomain)
                }

                override fun onErrorGetWhitelistData(errString: String) {
                    error(errString)
                }
            })
        }
    }

    private fun renderFab(whitelistDomain: WhitelistDomain) {
        if (userSession.isLoggedIn && !whitelistDomain.authors.isEmpty()) {
            showFeedFab(whitelistDomain)
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

    override fun onPause() {
        super.onPause()
        hideAllFab(false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            hideAllFab(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = FeedPlusContainerFragment().apply { arguments = bundle }
    }

    private fun showFeedFab(whitelistDomain: WhitelistDomain) {
        fab_feed.show()
        isFabExpanded = true
        if (!whitelistDomain.authors.isEmpty() && whitelistDomain.authors.size != 1) {
            fab_feed.setOnClickListener(fabClickListener(whitelistDomain))
        } else {
            val author = whitelistDomain.authors[0]
            fab_feed.setOnClickListener { v -> onGoToLink(author.link) }
        }
    }

    private fun fabClickListener(whitelistDomain: WhitelistDomain): View.OnClickListener {
        return View.OnClickListener {
            if (isFabExpanded) {
                hideAllFab(false)
            } else {
                fab_feed.animation = AnimationUtils.loadAnimation(activity, R.anim.rotate_forward)
                layout_grey_popup.visibility = View.VISIBLE
                for (author in whitelistDomain.authors) {
                    if (author.title.equals(Author.KEY_POST_TOKO, ignoreCase = true)) {
                        fab_feed_shop.show()
                        text_fab_shop.visibility = View.VISIBLE
                        fab_feed_shop.setOnClickListener { v1 -> onGoToLink(author.link) }
                    } else {
                        fab_feed_byme.show()
                        text_fab_byme.visibility = View.VISIBLE
                        fab_feed_byme.setOnClickListener { v12 -> goToCreateAffiliate(author.link) }
                    }
                }
                layout_grey_popup.setOnClickListener { v3 -> hideAllFab(false) }
                isFabExpanded = true
            }
        }
    }

    private fun goToCreateAffiliate(link: String) {
        if (context != null) {
            if (affiliatePreference.isFirstTimeEducation(userSession.userId)) {

                val intent = RouteManager.getIntent(
                        context,
                        ApplinkConst.DISCOVERY_PAGE.replace("{page_id}", DISCOVERY_BY_ME)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                affiliatePreference.setFirstTimeEducation(userSession.userId)

            } else {
                RouteManager.route(context, ApplinkConst.AFFILIATE_CREATE_POST, "-1", "-1")
            }
        }
    }

    private fun hideAllFab(isInitial: Boolean) {
        if (activity == null) {
            return
        }

        if (isInitial) {
            fab_feed.hide()
        } else {
            fab_feed.animation = AnimationUtils.loadAnimation(activity, R.anim.rotate_backward)
        }
        fab_feed_byme.hide()
        fab_feed_shop.hide()
        text_fab_byme.visibility = View.GONE
        text_fab_shop.visibility = View.GONE
        layout_grey_popup.visibility = View.GONE
        isFabExpanded = false
    }

    fun onGoToLink(link: String) {
        if (!TextUtils.isEmpty(link)) {
            RouteManager.route(activity, link)
        }
    }

    fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            activity!!.startActivityForResult(intent, FeedPlusFragment.REQUEST_LOGIN)
        }
    }
}
