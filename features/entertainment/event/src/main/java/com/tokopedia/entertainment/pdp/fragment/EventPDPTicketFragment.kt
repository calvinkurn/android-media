package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.common.util.EventQuery.mutationVerifyV2
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.END_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.SELECTED_DATE
import com.tokopedia.entertainment.pdp.activity.EventPDPTicketActivity.Companion.START_DATE
import com.tokopedia.entertainment.pdp.adapter.EventPDPParentPackageAdapter
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactoryImpl
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.EventPDPTicket
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.pdp.ItemMap
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.data.pdp.VerifyRequest
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getActiveDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.isMaxDateNotMoreThanSelected
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.isMinDateNotLessThanSelected
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getInitialVerify
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getItemIds
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getItemMap
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getListItemMap
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getTotalPrice
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventVerifyMapper.getTotalQuantity
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPTicketViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_ticket_listing_activity.*
import kotlinx.android.synthetic.main.ent_ticket_listing_fragment.*
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.*
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket_banner.*
import kotlinx.android.synthetic.main.widget_event_pdp_calendar.view.*
import java.util.*
import javax.inject.Inject

class EventPDPTicketFragment : BaseListFragment<EventPDPTicket, PackageTypeFactory>(),
        OnBindItemTicketListener, OnCoachmarkListener {

    private var urlPDP = ""
    private var startDate = ""
    private var endDate = ""
    private var gatewayCode = ""
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
    private var packageTypeFactoryImp = PackageTypeFactoryImpl(this, this)

    private lateinit var recommendationAdapter: EventPDPParentPackageAdapter

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

    override fun onItemClicked(t: EventPDPTicket?) {}

    override fun getRecyclerViewResourceId(): Int = R.id.rvEventTicketList

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun loadData(p0: Int) {
        viewModel.getData(urlPDP, selectedDate, true, EventQuery.eventPDPV3(),
                eventContentById())
    }

    override fun createAdapterInstance(): BaseListAdapter<EventPDPTicket, PackageTypeFactory> {
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

        TimeZone.setDefault(TimeZone.getTimeZone(GMT));
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        observeData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun quantityEditorValueButtonClicked(idPackages: String, idPackagesItem: String, packageItem: PackageItem, totalPrice: Int,
                                                  qty: String, isError: Boolean, product_name: String,
                                                  product_id: String, price: String, selectedDate: String, packageName: String) {
        if (!idPackageActive.equals(idPackages)) {
            hashItemMap.clear()
            idPackageActive = idPackages
        }

        if (qty.toInt() > EMPTY_QTY) {
            hashItemMap.put(idPackagesItem, getItemMap(packageItem, pdpData, qty.toInt(), totalPrice, selectedDate, idPackageActive, packageName))
        } else {
            hashItemMap.remove(idPackagesItem)
        }

        this.PACKAGES_ID = idPackagesItem
        this.AMOUNT_TICKET = getTotalQuantity(hashItemMap)
        this.PRODUCT_NAME = product_name
        this.PRODUCT_ID = product_id
        this.PRODUCT_PRICE = getTotalPrice(hashItemMap).toString()

        if (getTotalQuantity(hashItemMap) > EMPTY_QTY) {
            setTotalPrice(getRupiahFormat(getTotalPrice(hashItemMap)))
            showViewBottom(AMOUNT_TICKET > EMPTY_QTY)
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
        rvEventTicketList.apply {
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

    private fun setupBottomSheet(listActiveDates: List<String>) {
        if (startDate.isNotBlank() && endDate.isNotBlank() && selectedDate.isNotBlank()) {
            bottomSheets = BottomSheetUnify()
            val view = LayoutInflater.from(context).inflate(R.layout.widget_event_pdp_calendar, null)
            view.bottom_sheet_calendar.run {
                if (isMaxDateNotMoreThanSelected(pdpData, selectedDate) && isMinDateNotLessThanSelected(pdpData, selectedDate) && pdpData.dates.size > 1) {
                    calendarPickerView?.init(Date(startDate.toLong() * DATE_MULTIPLICATION), Date(endDate.toLong() * DATE_MULTIPLICATION), listHoliday, getActiveDate(listActiveDates))
                            ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                            ?.withSelectedDate(Date(selectedDate.toLong() * DATE_MULTIPLICATION))
                } else {
                    calendarPickerView?.init(Date(startDate.toLong() * DATE_MULTIPLICATION), Date(endDate.toLong() * DATE_MULTIPLICATION), listHoliday, getActiveDate(listActiveDates))
                            ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                }

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
            eventPDPTracking.onClickPesanTiket(viewModel.categoryData, PACKAGES_ID, getListItemMap(hashItemMap))
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
                    startActivity(EventCheckoutActivity.createIntent(it, urlPDP, metaDataResponse, idPackageActive, gatewayCode))
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
        viewModel.ticketModel.observe(viewLifecycleOwner, Observer {
            val packageV3 = it as? List<PackageV3>
            packageV3?.let { packages ->
                clearAllData()
                swipe_refresh_layout.isRefreshing = false
                packages.run { renderList(listOf(EventPDPTicketGroup(this))) }
                showUbah(true)
            }
        })

        viewModel.recommendationTicketModel.observe(viewLifecycleOwner, Observer {
            it?.run {
                val packageV3 = it as? List<PackageV3>
                packageV3?.let { packages ->
                    if (this.isNotEmpty()) {
                        renderList(listOf(EventPDPTicketBanner()))
                        renderList(listOf(EventPDPTicketGroup(packages)))
                    }
                }
                if(!getLocalCache()) showCoachMark(this)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            NetworkErrorHelper.createSnackbarRedWithAction(activity, String.format(it)) {
                showViewBottom(false)
                loadInitialData()
            }.showRetrySnackbar()
        })

        viewModel.productDetailEntity.observe(viewLifecycleOwner, Observer {
            pdpData = it.eventProductDetail.productDetailData
            eventVerifyRequest = getInitialVerify(pdpData)
            changeLabel()
        })

        viewModel.verifyResponse.observe(viewLifecycleOwner, Observer {
            metaDataResponse = it.eventVerify.metadata
            gatewayCode = it.eventVerify.gatewayCode
            context?.let {
                if (userSession.isLoggedIn) {
                    startActivity(EventCheckoutActivity.createIntent(it, urlPDP, metaDataResponse, idPackageActive, gatewayCode))
                } else {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        })

        viewModel.eventHoliday.observe(viewLifecycleOwner, Observer {
            listHoliday = it
            setupBottomSheet(pdpData.dates)
        })
    }

    private fun showViewBottom(state: Boolean) {
        viewBottom?.visibility = if (state) View.VISIBLE else View.GONE
        containerEventBottom?.visibility = if (state) View.VISIBLE else View.GONE
        if (!state) txtTotalHarga.text = String.format(resources.getString(R.string.ent_default_totalPrice))
    }

    private fun setupUbahButton() {
        activity?.txtUbah?.setOnClickListener {
            showUpBottomSheet()
        }
    }

    private fun getTimestamptoText(state: Boolean): String {
        return DateFormatUtils.getFormattedDate(if (state) selectedDate else getTodayDates(), DATE_TICKET)
    }

    private fun getDateTicketFormatted(date: String): String {
        return DateFormatUtils.getFormattedDate(date, DATE_TICKET)
    }

    private fun getTodayDates(): String = (Date().time / 1000L).toString()

    override fun getSelectedDate(): String {
        return selectedDate
    }

    override fun getLocalCache(): Boolean {
        return localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, false)
    }

    fun showCoachMark(listRecom: List<EventPDPTicketModel>) {
        Handler().run {
            postDelayed({
                accordionEventPDPTicket.expandGroup(0)
            }, EXPAND_ACCORDION_START_DELAY)
            postDelayed({
                context?.let {
                    val coachMark = CoachMark2(it)
                    coachMark.apply {
                        showCoachMark(ArrayList(getCoachmarkItem(listRecom)), null, 0)
                        setStepListener(object : CoachMark2.OnStepListener{
                            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                                if(currentIndex == 1){
                                    val position = tgEventTicketRecommendationTitle.y
                                    scroll_ticket_pdp.smoothScrollTo(0, position.toInt())
                                }
                            }
                        })
                    }
                }
                localCacheHandler.apply {
                    putBoolean(SHOW_COACH_MARK_KEY, true)
                    applyEditor()
                }
            }, COACH_MARK_START_DELAY)
        }
    }

    fun getCoachmarkItem(listRecom: List<EventPDPTicketModel>): List<CoachMark2Item> {
        val coachmarkList: MutableList<CoachMark2Item> = mutableListOf()
        activity?.let { _activity ->
            if (isAdded) {
                checkAvailableCoachmark()?.let {
                    coachmarkList.add(0,
                            CoachMark2Item(
                                    it,
                                    getString(R.string.ent_home_coachmark_title),
                                    getString(R.string.ent_home_coachmark_subtitle),
                                    CoachMark2.POSITION_BOTTOM
                            )
                    )
                }
                if (listRecom.isNotEmpty()) {
                    rvEventTicketList.findViewHolderForAdapterPosition(2)?.itemView?.let {
                        val recomAccordion = it.findViewById<AccordionUnify>(R.id.accordionEventPDPTicket)
                        recomAccordion.expandGroup(0)
                        coachmarkList.add(if(checkAvailableCoachmark() != null) 1 else 0,
                                CoachMark2Item(
                                        it,
                                        getString(R.string.ent_home_coachmark_title_recom),
                                        getString(R.string.ent_home_coachmark_subtitle_recom),
                                        CoachMark2.POSITION_TOP
                                )
                        )
                    }
                }
            }
        }
        return coachmarkList
    }

    override fun clickRecommendation(list: List<String>) {
        setupBottomSheet(list)
        showUpBottomSheet()
    }

    private fun showUpBottomSheet() {
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    private fun changeLabel() {
        val bitwiseIsHiburan = pdpData.customText1.toInt() and IS_HIBURAN
        if (pdpData.dates.size > 1) {
            activity?.txtPlaceHolderTglKunjungan?.text = resources.getString(R.string.ent_pdp_tanggal_kunjungan)
        } else if (bitwiseIsHiburan > 0) {
            activity?.txtPlaceHolderTglKunjungan?.text = resources.getString(R.string.ent_pdp_berlaku_hingga)
            activity?.txtDate?.text = getDateTicketFormatted(pdpData.maxEndDate)
        } else {
            activity?.txtPlaceHolderTglKunjungan?.text = resources.getString(R.string.ent_pdp_tanggal_kunjungan)
            activity?.txtDate?.text = getDateTicketFormatted(pdpData.dates.first())
        }
    }

    private fun getLayoutCoachmark(id: Int): View?{
        val accordion = rvEventTicketList.findViewHolderForAdapterPosition(0)?.itemView
                ?.findViewById<AccordionUnify>(R.id.accordionEventPDPTicket)
        return accordion?.getChildAt(0)
                ?.findViewById<RecyclerView>(R.id.rv_accordion_expandable)
                ?.findViewHolderForLayoutPosition(0)?.itemView
                ?.findViewById<Typography>(id)
    }

    private fun checkAvailableCoachmark():View? {
        return when{
            getLayoutCoachmark(R.id.txtPilih_ticket)!=null -> getLayoutCoachmark(R.id.txtPilih_ticket)
            getLayoutCoachmark(R.id.txtHabis_ticket)!=null -> getLayoutCoachmark(R.id.txtHabis_ticket)
            getLayoutCoachmark(R.id.txtNotStarted)!=null -> getLayoutCoachmark(R.id.txtNotStarted)
            getLayoutCoachmark(R.id.txtAlreadyEnd)!=null -> getLayoutCoachmark(R.id.txtAlreadyEnd)
            else -> null
        }
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
        const val IS_HIBURAN = 8192
        const val DATE_TICKET = "EEE, dd MMM yyyy"
        const val GMT = "GMT+7"

        const val PREFERENCES_NAME = "event_ticket_preferences"
        const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_event_ticket"
        private const val COACH_MARK_START_DELAY = 650L
        private const val EXPAND_ACCORDION_START_DELAY = 500L
    }

}