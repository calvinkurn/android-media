package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.activity.AffiliateCuratedProductActivity
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewpager.AffiliateCuratedProductPagerAdapter
import com.tokopedia.affiliate.feature.dashboard.view.bottomsheet.NoConnectionBottomSheetDialog
import com.tokopedia.affiliate.feature.dashboard.view.custom.AffiliateDashboardTab
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateDashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.ShareableByMeProfileViewModel
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.widget.ByMeInstastoryView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.EmptyState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardFragment :
        BaseDaggerFragment(),
        AffiliateDashboardContract.View,
        ShareBottomSheets.OnShareItemClickListener,
        AffiliateCuratedProductFragment.CuratedProductListener {

    companion object {
        fun newInstance(bundle: Bundle): AffiliateDashboardFragment {
            return AffiliateDashboardFragment().apply {
                arguments = bundle
            }
        }
    }

    override val ctx: Context?
        get() = context

    @Inject
    lateinit var presenter: AffiliateDashboardPresenter

    @Inject
    lateinit var affiliatePrefs: AffiliatePreference

    @Inject
    lateinit var userSession: UserSessionInterface

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private val unifyCalendar
        get() = calendarView.findViewById<UnifyCalendar>(R.id.uc_filter)

    private val tabCuratedPost: AffiliateDashboardTab by lazy {
        AffiliateDashboardTab(context).apply {
            setTitle(getString(R.string.af_tab_curated_from_post))
        }
    }

    private val tabCuratedTraffic: AffiliateDashboardTab by lazy {
        AffiliateDashboardTab(context).apply {
            setTitle(getString(R.string.af_tab_curated_from_traffic))
        }
    }

    private val coachMarkAffIncome: CoachMark by lazy {
        CoachMarkBuilder()
                .allowPreviousButton(false)
                .build()
    }

    private val coachMark: CoachMark by lazy {
        CoachMarkBuilder().build()
    }

    private val coachMarkIncomeItem: CoachMarkItem by lazy {
        CoachMarkItem(ivAfIncomeInfo, getString(R.string.af_info), getString(R.string.af_income_tooltip))
    }

    private val coachMarkCuratedTrafficItem: CoachMarkItem by lazy {
        CoachMarkItem(tabCuratedTraffic, getString(R.string.curated_from_traffic), getString(R.string.curated_from_traffic_info))
    }

    private val coachMarkCuratedPostItem: CoachMarkItem by lazy {
        CoachMarkItem(tabCuratedPost, getString(R.string.curated_from_post), getString(R.string.curated_from_post_info))
    }

    private val noConnectionDialog: NoConnectionBottomSheetDialog by lazy { NoConnectionBottomSheetDialog.createInstance(context) }

    private var startDate by Delegates.observable<Date?>(null) { _, _, newValue ->
        if (::tvStartDate.isInitialized) tvStartDate.text = newValue?.let(dateFormatter::format).orEmpty()
    }
    private var endDate by Delegates.observable<Date?>(null) { _, _, newValue ->
        if (::tvEndDate.isInitialized) tvEndDate.text = newValue?.let(dateFormatter::format).orEmpty()
    }

    private var tempStartDate = startDate
    private var tempEndDate = endDate

    private lateinit var llFullDashboardPage: LinearLayout
    private lateinit var clDashboard: CoordinatorLayout
    private lateinit var tvTotalSaldo: TextView
    private lateinit var tvAffiliateIncome: TextView
    private lateinit var tvTotalViewed: TextView
    private lateinit var tvTotalClicked: TextView
    private lateinit var tvPostedProduct: TextView
    private lateinit var tlCuratedProducts: TabLayout
    private lateinit var vpCuratedProduct: ViewPager
    private lateinit var llStartDate: LinearLayout
    private lateinit var llEndDate: LinearLayout
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var btnCheckBalance: UnifyButton
    private lateinit var tvSeeAll: TextView
    private lateinit var clViewedClicked: ConstraintLayout
    private lateinit var clPostedProduct: ConstraintLayout
    private lateinit var vPostedViewedSeparator: View
    private lateinit var llCuratedProductHistory: LinearLayout
    private lateinit var esShareNow: EmptyState
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var ivAfIncomeInfo: ImageView
    private lateinit var bmivShare: ByMeInstastoryView
    private lateinit var ablAfDashboard: AppBarLayout

    private lateinit var calendarBottomSheet: CloseableBottomSheetDialog
    private lateinit var calendarView: View

    private lateinit var directFragmentCurated: AffiliateCuratedProductFragment
    private lateinit var indirectFragmentCurated: AffiliateCuratedProductFragment

    private lateinit var holidayList: List<Legend>

    private lateinit var profileHeader: ShareableByMeProfileViewModel

    override fun getScreenName(): String = "Dashboard"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_af_dashboard, container, false)
        presenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        presenter.checkAffiliate()
    }

    override fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel, byMeHeader: ShareableByMeProfileViewModel) {
        clDashboard.visible()

        tvTotalSaldo.text = MethodChecker.fromHtml(header.totalSaldoAktif)
        tvAffiliateIncome.text = MethodChecker.fromHtml(header.affiliateIncome)
        tvTotalViewed.text = MethodChecker.fromHtml(header.seenCount)
        tvTotalClicked.text = MethodChecker.fromHtml(header.clickCount)
        tvPostedProduct.text = MethodChecker.fromHtml(getString(R.string.posted_product_with_number, header.productCount))

        if (header.productCount.toIntOrZero() <= 0) showEmptyState()
        else showNonEmptyState()

        if (startDate != null && endDate != null) showChangesAppliedToaster()

        srlRefresh.isRefreshing = false

        profileHeader = byMeHeader
        initByMeInstastoryView(byMeHeader)
    }

    override fun onErrorCheckAffiliate(error: String) {
        clDashboard.gone()
        noConnectionDialog.showWithListener(presenter::checkAffiliate)
    }

    override fun onSuccessCheckAffiliate(isAffiliate: Boolean) {
        if (isAffiliate) presenter.loadDashboardDetail(startDate, endDate)
        else closePage()
    }

    override fun onUserNotLoggedIn() {
        closePage()
    }

    override fun onGetHolidayList(legendList: List<Legend>) {
        holidayList = legendList
        openCalendarPicker()
    }

    override fun onErrorGetHoliday(error: Throwable) {
        holidayList = emptyList()
        openCalendarPicker()
    }

    override fun onErrorGetDashboardItem(error: String) {
        clDashboard.gone()
        noConnectionDialog.showWithListener { presenter.loadDashboardDetail(startDate, endDate) }
    }

    override fun showLoading() {
        llFullDashboardPage.showLoading()
    }

    override fun hideLoading() {
        llFullDashboardPage.hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun shouldShareProfile() {
        if (::profileHeader.isInitialized) {
            ShareBottomSheets.newInstance(
                    this,
                    "",
                    profileHeader.avatar,
                    profileHeader.link,
                    String.format(getString(R.string.profile_share_text), profileHeader.link),
                    String.format(getString(R.string.profile_share_title)),
                    bmivShare.getTempFileUri()
            ).also {
                it.show(fragmentManager)
            }
        }
    }

    override fun onShareItemClicked(packageName: String) {

    }

    private fun onCheckBalanceClicked() {
        if (RouteManager.isSupportApplink(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)) {
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        }
    }

    private fun onSeeAllProductClicked() {
        startActivity(Intent(context, AffiliateCuratedProductActivity::class.java))
    }

    private fun onRefresh() {
        srlRefresh.isRefreshing = false
        clDashboard.gone()
        presenter.checkAffiliate()
        onDateChanged()
    }

    private fun openCalendarPicker() {
        if (!::holidayList.isInitialized) presenter.loadHolidayList()
        else {
            calendarView = initCalendarView()
            calendarBottomSheet = initCalendarBottomSheet()
            unifyCalendar.calendarPickerView?.scrollToDate(startDate ?: getCurrentDate())
            tempStartDate = null
            tempEndDate = null
            calendarBottomSheet.show()
        }
    }

    private fun initCalendarView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_af_filter_date, null, false)
        val ivClose = view.findViewById<AppCompatImageView>(R.id.iv_close)
        ivClose.setOnClickListener {
            calendarBottomSheet.dismiss()
        }
        return view
    }

    private fun initCalendarBottomSheet(): CloseableBottomSheetDialog {
        initCalendar(unifyCalendar)
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context)
        bottomSheet.apply {
            setCustomContentView(
                    calendarView,
                    getString(R.string.filter_date),
                    false
            )
            unifyCalendar.layoutParams.height = getScreenHeight() / 2
            setCancelable(false)
            setOnDismissListener {
                if (tempStartDate != null && tempEndDate != null) {
                    startDate = tempStartDate
                    endDate = tempEndDate
                    onDateChanged()
                }
            }
        }
        return bottomSheet
    }

    private fun initCalendar(calendar: UnifyCalendar) {
        val pickerView = calendar.calendarPickerView
        val calendarPickerView = pickerView?.init(getMinDate(), getMaxDate(), holidayList)
                ?.inMode(CalendarPickerView.SelectionMode.RANGE)
        pickerView?.setOnDateSelectedListener(getOnSelectedDateListener())

        startDate?.let { startDate ->
            endDate?.let { endDate ->
                calendarPickerView?.withSelectedDates(listOf(startDate, endDate))
            }
        }
    }

    private fun getMinDate(): Date {
        val minDate = "2015-01-01"
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormatter.parse(minDate)
    }

    private fun getMaxDate(): Date {
        val maxTime = Calendar.getInstance(Locale.getDefault())
        maxTime.add(Calendar.DAY_OF_MONTH, 1)
        maxTime.set(Calendar.HOUR_OF_DAY, 0)
        maxTime.set(Calendar.MINUTE, 0)
        maxTime.set(Calendar.SECOND, 0)
        maxTime.set(Calendar.MILLISECOND, 0)
        return maxTime.time
    }

    private fun getCurrentDate(): Date {
        val currentTime = Calendar.getInstance(Locale.getDefault())
        currentTime.set(Calendar.HOUR_OF_DAY, 0)
        currentTime.set(Calendar.MINUTE, 0)
        currentTime.set(Calendar.SECOND, 0)
        currentTime.set(Calendar.MILLISECOND, 0)
        return currentTime.time
    }

    private fun getOnSelectedDateListener(): CalendarPickerView.OnDateSelectedListener {
        return object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if ((tempStartDate != null && tempEndDate != null) ||
                        (tempStartDate == null && tempEndDate == null) ||
                        (tempEndDate == null && date.before(tempStartDate))) {
                    tempStartDate = date
                    tempEndDate = null
                } else if (tempStartDate != null && tempEndDate == null && date.after(tempStartDate)) {
                    tempEndDate = date
                    calendarBottomSheet.dismiss()
                }
            }

            override fun onDateUnselected(date: Date) {

            }
        }
    }

    private fun onDateChanged() {
        presenter.loadDashboardDetail(startDate, endDate)
        directFragmentCurated.apply {
            startDate = this@AffiliateDashboardFragment.startDate
            endDate = this@AffiliateDashboardFragment.endDate
        }.run { loadData(1) }
        indirectFragmentCurated.apply {
            startDate = this@AffiliateDashboardFragment.startDate
            endDate = this@AffiliateDashboardFragment.endDate
        }.run { loadData(1) }
    }

    private fun closePage() = activity?.finish()

    private fun showEmptyState() {
        clViewedClicked.gone()
        vPostedViewedSeparator.gone()
        llCuratedProductHistory.gone()
        esShareNow.visible()
    }

    private fun showNonEmptyState() {
        clViewedClicked.visible()
        vPostedViewedSeparator.visible()
        llCuratedProductHistory.visible()
        esShareNow.gone()

        if (affiliatePrefs.isFirstTimeOpenDashboard(userSession.userId)) {
            coachMark.show(activity, "FirstTimeUser", arrayListOf(coachMarkCuratedPostItem, coachMarkCuratedTrafficItem))
            affiliatePrefs.setFirstTimeOpenDashboard(userSession.userId)
        }
    }

    private fun showTooltip() {
        coachMarkAffIncome.show(activity, "AffiliateIncome", arrayListOf(coachMarkIncomeItem))
    }

    private fun showChangesAppliedToaster() {
        view?.let {
            Toaster.showNormalWithAction(it, getString(R.string.af_success_get_dashboard_info), 2000, getString(R.string.af_ok), View.OnClickListener { })
        }
    }

    private fun initByMeInstastoryView(byMeHeader: ShareableByMeProfileViewModel) {
        bmivShare.apply {
            setUserName(byMeHeader.formattedUserName)
            ivAvatar.loadImageCircle(byMeHeader.avatar)
        }
    }

    private fun initView(view: View) {
        view.run {
            llFullDashboardPage = findViewById(R.id.ll_full_dashboard_page)
            clDashboard = findViewById(R.id.cl_dashboard)
            tvTotalSaldo = findViewById(R.id.tv_total_saldo)
            tvAffiliateIncome = findViewById(R.id.tv_affiliate_income)
            tvTotalViewed = findViewById(R.id.tv_total_viewed)
            tvTotalClicked = findViewById(R.id.tv_total_clicked)
            tvPostedProduct = findViewById(R.id.tv_posted_product)
            tlCuratedProducts = findViewById(R.id.tl_curated_products)
            vpCuratedProduct = findViewById(R.id.vp_curated_product)
            llStartDate = findViewById(R.id.ll_start_date)
            llEndDate = findViewById(R.id.ll_end_date)
            tvStartDate = findViewById(R.id.tv_start_date)
            tvEndDate = findViewById(R.id.tv_end_date)
            btnCheckBalance = findViewById(R.id.btn_check_balance)
            tvSeeAll = findViewById(R.id.tv_see_all)
            clViewedClicked = findViewById(R.id.cl_viewed_clicked)
            clPostedProduct = findViewById(R.id.cl_posted_product)
            vPostedViewedSeparator = findViewById(R.id.v_posted_viewed_separator)
            llCuratedProductHistory = findViewById(R.id.ll_curated_product_history)
            esShareNow = findViewById(R.id.es_share_now)
            srlRefresh = findViewById(R.id.srl_refresh)
            ivAfIncomeInfo = findViewById(R.id.iv_af_income_info)
            bmivShare = findViewById(R.id.bmiv_share)
            ablAfDashboard = findViewById(R.id.abl_af_dashboard)
        }
    }

    private fun setupView(view: View) {
        fragmentManager?.let {
            if (!::directFragmentCurated.isInitialized) directFragmentCurated = AffiliateCuratedProductFragment.newInstance(1).apply { setListener(this@AffiliateDashboardFragment) }
            if (!::indirectFragmentCurated.isInitialized) indirectFragmentCurated = AffiliateCuratedProductFragment.newInstance(2).apply { setListener(this@AffiliateDashboardFragment) }
            vpCuratedProduct.adapter = AffiliateCuratedProductPagerAdapter(it, listOf(
                    directFragmentCurated,
                    indirectFragmentCurated
            ))
        }
        llStartDate.setOnClickListener { openCalendarPicker() }
        llEndDate.setOnClickListener { openCalendarPicker() }

        btnCheckBalance.setOnClickListener { onCheckBalanceClicked() }
        tvSeeAll.setOnClickListener { onSeeAllProductClicked() }

        srlRefresh.setOnRefreshListener { onRefresh() }
        ivAfIncomeInfo.setOnClickListener { showTooltip() }

        esShareNow.setPrimaryCTAClickListener { shouldShareProfile() }

        ablAfDashboard.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            srlRefresh.isEnabled = (verticalOffset == 0)
        })

        tlCuratedProducts.apply {
            setupWithViewPager(vpCuratedProduct)

            getTabAt(0)?.let { tab ->
                tab.customView = tabCuratedPost
            }

            getTabAt(1)?.let { tab ->
                tab.customView = tabCuratedTraffic
            }

            onTabSelected { tab ->
                if (tab.customView == tabCuratedPost) {
                    tabCuratedPost.setActive(true)
                    tabCuratedTraffic.setActive(false)
                } else {
                    tabCuratedPost.setActive(false)
                    tabCuratedTraffic.setActive(true)
                }
            }
        }

        tabCuratedPost.setActive(true)
    }
}