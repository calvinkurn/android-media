package com.tokopedia.instantloan.view.activity

import android.os.Build
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.InstantLoanComponentInstance
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.data.model.response.GqlLendingBannerData
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingPartnerData
import com.tokopedia.instantloan.data.model.response.TestimonialEntity
import com.tokopedia.instantloan.ddcollector.DDCollectorManager
import com.tokopedia.instantloan.network.InstantLoanUrl
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.HELP_URL
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.PAYMENT_METHODS_URL
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.SUBMISSION_HISTORY_URL
import com.tokopedia.instantloan.view.adapter.*
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment
import com.tokopedia.instantloan.view.fragment.LePartnerFragment
import com.tokopedia.instantloan.view.fragment.TanpaAgunanFragment
import com.tokopedia.instantloan.view.presenter.InstantLoanLendingDataPresenter
import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager
import com.tokopedia.instantloan.view.ui.InstantLoanItem
import com.tokopedia.instantloan.view.ui.PartnerDataPageItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_instant_loan.*
import kotlinx.android.synthetic.main.il_other_financial_products.*
import kotlinx.android.synthetic.main.layout_bottom_banner.*
import kotlinx.android.synthetic.main.layout_il_testimonials.*
import kotlinx.android.synthetic.main.layout_lending_category.*
import kotlinx.android.synthetic.main.layout_lending_partner.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.TimeInterval
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InstantLoanActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent>, InstantLoanLendingDataContractor.View,
        DanaInstantFragment.ActivityInteractor, BannerPagerAdapter.BannerClick {

    @Inject
    lateinit var mLendingDataPresenter: InstantLoanLendingDataPresenter

    @Inject
    lateinit var instantLoanAnalytics: InstantLoanAnalytics

    @Inject
    lateinit var userSession: UserSession

    private lateinit var mBannerPager: ViewPager

    private var tabLayout: TabLayout? = null
    private var heightWrappingViewPager: HeightWrappingViewPager? = null
    private lateinit var partnerHeightWrappingViewPager: HeightWrappingViewPager
    private var activeTabPosition = 1
    private var instantLoanEnabled = true
    private var menu: Menu? = null
    private var onGoingLoanStatus = false
    private var onGoingLoanId: Int = 0
    private var menushown = false

    private lateinit var rvSeoLayout: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var lendingCategoryAdpater: LendingCategoryAdapter
    private lateinit var lendingSeoAdapter: LendingSeoAdapter

    private var observable: Subscription? = null
    private var tanpaAgunanFragmentInstance: TanpaAgunanFragment? = null

    private var parterDataList: ArrayList<GqlLendingPartnerData> = ArrayList()

    internal var instantLoanItemList: MutableList<InstantLoanItem> = ArrayList()

    internal var partnerDataItemList: ArrayList<PartnerDataPageItem> = ArrayList()

    private val mBannerPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            sendBannerImpressionEvent(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initInjector()
        mLendingDataPresenter.attachView(this)
        initializeView()
        attachViewListener()
        setupToolbar()
        loadSection()
        loadTestimonials()
    }

    private fun setupSeoLayout() {
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSeoLayout.layoutManager = linearLayoutManager
        rvSeoLayout.adapter = lendingSeoAdapter
    }

    private fun setupCategoryLayout() {
        layoutManager = GridLayoutManager(this, COLUMN_COUNT_FOR_LOAN_CATEGORY)
        rv_lending_category.layoutManager = layoutManager
        rv_lending_category.adapter = lendingCategoryAdpater
    }

    private fun setupPartnerLayout() {
        populatePartnerItemList()
        val partnerItemPagerAdapter = LendingPartnerPagerAdapter(supportFragmentManager)
        partnerItemPagerAdapter.setData(partnerDataItemList)

        partnerHeightWrappingViewPager.adapter = partnerItemPagerAdapter

        if (partnerDataItemList.size > 1) {
            il_partner_page_indicator.visibility = View.VISIBLE
            il_partner_page_indicator.setViewPager(partnerHeightWrappingViewPager, 0)
        } else {
            il_partner_page_indicator.visibility = View.GONE
        }
    }

    override fun IsUserLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

    fun renderBannerList(banners: ArrayList<GqlLendingBannerData>) {
        mBannerPager = findViewById(com.tokopedia.instantloan.R.id.view_pager_banner)

        mBannerPager.apply {
            offscreenPageLimit = 2
            adapter = BannerPagerAdapter(banners, context as BannerPagerAdapter.BannerClick)
            setPadding(resources.getDimensionPixelOffset(com.tokopedia.instantloan.R.dimen.il_margin_banner), 0,
                    resources.getDimensionPixelOffset(com.tokopedia.instantloan.R.dimen.il_margin_banner), 0)
            clipToPadding = false
            pageMargin = resources.getDimensionPixelOffset(com.tokopedia.instantloan.R.dimen.il_margin_medium)
            addOnPageChangeListener(mBannerPageChangeListener)
        }

        if (banners.size > 1) {
            il_banner_page_indicator.visibility = View.VISIBLE
            il_banner_page_indicator.setViewPager(mBannerPager, 0)
        } else {
            il_banner_page_indicator.visibility = View.GONE
        }

        sendBannerImpressionEvent(0)
    }

    override fun renderLendingData(gqlLendingDataResponse: GqlLendingDataResponse) {

        if (gqlLendingDataResponse.leBanner.bannerData.isNotEmpty()) {
            showBannerLayout()
            renderBannerList(gqlLendingDataResponse.leBanner.bannerData)
        } else {
            hideBannerLayout()
        }

        if (gqlLendingDataResponse.leCategory.categoryData.isNotEmpty()) {
            showCategoryLayout()
            lendingCategoryAdpater = LendingCategoryAdapter(gqlLendingDataResponse.leCategory.categoryData)
            setupCategoryLayout()
            tanpaAgunanFragmentInstance?.setCategoryData(gqlLendingDataResponse.leCategory.categoryData)

        } else {
            hideCategoryLayout()
        }

        if (gqlLendingDataResponse.lePartner.partnerData.isNotEmpty()) {
            showPartnerLayout()
            parterDataList = gqlLendingDataResponse.lePartner.partnerData
            setupPartnerLayout()
        } else {
            hidePartnerLayout()
        }

        if (gqlLendingDataResponse.leSeo != null && gqlLendingDataResponse.leSeo?.seoData != null &&
                gqlLendingDataResponse.leSeo?.seoData?.isNotEmpty()!!) {
            showSeoLayout()
            lendingSeoAdapter = LendingSeoAdapter(gqlLendingDataResponse.leSeo!!.seoData!!)
            setupSeoLayout()
        } else {
            hideSeoLayout()
        }

    }

    private fun hideSeoLayout() {
        rvSeoLayout.visibility = View.GONE
    }

    private fun showSeoLayout() {
        rvSeoLayout.visibility = View.VISIBLE
    }

    private fun hideBannerLayout() {
        il_banner_layout.visibility = View.GONE
    }

    private fun showBannerLayout() {
        il_banner_layout.visibility = View.VISIBLE
    }


    private fun loadTestimonials() {

        val testimonialList: ArrayList<TestimonialEntity> = ArrayList()

        val testimonialItem1 = TestimonialEntity(getString(com.tokopedia.instantloan.R.string.il_testimonial_review_1), getString(com.tokopedia.instantloan.R.string.il_testimonial_name_1), InstantLoanUrl.COMMON_URL.USER_TESTIMONIAL_IMAGE_URL_1)
        testimonialList.add(testimonialItem1)

        val testimonialItem2 = TestimonialEntity(getString(com.tokopedia.instantloan.R.string.il_testimonial_review_2), getString(com.tokopedia.instantloan.R.string.il_testimonial_name_2), InstantLoanUrl.COMMON_URL.USER_TESTIMONIAL_IMAGE_URL_2)
        testimonialList.add(testimonialItem2)

        val testimonialItem3 = TestimonialEntity(getString(com.tokopedia.instantloan.R.string.il_testimonial_review_3), getString(com.tokopedia.instantloan.R.string.il_testimonial_name_3), InstantLoanUrl.COMMON_URL.USER_TESTIMONIAL_IMAGE_URL_3)
        testimonialList.add(testimonialItem3)

        il_view_pager_testimonials.pageMargin = resources.getDimensionPixelOffset(com.tokopedia.instantloan.R.dimen.il_margin_medium)
        il_view_pager_testimonials.adapter = DanaInstanTestimonialsPagerAdapter(testimonialList)
        (il_view_pager_testimonials.adapter as DanaInstanTestimonialsPagerAdapter).notifyDataSetChanged()

        var currentItem = 0
        il_view_pager_testimonials.setCurrentItem(currentItem)

        observable = Observable.interval(8, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<TimeInterval<Long>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(longTimeInterval: TimeInterval<Long>) {
                        currentItem++
                        if (currentItem <= testimonialList.size - 1) {
                            il_view_pager_testimonials.setCurrentItem(currentItem)
                        } else {
                            currentItem = 0
                            il_view_pager_testimonials.setCurrentItem(currentItem)

                        }
                    }
                })

    }

    private fun showCategoryLayout() {
        il_category_layout.visibility = View.VISIBLE
    }

    private fun hideCategoryLayout() {
        il_category_layout.visibility = View.GONE
    }

    private fun hidePartnerLayout() {
        il_partner_layout.visibility = View.GONE
    }

    private fun showPartnerLayout() {
        il_partner_layout.visibility = View.VISIBLE
    }

    private fun hideTestimonials() {
        dana_instan_testimonials.visibility = View.GONE
    }

    private fun showTestimonials() {
        dana_instan_testimonials.visibility = View.VISIBLE
    }

    private fun loadSection() {

        if (instantLoanEnabled) {
            populateTwoTabItem()
        } else {
            populateOneTabItem()
        }
        val instantLoanPagerAdapter = InstantLoanPagerAdapter(supportFragmentManager)
        instantLoanPagerAdapter.setData(instantLoanItemList)
        heightWrappingViewPager!!.adapter = instantLoanPagerAdapter
        tabLayout!!.setupWithViewPager(heightWrappingViewPager)
        setActiveTab()

        heightWrappingViewPager!!.addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        if (position == DANA_INSTAN_TAB_POSITION) {
                            showTestimonials()
                        } else {
                            hideTestimonials()
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                    }
                }
        )
    }

    private fun populateOneTabItem() {
        tanpaAgunanFragmentInstance = getTanpaAgunanFragment()
        instantLoanItemList.add(InstantLoanItem(getPageTitle(PINJAMAN_ONLINE_TAB_POSITION),
                tanpaAgunanFragmentInstance))
    }

    private fun populateTwoTabItem() {
        tanpaAgunanFragmentInstance = getTanpaAgunanFragment()
        instantLoanItemList.add(InstantLoanItem(getPageTitle(PINJAMAN_ONLINE_TAB_POSITION),
                tanpaAgunanFragmentInstance))
        instantLoanItemList.add(InstantLoanItem(getPageTitle(DANA_INSTAN_TAB_POSITION),
                getDanaInstantFragment()))
    }

    private fun populatePartnerItemList() {

        var pointer = 0
        var tempList: ArrayList<GqlLendingPartnerData> = ArrayList(DEFAULT_PARTNER_ITEM_LIST_SIZE)
        for (inx in parterDataList) {
            tempList.add(inx)
            pointer++
            if (pointer == DEFAULT_PARTNER_ITEM_LIST_SIZE) {
                pointer = 0
                partnerDataItemList.add(PartnerDataPageItem(getPartnerFragment(tempList)))
                tempList = ArrayList(DEFAULT_PARTNER_ITEM_LIST_SIZE)
            }
        }

        if (tempList.size > 0) {
            partnerDataItemList.add(PartnerDataPageItem(getPartnerFragment(tempList)))
        }
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

    private fun getDanaInstantFragment(): DanaInstantFragment {
        return DanaInstantFragment.createInstance()
    }

    private fun getTanpaAgunanFragment(): TanpaAgunanFragment {
        return TanpaAgunanFragment.createInstance()
    }

    private fun getPartnerFragment(partnerItemList: ArrayList<GqlLendingPartnerData>): Fragment {
        return LePartnerFragment.createInstance(partnerItemList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteConfig = FirebaseRemoteConfigImpl(this)
        instantLoanEnabled = remoteConfig.getBoolean(RemoteConfigKey.SHOW_INSTANT_LOAN, true)
        if (intent != null && intent.extras != null) {
            val bundle = intent.extras
            val tabName = bundle!!.getString(TAB_NAME)
            setActiveTabPosition(tabName)
        } else if (intent != null && intent.data != null) {
            setActiveTabPosition(intent?.data?.lastPathSegment)
        } else {
            activeTabPosition = 1
        }

        val tab = tabLayout!!.getTabAt(activeTabPosition)
        tab?.select()
    }

    private fun setActiveTabPosition(tabName: String?) {
        if (tabName != null) {
            when (tabName) {
                TAB_INSTAN -> if (instantLoanEnabled) {
                    activeTabPosition = DANA_INSTAN_TAB_POSITION
                } else {
                    finish()
                }
                TAB_TANPA_AGUNAN ->
                    activeTabPosition = PINJAMAN_ONLINE_TAB_POSITION

                else -> activeTabPosition = DANA_INSTAN_TAB_POSITION
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu

        if (onGoingLoanStatus && !menushown) {
            menushown = true
            val menuInflater = menuInflater
            menuInflater.inflate(com.tokopedia.instantloan.R.menu.instant_loan_menu, menu)
            return true
        } else {
            return super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == com.tokopedia.instantloan.R.id.submission_history) {
            openWebView(SUBMISSION_HISTORY_URL)
            return true
        } else if (id == com.tokopedia.instantloan.R.id.payment_method) {
            openWebView(String.format(PAYMENT_METHODS_URL, onGoingLoanId.toString()))
            return true
        } else if (id == com.tokopedia.instantloan.R.id.help) {
            openWebView(HELP_URL)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onDestroy() {
        super.onDestroy()
        mLendingDataPresenter.detachView()
        if (observable != null && !observable?.isUnsubscribed!!)
            observable?.unsubscribe()
    }


    override fun getLayoutRes(): Int {
        return com.tokopedia.instantloan.R.layout.activity_instant_loan
    }

    private fun initInjector() {
        val daggerInstantLoanComponent = InstantLoanComponentInstance.get(application)
        daggerInstantLoanComponent!!.inject(this)
    }

    private fun initializeView() {
        tabLayout = findViewById(com.tokopedia.instantloan.R.id.tabs)
        heightWrappingViewPager = findViewById(com.tokopedia.instantloan.R.id.il_pager)
        partnerHeightWrappingViewPager = findViewById(com.tokopedia.instantloan.R.id.view_pager_partner)
        rvSeoLayout = findViewById(com.tokopedia.instantloan.R.id.rv_lending_seo)
    }

    private fun attachViewListener() {
        financial_product_one.setOnClickListener {
            openWebView(InstantLoanUrl.COMMON_URL.CREDIT_CARD_WEBVIEW_URL)
        }

        financial_product_two.setOnClickListener {
            openWebView(InstantLoanUrl.COMMON_URL.INSURANCE_WEBVIEW_URL)
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById(com.tokopedia.instantloan.R.id.toolbar)
        toolbar.setNavigationIcon(com.tokopedia.instantloan.R.drawable.instant_loan_action_back_arrow_black)
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
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.design.R.color.white)
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
        val bannerPagerAdapter = mBannerPager.adapter as BannerPagerAdapter?

        if (!(bannerPagerAdapter?.bannerEntities == null)) {
            val eventLabel = (bannerPagerAdapter.bannerEntities[position].bannerLink
                    + " - " + position.toString())

            instantLoanAnalytics.eventLoanBannerImpression(eventLabel)
        }

    }

    private fun getPageTitle(position: Int): CharSequence {
        return resources.getStringArray(com.tokopedia.instantloan.R.array.values_title)[position]
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

        val COLUMN_COUNT_FOR_LOAN_CATEGORY = 4
        val DEFAULT_PARTNER_ITEM_LIST_SIZE = 9
        val TAB_NAME = "tab_name"
        val PINJAMAN_ONLINE_TAB_POSITION = 0
        val DANA_INSTAN_TAB_POSITION = 1
        private val TAB_INSTAN = "instan"
        private val TAB_TANPA_AGUNAN = "pinjamanonline"
    }
}

