package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
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
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewpager.AffiliateProductBoughtPagerAdapter
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateDashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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

    private lateinit var tvTotalSaldo: TextView
    private lateinit var tvAffiliateIncome: TextView
    private lateinit var tvTotalViewed: TextView
    private lateinit var tvTotalClicked: TextView
    private lateinit var tlProductBought: TabLayout
    private lateinit var vpProductBought: ViewPager
    private lateinit var llStartDate: LinearLayout
    private lateinit var llEndDate: LinearLayout

    private lateinit var calendarBottomSheet: CloseableBottomSheetDialog
    private lateinit var calendarView: View

    private var startDate: Date? = null
    private var endDate: Date? = null

    override val ctx: Context?
        get() = context

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
            tvTotalSaldo = findViewById(R.id.tv_total_saldo)
            tvAffiliateIncome = findViewById(R.id.tv_affiliate_income)
            tvTotalViewed = findViewById(R.id.tv_total_viewed)
            tvTotalClicked = findViewById(R.id.tv_total_clicked)
            tlProductBought = findViewById(R.id.tl_product_bought)
            vpProductBought = findViewById(R.id.vp_product_bought)
            llStartDate = findViewById(R.id.ll_start_date)
            llEndDate = findViewById(R.id.ll_end_date)
        }
    }

    private fun setupView(view: View) {
        fragmentManager?.let {
            vpProductBought.adapter = AffiliateProductBoughtPagerAdapter(it, listOf(
                    AffiliateProductBoughtFragment.newInstance(1),
                    AffiliateProductBoughtFragment.newInstance(2)
            ))
        }
        vpProductBought.layoutParams.height = getScreenHeight() / 2
        tlProductBought.setupWithViewPager(vpProductBought)
        llStartDate.setOnClickListener { openCalendarPicker() }
        llEndDate.setOnClickListener { openCalendarPicker() }
    }

    override fun hideLoading() {

    }

    override fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel) {
        tvTotalSaldo.text = MethodChecker.fromHtml(header.totalSaldoAktif)
        tvAffiliateIncome.text = MethodChecker.fromHtml(header.saldoString)
        tvTotalViewed.text = MethodChecker.fromHtml(header.seenCount)
        tvTotalClicked.text = MethodChecker.fromHtml(header.clickCount)
    }

    override fun onErrorCheckAffiliate(error: String) {
        NetworkErrorHelper.showEmptyState(activity,
                view,
                error
        ) { presenter.checkAffiliate() }
    }

    override fun onSuccessCheckAffiliate(isAffiliate: Boolean) {
        if (isAffiliate) presenter.loadDashboardDetail()
        else closePage()
    }

    override fun onUserNotLoggedIn() {
        closePage()
    }

    private fun openCalendarPicker() {
        if (!::calendarView.isInitialized) calendarView = initCalendarView()
        if (!::calendarBottomSheet.isInitialized) calendarBottomSheet = initCalendarBottomSheet()
        calendarBottomSheet.show()
    }

    private fun initCalendarView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_af_filter_date, null, false)
        val ivClose = view.findViewById<AppCompatImageView>(R.id.iv_close)
        ivClose.setOnClickListener { calendarBottomSheet.dismiss() }
        return view
    }

    private fun initCalendarBottomSheet(): CloseableBottomSheetDialog {
        val unifyCalendar = calendarView.findViewById<UnifyCalendar>(R.id.uc_filter)
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
        }
        return bottomSheet
    }

    private fun initCalendar(calendar: UnifyCalendar) {
        val pickerView = calendar.calendarPickerView
        val calendarPickerView = pickerView.init(getMinDate(), getMaxDate(), emptyList())
                .inMode(CalendarPickerView.SelectionMode.RANGE)

        if (startDate != null && endDate != null) {
            calendarPickerView.withSelectedDates(listOf(startDate, endDate))
        } else pickerView.scrollToDate(Calendar.getInstance().time)
    }

    private fun getMinDate(): Date {
        val minDate = "2005-01-01"
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormatter.parse(minDate)
    }

    private fun getMaxDate(): Date {
        val currentTime = Calendar.getInstance()
        currentTime.set(Calendar.DAY_OF_MONTH, currentTime.getActualMaximum(Calendar.DAY_OF_MONTH))
        currentTime.set(Calendar.HOUR_OF_DAY, 0)
        currentTime.set(Calendar.MINUTE, 0)
        currentTime.set(Calendar.SECOND, 0)
        currentTime.set(Calendar.MILLISECOND, 0)
        return currentTime.time
    }


    private fun closePage() = activity?.finish()
}