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
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.common.util.EventQuery.mutationVerifyV2
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPTicketViewModel
import kotlinx.android.synthetic.main.ent_ticket_listing_fragment.*
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.START_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.SELECTED_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.END_DATE
import com.tokopedia.entertainment.pdp.adapter.EventPDPParentPackageAdapter
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactoryImpl
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.pdp.*
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getActiveDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getInitialVerify
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getItemIds
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getItemMap
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getListItemMap
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getTotalPrice
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getTotalQuantity
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.custom_event_expandable_parent.view.*
import kotlinx.android.synthetic.main.ent_ticket_listing_activity.*
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.view.*
import kotlinx.android.synthetic.main.widget_event_pdp_calendar.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EventPDPTicketFragment : BaseListFragment<EventPDPTicketModel, PackageTypeFactory>(),
        OnBindItemTicketListener, OnCoachmarkListener{

    private var urlPDP = ""
    private var startDate = ""
    private var endDate = ""
    private var selectedDate = ""
    private var PACKAGES_ID = ""
    private var AMOUNT_TICKET = 0
    private var PRODUCT_NAME = ""
    private var PRODUCT_ID = ""
    private var PRODUCT_PRICE = ""
    private var idPackageActive = ""
    private var metaDataResponse = MetaDataResponse()
    private var listHoliday: List<Legend> = arrayListOf()
    lateinit var bottomSheets: BottomSheetUnify
    private var eventVerifyRequest: VerifyRequest = VerifyRequest()
    private var hashItemMap: HashMap<String, ItemMap> = hashMapOf()
    private var pdpData: ProductDetailData = ProductDetailData()
    private var packageTypeFactoryImp = PackageTypeFactoryImpl(this,this)

    @Inject
    lateinit var viewModel: EventPDPTicketViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    private lateinit var localCacheHandler: LocalCacheHandler

    override fun getScreenName(): String = ""
    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): PackageTypeFactory = packageTypeFactoryImp

    override fun onItemClicked(p0: EventPDPTicketModel?) {}

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_viewParent

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun loadData(p0: Int) {
        viewModel.getData(urlPDP, selectedDate, swipe_refresh_layout.isRefreshing, EventQuery.eventPDPV3(),
                eventContentById())
    }

    override fun createAdapterInstance(): BaseListAdapter<EventPDPTicketModel, PackageTypeFactory> {
        return EventPDPParentPackageAdapter(packageTypeFactoryImp, eventPDPTracking)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_ticket_listing_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        urlPDP = arguments?.getString(EXTRA_URL_PDP, "") ?: ""
        startDate = arguments?.getString(START_DATE, "") ?: ""
        selectedDate = arguments?.getString(SELECTED_DATE, "") ?: ""
        endDate = arguments?.getString(END_DATE, "") ?: ""
        super.onCreate(savedInstanceState)

        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        observeData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun quantityEditorValueButtonClicked(idPackages: String, idPackagesItem: String, packageItem: PackageItem, totalPrice: Int,
                                                  qty: String, isError: Boolean, product_name: String,
                                                  product_id: String, price: String, selectedDate: String, packageName:String) {
        if (!idPackageActive.equals(idPackages)) {
            hashItemMap.clear()
            idPackageActive = idPackages
        }

        if(qty.toInt() > EMPTY_QTY) {
            hashItemMap.put(idPackagesItem, getItemMap(packageItem, pdpData, qty.toInt(), totalPrice, selectedDate, idPackageActive, packageName))
        } else {
            hashItemMap.remove(idPackagesItem)
        }

        this.PACKAGES_ID = idPackagesItem
        this.AMOUNT_TICKET = getTotalQuantity(hashItemMap)
        this.PRODUCT_NAME = product_name
        this.PRODUCT_ID = product_id
        this.PRODUCT_PRICE = getTotalPrice(hashItemMap).toString()

        if (getTotalPrice(hashItemMap) != EMPTY_QTY) {
            setTotalPrice(getRupiahFormat(getTotalPrice(hashItemMap)))
            showViewBottom(!isError)
        } else {
            showViewBottom(false)
        }
    }

    private fun setTotalPrice(nominal: String) {
        txtTotalHarga.text = nominal
    }

    private fun setupView() {
        setupRecycler()
        setupSwipeRefresh()
        setupHeader()
        setupPilihTicketButton()
    }

    private fun setupRecycler() {
        recycler_viewParent.apply {
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    private fun setupSwipeRefresh() {
        swipe_refresh_layout.apply {
            setOnRefreshListener {
                showViewBottom(false)
                showUbah(false)
                loadInitialData()
            }
        }
    }

    private fun setupBottomSheet() {
        if (startDate.isNotBlank() && endDate.isNotBlank() && selectedDate.isNotBlank()) {
            bottomSheets = BottomSheetUnify()
            val view = LayoutInflater.from(context).inflate(R.layout.widget_event_pdp_calendar, null)
            view.bottom_sheet_calendar.run {
                calendarPickerView?.init(Date(startDate.toLong() * DATE_MULTIPLICATION), Date(endDate.toLong() * DATE_MULTIPLICATION), listHoliday, getActiveDate(pdpData))
                        ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                        ?.withSelectedDate(Date(selectedDate.toLong() * DATE_MULTIPLICATION))
                calendarPickerView?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                    override fun onDateSelected(date: Date) {
                        activity?.txtDate?.text = DateFormatUtils.getFormattedDate(date.time, DateFormatUtils.FORMAT_D_MMMM_YYYY)
                        selectedDate = (date.time / 1000L).toString()
                        bottomSheets.dismiss()
                        PACKAGES_ID = ""
                        AMOUNT_TICKET = EMPTY_QTY
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

    private fun setupHeader() {
        activity?.txtDate?.text = getTimestamptoText(selectedDate.isNotBlank())
        if (startDate.isNotBlank() && endDate.isNotBlank()) {
            activity?.loaderUbah?.visibility = View.VISIBLE
            setupUbahButton()
        } else activity?.txtUbah?.visibility = View.GONE
    }

    private fun setupPilihTicketButton() {
        pilihTicketBtn.setOnClickListener {
            eventVerifyRequest.cartdata.metadata.totalPrice = getTotalPrice(hashItemMap)
            eventVerifyRequest.cartdata.metadata.itemIds = getItemIds(hashItemMap)
            eventVerifyRequest.cartdata.metadata.itemMaps = getListItemMap(hashItemMap)
            eventVerifyRequest.cartdata.metadata.quantity = getTotalQuantity(hashItemMap)
            eventPDPTracking.onClickPesanTiket(viewModel.categoryData,PACKAGES_ID,getListItemMap(hashItemMap))
            viewModel.verify(mutationVerifyV2(), eventVerifyRequest)
        }
    }

    private fun showUbah(state: Boolean) {
        if (startDate.isNotBlank() && endDate.isNotBlank()) {
            activity?.loaderUbah?.visibility = if (state) View.GONE else View.VISIBLE
            activity?.txtUbah?.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let {
                    startActivity(EventCheckoutActivity.createIntent(it, urlPDP, metaDataResponse, idPackageActive))
                }
            }
        }
    }

    override fun resetPackage() {
        hashItemMap.clear()
        idPackageActive = ""
        setTotalPrice(getRupiahFormat(EMPTY_QTY))
        showViewBottom(false)
    }

    private fun observeData() {
        viewModel.ticketModel.observe(this, Observer {
            clearAllData()
            swipe_refresh_layout.isRefreshing = false
            it?.run { renderList(this) }
            showUbah(true)
        })

        viewModel.error.observe(this, Observer {
            NetworkErrorHelper.createSnackbarRedWithAction(activity, String.format(it)) {
                showViewBottom(false)
                loadInitialData()
            }.showRetrySnackbar()
        })

        viewModel.productDetailEntity.observe(this, Observer {
            pdpData = it.eventProductDetail.productDetailData
            eventVerifyRequest = getInitialVerify(pdpData)

        })

        viewModel.verifyResponse.observe(this, Observer {
            metaDataResponse = it.eventVerify.metadata
            if (userSession.isLoggedIn) {
                startActivity(EventCheckoutActivity.createIntent(context!!, urlPDP, metaDataResponse, idPackageActive))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
        })

        viewModel.eventHoliday.observe(this, Observer {
            listHoliday = it
            setupBottomSheet()
        })
    }

    private fun showViewBottom(state: Boolean) {
        viewBottom?.visibility = if (state) View.VISIBLE else View.GONE
        constraintLayout?.visibility = if (state) View.VISIBLE else View.GONE
        if (!state) txtTotalHarga.text = String.format(resources.getString(R.string.ent_default_totalPrice))
    }

    private fun setupUbahButton() {
        activity?.txtUbah?.setOnClickListener {
            fragmentManager?.let {
                bottomSheets.show(it, "")
            }
        }
    }

    private fun getTimestamptoText(state: Boolean): String {
        return DateFormatUtils.getFormattedDate(if (state) selectedDate else getTodayDates(), DATE_TICKET)
    }

    private fun getTodayDates(): String = (Date().time / 1000L).toString()

    override fun getSelectedDate(): String {
        return selectedDate
    }

    override fun getLocalCache(): Boolean {
        return localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, true)
    }

    override fun showCoachMark(view: View, height:Int) {
        val coachMark = CoachMarkBuilder().build().apply {
            enableSkip = true
        }
        coachMark.setHighlightMargin(marginTop = height)
        coachMark.show(
                activity,
                EventPDPTicketFragment::class.java.simpleName,
                getCoachMarkItems(view)
        )
        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, false)
            applyEditor()
        }
    }

    private fun getCoachMarkItems(view: View):ArrayList<CoachMarkItem>{
        return arrayListOf(CoachMarkItem(
                view,
                getString(R.string.ent_home_coachmark_title),
                getString(R.string.ent_home_coachmark_subtitle),
                CoachMarkContentPosition.BOTTOM
        ))
    }


    companion object {
        fun newInstance(url: String, selectedDate: String, startDate: String, endDate: String) = EventPDPTicketFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, url)
                putString(START_DATE, startDate)
                putString(SELECTED_DATE, selectedDate)
                putString(END_DATE, endDate)
            }
        }

        val EMPTY_VALUE = "-"
        val EMPTY_QTY = 0
        val REQUEST_CODE_LOGIN = 100
        const val DATE_MULTIPLICATION = 1000
        const val DATE_TICKET = "EEE, dd MMM yyyy"

        private const val PREFERENCES_NAME = "event_ticket_preferences"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_event_ticket"
        private const val COACH_MARK_START_DELAY = 1000L
    }

}