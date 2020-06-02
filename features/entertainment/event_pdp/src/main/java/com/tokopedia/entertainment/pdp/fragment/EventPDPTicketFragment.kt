package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactoryImp
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPTicketViewModel
import kotlinx.android.synthetic.main.ent_ticket_listing_fragment.*
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.START_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.SELECTED_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.END_DATE
import com.tokopedia.entertainment.pdp.adapter.BaseListPackageAdapter
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_ticket_listing_activity.*
import kotlinx.android.synthetic.main.widget_event_pdp_calendar.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class EventPDPTicketFragment: BaseListFragment<EventPDPTicketModel, PackageTypeFactory>() {

    private var urlPDP  = ""
    private var startDate = ""
    private var endDate = ""
    private var selectedDate = ""
    private var EXTRA_PACKAGES_ID = ""
    private var AMOUNT_TICKET = ""
    private var PRODUCT_NAME = ""
    private var PRODUCT_ID = ""
    private var PRODUCT_PRICE = ""
    lateinit var bottomSheets: BottomSheetUnify
    private var packageTypeFactoryImp = PackageTypeFactoryImp()

    @Inject
    lateinit var viewModel: EventPDPTicketViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    override fun getScreenName(): String = ""
    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): PackageTypeFactory = packageTypeFactoryImp

    override fun onItemClicked(p0: EventPDPTicketModel?) {}

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_viewParent

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun loadData(p0: Int) {
        viewModel.getData(resources,urlPDP, selectedDate, swipe_refresh_layout.isRefreshing)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_ticket_listing_fragment, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        urlPDP = arguments?.getString(EXTRA_URL_PDP, "") ?: ""
        startDate = arguments?.getString(START_DATE, "") ?: ""
        selectedDate = arguments?.getString(SELECTED_DATE, "") ?: ""
        endDate = arguments?.getString(END_DATE, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun createAdapterInstance(): BaseListAdapter<EventPDPTicketModel, PackageTypeFactory> {
        return BaseListPackageAdapter(packageTypeFactoryImp, ::setSelectedPackages, eventPDPTracking)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        observeData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setSelectedPackages(idPackages: String, idGroup:String, nominal: String, qty: String, isError: Boolean, product_name: String, product_id: String, price: String){
        this.EXTRA_PACKAGES_ID = idPackages
        viewModel.EXTRA_GROUPS_ID = idGroup
        this.AMOUNT_TICKET = qty
        this.PRODUCT_NAME = product_name
        this.PRODUCT_ID = product_id
        this.PRODUCT_PRICE = price
        setTotalPrice(nominal)
        showViewBottom(!isError)
    }

    private fun setTotalPrice(nominal: String){ txtTotalHarga.text = nominal }

    private fun setupView(){
        setupTicker()
        setupRecycler()
        setupSwipeRefresh()
        setupHeader()
        setupPilihTicketButton()
    }

    private fun setupTicker(){
        activity?.pdp_ticket_ticker?.setTextDescription(getSpannableText())
    }

    private fun getSpannableText(): SpannableStringBuilder{
        val spannable = SpannableStringBuilder(String.format(resources.getString(R.string.ent_pdp_ticket_tickertext)))
        spannable.setSpan(StyleSpan(Typeface.BOLD),15, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    private fun setupRecycler(){
        recycler_viewParent.apply {
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    private fun setupSwipeRefresh(){
        swipe_refresh_layout.apply {
            setOnRefreshListener {
                showViewTop(false)
                showViewBottom(false)
                showUbah(false)
                loadInitialData()
            }
        }
    }

    private fun setupBottomSheet(){
        if(startDate.isNotBlank() && endDate.isNotBlank() && selectedDate.isNotBlank()){
            bottomSheets = BottomSheetUnify()
            val view = LayoutInflater.from(context).inflate(R.layout.widget_event_pdp_calendar, null)
            view.bottom_sheet_calendar.run {
                calendarPickerView?.init(Date(startDate.toLong() * 1000), Date(endDate.toLong()* 1000), ArrayList(), viewModel.listsActiveDate)
                        ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                        ?.withSelectedDate(Date(selectedDate.toLong() * 1000))
                calendarPickerView?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener{
                    override fun onDateSelected(date: Date) {
                        activity?.txtDate?.text = DateFormatUtils.getFormattedDate(date.time, DateFormatUtils.FORMAT_D_MMMM_YYYY)
                        selectedDate = (date.time / 1000L).toString()
                        bottomSheets.dismiss()
                        EXTRA_PACKAGES_ID = ""
                        AMOUNT_TICKET = ""
                        eventPDPTracking.onClickPickDate()
                        showViewBottom(false)
                        loadInitialData()
                    }

                    override fun onDateUnselected(date: Date) {}
                })
            }

            bottomSheets.apply {
                setChild(view)
                setCloseClickListener { bottomSheets.dismiss() }
            }
        }
    }

    private fun setupHeader(){
        activity?.txtDate?.text = getTimestamptoText(selectedDate.isNotBlank())
        if(startDate.isNotBlank() && endDate.isNotBlank()){
            activity?.loaderUbah?.visibility = View.VISIBLE
            setupUbahButton()
        } else activity?.txtUbah?.visibility = View.GONE
    }

    private fun setupPilihTicketButton(){
        pilihTicketBtn.setOnClickListener {
            eventPDPTracking.onClickPesanTiket(viewModel.categoryData,
                    PRODUCT_NAME, PRODUCT_ID, PRODUCT_PRICE, AMOUNT_TICKET.toInt(), EXTRA_PACKAGES_ID)
            if(userSession.isLoggedIn) {
                startActivity(EventCheckoutActivity.createIntent(context!!, urlPDP,
                        viewModel.EXTRA_SCHEDULE_ID, viewModel.EXTRA_GROUPS_ID, EXTRA_PACKAGES_ID, AMOUNT_TICKET.toInt()))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun showUbah(state: Boolean){
        if(startDate.isNotBlank() && endDate.isNotBlank()){
            activity?.loaderUbah?.visibility = if(state) View.GONE else View.VISIBLE
            activity?.txtUbah?.visibility = if(state) View.VISIBLE else View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let{
                    startActivity(EventCheckoutActivity.createIntent(it, urlPDP,
                            viewModel.EXTRA_SCHEDULE_ID, viewModel.EXTRA_GROUPS_ID, EXTRA_PACKAGES_ID, AMOUNT_TICKET.toInt()))
                }
            }
        }
    }

    private fun observeData(){

        viewModel.ticketModel.observe(this, Observer {
            clearAllData()
            swipe_refresh_layout.isRefreshing = false
            it?.run { renderList(this) }
            showViewTop(true)
            showUbah(true)
            setupBottomSheet()
        })

        viewModel.error.observe(this, Observer {
            NetworkErrorHelper.createSnackbarRedWithAction(activity, String.format(resources.getString(R.string.ent_error_network_message))){
                showViewTop(false)
                showViewBottom(false)
                loadInitialData()
            }.showRetrySnackbar()
        })
    }

    private fun showViewTop(state: Boolean){
        pdp_ticket_ticker?.visibility = if(state) View.VISIBLE else View.GONE
        viewTop?.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun showViewBottom(state: Boolean){
        viewBottom?.visibility = if(state) View.VISIBLE else View.GONE
        constraintLayout?.visibility = if(state) View.VISIBLE else View.GONE
        if(!state) txtTotalHarga.text = String.format(resources.getString(R.string.ent_default_totalPrice))
    }

    private fun setupUbahButton(){
        activity?.txtUbah?.setOnClickListener {
            fragmentManager?.let {
                bottomSheets.show(it, "")
            }
        }
    }

    private fun getTimestamptoText(state: Boolean): String{
        return DateFormatUtils.getFormattedDate(if(state) selectedDate else getTodayDates(), DateFormatUtils.FORMAT_DD_MMMM_YYYY)
    }

    private fun getTodayDates(): String = (Date().time / 1000L).toString()

    companion object{
        fun newInstance(url: String, selectedDate:String, startDate: String, endDate: String) = EventPDPTicketFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, url)
                putString(START_DATE, startDate)
                putString(SELECTED_DATE, selectedDate)
                putString(END_DATE, endDate)
            }
        }
        val EMPTY_VALUE = "-"
        val REQUEST_CODE_LOGIN = 100
    }

}