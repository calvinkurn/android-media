package com.tokopedia.instantloan.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.instantloan.InstantLoanComponentInstance
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.data.model.response.BannerEntity
import com.tokopedia.instantloan.ddcollector.DDCollectorManager
import com.tokopedia.instantloan.ddcollector.PermissionResultCallback
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.HELP_URL
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.PAYMENT_METHODS_URL
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.SUBMISSION_HISTORY_URL
import com.tokopedia.instantloan.router.InstantLoanRouter
import com.tokopedia.instantloan.view.adapter.BannerPagerAdapter
import com.tokopedia.instantloan.view.adapter.InstantLoanPagerAdapter
import com.tokopedia.instantloan.view.contractor.BannerContractor
import com.tokopedia.instantloan.view.contractor.OnGoingLoanContractor
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment
import com.tokopedia.instantloan.view.fragment.DenganAgunanFragment
import com.tokopedia.instantloan.view.fragment.TanpaAgunanFragment
import com.tokopedia.instantloan.view.presenter.BannerListPresenter
import com.tokopedia.instantloan.view.presenter.OnGoingLoanPresenter
import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager
import com.tokopedia.instantloan.view.ui.InstantLoanItem
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class InstantLoanActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>, BannerContractor.View, OnGoingLoanContractor.View, DanaInstantFragment.ActivityInteractor, BannerPagerAdapter.BannerClick, View.OnClickListener {
    @Inject
    lateinit var mBannerPresenter: BannerListPresenter

    @Inject
    lateinit var onGoingLoanPresenter: OnGoingLoanPresenter

    @Inject
    lateinit var instantLoanAnalytics: InstantLoanAnalytics

    @Inject
    lateinit var userSession: UserSession

    private var mBannerPager: ViewPager? = null
    private var mBtnNextBanner: FloatingActionButton? = null
    private var mBtnPreviousBanner: FloatingActionButton? = null

    private var tabLayout: TabLayout? = null
    private var heightWrappingViewPager: HeightWrappingViewPager? = null
    private var activeTabPosition = 0
    private var instantLoanEnabled = true
    private var menu: Menu? = null
    private var onGoingLoanStatus = false
    private var onGoingLoanId: Int = 0
    private var menushown = false

    internal var instantLoanItemList: MutableList<InstantLoanItem> = ArrayList()

    private val mBannerPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            if (position == mBannerPager!!.adapter!!.count - 1) {
                mBtnNextBanner!!.hide()
                mBtnPreviousBanner!!.show()
            } else if (position == 0) {
                mBtnPreviousBanner!!.hide()
                mBtnNextBanner!!.show()
            } else {
                mBtnNextBanner!!.show()
                mBtnPreviousBanner!!.show()
            }
            sendBannerImpressionEvent(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }


    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initInjector()
        mBannerPresenter.attachView(this)
        onGoingLoanPresenter.attachView(this)
        initializeView()
        attachViewListener()
        setupToolbar()
        loadSection()
        mBannerPresenter.loadBanners()

        if (userSession != null && userSession.isLoggedIn) {
            onGoingLoanPresenter.checkUserOnGoingLoanStatus()
        }

    }

    private fun loadSection() {

        if (instantLoanEnabled) {
            populateThreeTabItem()
        } else {
            populateTwoTabItem()
        }
        val instantLoanPagerAdapter = InstantLoanPagerAdapter(supportFragmentManager)
        instantLoanPagerAdapter.setData(instantLoanItemList)
        heightWrappingViewPager!!.adapter = instantLoanPagerAdapter
        tabLayout!!.setupWithViewPager(heightWrappingViewPager)
        setActiveTab()
    }

    private fun populateTwoTabItem() {
        instantLoanItemList.add(InstantLoanItem(getPageTitle(1),
                getTanpaAgunanFragment(1)))
        instantLoanItemList.add(InstantLoanItem(getPageTitle(2),
                getDenganAngunanFragment(2)))

    }

    private fun populateThreeTabItem() {
        instantLoanItemList.add(InstantLoanItem(getPageTitle(0),
                getDanaInstantFragment(0)))
        instantLoanItemList.add(InstantLoanItem(getPageTitle(1),
                getTanpaAgunanFragment(1)))
        instantLoanItemList.add(InstantLoanItem(getPageTitle(2),
                getDenganAngunanFragment(2)))
    }


    private fun setActiveTab() {
        heightWrappingViewPager!!.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        heightWrappingViewPager!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        heightWrappingViewPager!!.currentItem = activeTabPosition
                    }
                })
    }

    private fun getDanaInstantFragment(position: Int): DanaInstantFragment {
        return DanaInstantFragment.createInstance(position)
    }

    private fun getTanpaAgunanFragment(position: Int): TanpaAgunanFragment {
        return TanpaAgunanFragment.createInstance(position)
    }

    private fun getDenganAngunanFragment(position: Int): DenganAgunanFragment {
        return DenganAgunanFragment.createInstance(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        NetworkClient.init(this)
        super.onCreate(savedInstanceState)

        if (application is InstantLoanRouter) {
            instantLoanEnabled = (application as InstantLoanRouter).isInstantLoanEnabled
        }

        if (intent != null && intent.extras != null) {
            val bundle = intent.extras
            val tabName = bundle!!.getString(TAB_NAME)

            if (tabName != null) {
                when (tabName) {
                    TAB_INSTAN -> if (instantLoanEnabled) {
                        activeTabPosition = 0
                    } else {
                        finish()
                    }

                    TAB_TANPA_AGUNAN -> if (instantLoanEnabled) {
                        activeTabPosition = 1
                    } else {
                        activeTabPosition = 0
                    }
                    TAB_AGUNAN -> if (instantLoanEnabled) {
                        activeTabPosition = 2
                    } else {
                        activeTabPosition = 1
                    }
                    else -> activeTabPosition = 0
                }
            }
        } else {
            activeTabPosition = 0
        }

        val tab = tabLayout!!.getTabAt(activeTabPosition)
        tab?.select()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu

        if (onGoingLoanStatus && !menushown) {
            menushown = true
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.instant_loan_menu, menu)
            return true
        } else {
            return super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.submission_history) {
            openWebView(SUBMISSION_HISTORY_URL)
            return true
        } else if (id == R.id.payment_method) {
            openWebView(String.format(PAYMENT_METHODS_URL, onGoingLoanId.toString()))
            return true
        } else if (id == R.id.help) {
            openWebView(HELP_URL)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onDestroy() {
        super.onDestroy()
        mBannerPresenter.detachView()
        onGoingLoanPresenter.detachView()
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_instant_loan
    }

    override fun renderUserList(banners: List<BannerEntity>?) {
        if (!banners!!.isEmpty()) {
            if (banners.size > 1) {
                (findViewById<View>(R.id.button_next) as FloatingActionButton).show()
            }
            findViewById<View>(R.id.container_banner).visibility = View.VISIBLE
            mBannerPager = findViewById(R.id.view_pager_banner)
            mBannerPager!!.offscreenPageLimit = 2
            mBannerPager!!.adapter = BannerPagerAdapter(this, banners, this)
            mBannerPager!!.setPadding(resources.getDimensionPixelOffset(R.dimen.il_margin_banner),
                    0, resources.getDimensionPixelOffset(R.dimen.il_margin_banner), 0)
            mBannerPager!!.clipToPadding = false
            mBannerPager!!.pageMargin = resources.getDimensionPixelOffset(R.dimen.il_margin_medium)
            mBannerPager!!.addOnPageChangeListener(mBannerPageChangeListener)
            sendBannerImpressionEvent(0)
        }
    }

    override fun nextBanner() {
        if (mBannerPager == null
                || mBannerPager!!.adapter == null
                || mBannerPager!!.currentItem == mBannerPager!!.adapter!!.count) {
            return
        }

        mBannerPager!!.setCurrentItem(mBannerPager!!.currentItem + 1, true) //+1 for move the page to next
    }

    override fun previousBanner() {
        if (mBannerPager == null
                || mBannerPager!!.adapter == null
                || mBannerPager!!.currentItem == 0) {
            return
        }

        mBannerPager!!.setCurrentItem(mBannerPager!!.currentItem - 1, true) //-1 for move the page to prev
    }


    override fun onClick(source: View) {
        if (source.id == R.id.button_next) {
            nextBanner()
        } else if (source.id == R.id.button_previous) {
            previousBanner()
        }
    }

    private fun initInjector() {
        val daggerInstantLoanComponent = InstantLoanComponentInstance.get(application)
        daggerInstantLoanComponent!!.inject(this)
    }

    private fun initializeView() {
        tabLayout = findViewById(R.id.tabs)
        heightWrappingViewPager = findViewById(R.id.pager)
        mBtnNextBanner = findViewById(R.id.button_next)
        mBtnPreviousBanner = findViewById(R.id.button_previous)
    }

    private fun attachViewListener() {
        mBtnNextBanner!!.setOnClickListener(this)
        mBtnPreviousBanner!!.setOnClickListener(this)
    }

    private fun setupToolbar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_icon_back_black)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setTitle(this.title)
            updateTitle("")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private var mRequiredPermission: List<String> = ArrayList()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        sendPermissionDeniedGTMEvent(permissions, grantResults)
        this.mRequiredPermission = DDCollectorManager.getsInstance().dangerousPermissions
        DDCollectorManager.getsInstance().onRequestPermissionsResult(requestCode, mRequiredPermission, permissions, grantResults)
    }

    private fun sendBannerImpressionEvent(position: Int) {
        val bannerPagerAdapter = mBannerPager!!.adapter as BannerPagerAdapter?

        if (bannerPagerAdapter != null &&
                bannerPagerAdapter.bannerEntityList != null &&
                bannerPagerAdapter.bannerEntityList[position] != null) {
            val eventLabel = (bannerPagerAdapter.bannerEntityList[position].link
                    + " - " + position.toString())

            instantLoanAnalytics.eventLoanBannerImpression(eventLabel)
        }
    }

    private fun getPageTitle(position: Int): CharSequence {
        return resources.getStringArray(R.array.values_title)[position]
    }

    override fun onBannerClick(view: View, position: Int) {
        val url = view.tag as String
        val eventLabel = url + " - " + position.toString()
        instantLoanAnalytics.eventLoanBannerClick(eventLabel)
        if (!TextUtils.isEmpty(url)) {
            openWebView(url)
        }
    }

    fun openWebView(url: String) {
        RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    private fun sendPermissionDeniedGTMEvent(permissions: Array<String>, grantResults: IntArray) {
        val eventLabel = StringBuilder(InstantLoanEventConstants.EventLabel.PL_PERMISSION_DENIED)
        for (i in permissions.indices) {
            if (grantResults[i] == -1) {
                eventLabel.append(permissions[i]).append(", ")
            }
        }

        instantLoanAnalytics.eventInstantLoanPermissionStatus(eventLabel.toString())
    }

    override fun setUserOnGoingLoanStatus(status: Boolean, loanId: Int) {
        this.onGoingLoanStatus = status
        this.onGoingLoanId = loanId
        onCreateOptionsMenu(menu)
    }

    companion object {

        val PINJAMAN_TITLE = "Pinjaman Online"

        val TAB_NAME = "tab_name"
        private val TAB_INSTAN = "instan"
        private val TAB_TANPA_AGUNAN = "tanpaagunan"
        private val TAB_AGUNAN = "agunan"

        fun createIntent(context: Context): Intent {
            return Intent(context, InstantLoanActivity::class.java)
        }
    }


    object DeepLinkIntents {
        @DeepLink(ApplinkConst.INSTANT_LOAN, ApplinkConst.INSTANT_LOAN_TAB)
        @JvmStatic
        fun getInstantLoanCallingIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, InstantLoanActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }
}

