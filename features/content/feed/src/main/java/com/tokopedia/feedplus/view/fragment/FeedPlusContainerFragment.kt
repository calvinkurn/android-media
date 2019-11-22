package com.tokopedia.feedplus.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.DISCOVERY_BY_ME
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedContainerComponent
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
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

    companion object {
        const val TOOLBAR_GRADIENT = 1
        const val TOOLBAR_WHITE = 2
        @JvmStatic
        fun newInstance(bundle: Bundle?) = FeedPlusContainerFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    internal lateinit var affiliatePreference: AffiliatePreference

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FeedPlusContainerViewModel::class.java]
    }

    private val pagerAdapter: FeedPlusTabAdapter by lazy {
        FeedPlusTabAdapter(childFragmentManager, emptyList(), arguments)
    }

    private val coachMark: CoachMark by lazy {
        CoachMarkBuilder()
                .allowPreviousButton(false)
                .build()
    }

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var isFabExpanded = false
    private var toolbarType = TOOLBAR_GRADIENT
    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0



    private lateinit var coachMarkItem:  CoachMarkItem
    private lateinit var feedBackgroundCrossfader: TransitionDrawable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.tabResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetTab(it.data)
                is Fail -> onErrorGetTab(it.throwable)
            }
        })
        viewModel.whitelistResp.observe(this, Observer {
            when (it) {
                is Success -> renderFab(it.data)
                is Fail -> onErrorGetWhitelist(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_plus_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
            status_bar_bg2.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        initView()
        requestFeedTab()
    }

    override fun onPause() {
        super.onPause()
        hideAllFab(false)
    }

    override fun onDestroy() {
        viewModel.tabResp.removeObservers(this)
        viewModel.whitelistResp.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerFeedContainerComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
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

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        toolbar?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
        }
        this.badgeNumberNotification = notificationCount
        this.badgeNumberInbox = inboxCount
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            hideAllFab(false)
        }
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

    private fun initView() {
        //status bar background compability
        setFeedBackgroundCrossfader()
        activity?.let {
            status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
            status_bar_bg2.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        status_bar_bg.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }
        status_bar_bg2.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.INVISIBLE
            else -> View.GONE
        }

        hideAllFab(true)
        if (!userSession.isLoggedIn) {
            fab_feed.show()
            fab_feed.setOnClickListener { onGoToLogin() }
        }
        setAdapter()
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox) // notify badge after toolbar created
        feed_appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (verticalOffset + toolbar.height < 0) {
                    showNormalTextWhiteToolbar()
                } else {
                    showWhiteTextTransparentToolbar()
                }
            }
        })

    }

    private fun requestFeedTab() {
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
        view_pager.offscreenPageLimit = pagerAdapter.count
        feed_loading.visibility = View.GONE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.VISIBLE
        view_pager.visibility = View.VISIBLE

        if (hasCategoryIdParam()) {
            goToExplore()
        }
        if (userSession.isLoggedIn) {
            viewModel.getWhitelist()
        }
    }

    private fun renderFab(whitelistDomain: WhitelistDomain) {
        if (userSession.isLoggedIn && whitelistDomain.authors.isNotEmpty()) {
            showFeedFab(whitelistDomain)
        }
    }

    private fun onErrorGetWhitelist(throwable: Throwable) {
        ToasterError.make(view_pager,
                ErrorHandler.getErrorMessage(context, throwable),
                BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.title_try_again)) {
                    if (userSession.isLoggedIn) {
                        viewModel.getWhitelist()
                    }
                }

    }

    private fun setFeedBackgroundCrossfader() {
        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.dp_50)
        startToTransitionOffset = status_bar_bg.layoutParams.height + resources.getDimensionPixelSize(R.dimen.dp_100)
        activity?.let {
            val feedBackgroundGradient = MethodChecker.getDrawable(it, R.drawable.gradient_feed)
            val feedBackgroundWhite = MethodChecker.getDrawable(it, R.drawable.gradient_feed_white)
            feedBackgroundCrossfader = TransitionDrawable(arrayOf<Drawable>(feedBackgroundGradient, feedBackgroundWhite))
        }
        feed_background_image.setImageDrawable(feedBackgroundCrossfader)
        feedBackgroundCrossfader.startTransition(0)
    }


    private fun showNormalTextWhiteToolbar() {
        if (toolbarType != TOOLBAR_GRADIENT) {
            feedBackgroundCrossfader.reverseTransition(200)
            toolbarType = TOOLBAR_GRADIENT
            status_bar_bg2.visibility = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
                else -> View.GONE
            }
            activity?.let {
                tab_layout.setSelectedTabIndicatorColor(MethodChecker.getColor(activity, R.color.tkpd_main_green))
                tab_layout.setTabTextColors(MethodChecker.getColor(activity, R.color.font_black_disabled_38), MethodChecker.getColor(activity, R.color.tkpd_main_green))
            }
        }
    }

    private fun showWhiteTextTransparentToolbar() {
        if (toolbarType != TOOLBAR_WHITE) {
            feedBackgroundCrossfader.reverseTransition(200)
            toolbarType = TOOLBAR_WHITE
            status_bar_bg2.visibility = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.INVISIBLE
                else -> View.GONE
            }
            activity?.let {
                tab_layout.setSelectedTabIndicatorColor(MethodChecker.getColor(activity, R.color.white))
                tab_layout.setTabTextColors(MethodChecker.getColor(activity, R.color.white), MethodChecker.getColor(activity, R.color.white))
            }
        }
    }

    private fun setAdapter() {
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun hasCategoryIdParam(): Boolean {
        return !arguments?.getString(ContentExploreFragment.PARAM_CATEGORY_ID).isNullOrBlank()
    }

    private fun canGoToExplore(): Boolean {
        return pagerAdapter.isContextExploreExist
    }

    private fun showFeedFab(whitelistDomain: WhitelistDomain) {
        fab_feed.show()
        isFabExpanded = true
        if (whitelistDomain.authors.size > 1) {
            fab_feed.setOnClickListener(fabClickListener(whitelistDomain))
        } else if (whitelistDomain.authors.size == 1) {
            val author = whitelistDomain.authors.first()
            fab_feed.setOnClickListener { onGoToLink(author.link) }
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
                    if (author.type.equals(Author.TYPE_AFFILIATE, ignoreCase = true)) {
                        fab_feed_byme.show()
                        text_fab_byme.visibility = View.VISIBLE
                        text_fab_byme.text = author.title
                        fab_feed_byme.setOnClickListener { goToCreateAffiliate() }
                    } else {
                        fab_feed_shop.show()
                        text_fab_shop.visibility = View.VISIBLE
                        text_fab_shop.text = author.title
                        fab_feed_shop.setOnClickListener { onGoToLink(author.link) }
                    }
                }
                layout_grey_popup.setOnClickListener { hideAllFab(false) }
                isFabExpanded = true
            }
        }
    }

    private fun goToCreateAffiliate() {
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

    fun hideAllFab(isInitial: Boolean) {
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

    private fun onGoToLink(link: String) {
        if (!TextUtils.isEmpty(link)) {
            RouteManager.route(activity, link)
        }
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            it.startActivityForResult(intent, FeedPlusFragment.REQUEST_LOGIN)
        }
    }

    fun showCreatePostOnBoarding() {
        fab_feed.addOneTimeGlobalLayoutListener {
            val x1: Int = fab_feed.x.toInt()
            val y1: Int = fab_feed.y.toInt()
            val x2: Int = x1 + fab_feed.width
            val y2: Int = y1 + fab_feed.height

            coachMarkItem = CoachMarkItem(
                    fab_feed,
                    getString(R.string.feed_onboarding_create_post_title),
                    getString(R.string.feed_onboarding_create_post_detail)
            ).withCustomTarget(intArrayOf(x1, y1, x2, y2))

            showFabCoachMark()
        }
    }

    private fun showFabCoachMark() {
        if (::coachMarkItem.isInitialized && !affiliatePreference.isCreatePostEntryOnBoardingShown(userSession.userId)) {
            coachMark.show(activity = activity, tag = null, tutorList = arrayListOf(coachMarkItem))
            affiliatePreference.setCreatePostEntryOnBoardingShown(userSession.userId)
        }
    }
}
