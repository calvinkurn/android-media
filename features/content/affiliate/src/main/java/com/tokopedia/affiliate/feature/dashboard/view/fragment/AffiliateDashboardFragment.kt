package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.activity.AffiliateCuratedProductActivity
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewpager.AffiliateCuratedProductPagerAdapter
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateDashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardFragment : BaseDaggerFragment(), AffiliateDashboardContract.View {

    companion object {
        fun newInstance(bundle: Bundle): AffiliateDashboardFragment {
            return AffiliateDashboardFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var presenter: AffiliateDashboardPresenter

    private lateinit var llDashboard: LinearLayout
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
    private lateinit var llCheckBalance: LinearLayout
    private lateinit var tvSeeAll: TextView

    private lateinit var calendarBottomSheet: CloseableBottomSheetDialog
    private lateinit var calendarView: View

    private lateinit var directFragmentCurated: AffiliateCuratedProductFragment
    private lateinit var indirectFragmentCurated: AffiliateCuratedProductFragment

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private var startDate by Delegates.observable<Date?>(null) { _, _, newValue ->
        if (::tvStartDate.isInitialized) tvStartDate.text = newValue?.let(dateFormatter::format).orEmpty()
    }
    private var endDate by Delegates.observable<Date?>(null) { _, _, newValue ->
        if (::tvEndDate.isInitialized) tvEndDate.text = newValue?.let(dateFormatter::format).orEmpty()
    }

    override val ctx: Context?
        get() = context

    private val unifyCalendar
        get() = calendarView.findViewById<UnifyCalendar>(R.id.uc_filter)

    private lateinit var holidayList: List<Legend>

    override fun getScreenName(): String = "Dashboard"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_af_affiliate_dashboard, container, false)
        presenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        presenter.checkAffiliate()
    }

    private fun initView(view: View) {
        view.run {
            llDashboard = findViewById(R.id.ll_dashboard)
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
            llCheckBalance = findViewById(R.id.ll_check_balance)
            tvSeeAll = findViewById(R.id.tv_see_all)
        }
    }

    private fun setupView(view: View) {
        fragmentManager?.let {
            if (!::directFragmentCurated.isInitialized) directFragmentCurated = AffiliateCuratedProductFragment.newInstance(1)
            if (!::indirectFragmentCurated.isInitialized) indirectFragmentCurated = AffiliateCuratedProductFragment.newInstance(2)
            vpCuratedProduct.adapter = AffiliateCuratedProductPagerAdapter(it, listOf(
                    directFragmentCurated,
                    indirectFragmentCurated
            ))
        }
        vpCuratedProduct.layoutParams.height = (getScreenHeight()*0.75).toInt()
        tlCuratedProducts.setupWithViewPager(vpCuratedProduct)
        llStartDate.setOnClickListener { openCalendarPicker() }
        llEndDate.setOnClickListener { openCalendarPicker() }

        llCheckBalance.setOnClickListener { onCheckBalanceClicked() }
        tvSeeAll.setOnClickListener { onSeeAllProductClicked() }
    }

    override fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel) {
        tvTotalSaldo.text = MethodChecker.fromHtml(header.totalSaldoAktif)
        tvAffiliateIncome.text = MethodChecker.fromHtml(header.saldoString)
        tvTotalViewed.text = MethodChecker.fromHtml(header.seenCount)
        tvTotalClicked.text = MethodChecker.fromHtml(header.clickCount)
        tvPostedProduct.text = MethodChecker.fromHtml(getString(R.string.posted_product, header.productCount))
    }

    override fun onErrorCheckAffiliate(error: String) {
        NetworkErrorHelper.showEmptyState(activity,
                llDashboard,
                error
        ) { presenter.checkAffiliate() }
    }

    override fun onSuccessCheckAffiliate(isAffiliate: Boolean) {
        if (isAffiliate) presenter.loadDashboardDetail(startDate, endDate)
        else closePage()
    }

    override fun onUserNotLoggedIn() {
        closePage()
    }

    private fun onCheckBalanceClicked() {
        if(RouteManager.isSupportApplink(context, ApplinkConst.DEPOSIT)) {
            RouteManager.route(context, ApplinkConst.DEPOSIT)
        }
    }

    private fun onSeeAllProductClicked() {
        startActivity(Intent(context, AffiliateCuratedProductActivity::class.java))
    }

    private fun openCalendarPicker() {
        if (!::holidayList.isInitialized) presenter.loadHolidayList()
        else {
            if (!::calendarView.isInitialized) calendarView = initCalendarView()
            if (!::calendarBottomSheet.isInitialized) calendarBottomSheet = initCalendarBottomSheet()
            unifyCalendar.calendarPickerView.scrollToDate(startDate ?: getCurrentDate())
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
            unifyCalendar.layoutParams.height = getScreenHeight()/2
            setCancelable(false)
            setOnDismissListener {
                if (startDate != null && endDate == null) startDate = null
                onDateChanged()
            }
        }
        return bottomSheet
    }

    private fun initCalendar(calendar: UnifyCalendar) {
        val pickerView = calendar.calendarPickerView
        val maxDate = getMaxDate()
        val calendarPickerView = pickerView.init(getMinDate(), maxDate, holidayList)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
        pickerView.setOnDateSelectedListener(getOnSelectedDateListener())

        if (startDate != null && endDate != null) calendarPickerView.withSelectedDates(listOf(startDate, endDate))
    }

    private fun getMinDate(): Date {
        val minDate = "2009-01-01"
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormatter.parse(minDate)
    }

    private fun getMaxDate(): Date {
        val maxTime = Calendar.getInstance(Locale.getDefault())
        maxTime.set(Calendar.DAY_OF_MONTH, maxTime.getActualMaximum(Calendar.DAY_OF_MONTH))
        maxTime.add(Calendar.MONTH, 1)
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
                if ((startDate != null && endDate != null) ||
                        (startDate == null && endDate == null) ||
                        (endDate == null && date.before(startDate))) {
                            startDate = date
                            endDate = null
                        }
                else if (startDate != null && endDate == null && date.after(startDate)) {
                    endDate = date
                    calendarBottomSheet.dismiss()
                }
            }

            override fun onDateUnselected(date: Date) {

            }
        }
    }

    override fun onGetHolidayList(legendList: List<Legend>) {
        holidayList = legendList
        openCalendarPicker()
    }

    override fun onErrorGetHoliday(error: Throwable) {
        holidayList = emptyList()
        openCalendarPicker()
    }

    private fun onDateChanged() {
        presenter.loadDashboardDetail(startDate, endDate)
        directFragmentCurated.loadData(1)
        indirectFragmentCurated.loadData(1)
    }

    override fun onErrorGetDashboardItem(error: String) {
        NetworkErrorHelper.showEmptyState(activity,
                llDashboard,
                error
        ) { presenter.loadDashboardDetail() }
    }

    private fun closePage() = activity?.finish()

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLoading() {
        llDashboard.showLoadingTransparent()
    }

    override fun hideLoading() {
        llDashboard.hideLoadingTransparent()
    }
}