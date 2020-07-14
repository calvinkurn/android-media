package com.tokopedia.buyerorder.unifiedhistory.list.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.di.UohComponentInstance
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.END_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.START_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.di.DaggerUohListComponent
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohListItemAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_kebab_menu_uoh.*
import kotlinx.android.synthetic.main.bottomsheet_kebab_menu_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.view.*
import kotlinx.android.synthetic.main.fragment_uoh_list.*
import kotlinx.android.synthetic.main.uoh_empty_list.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * Created by fwidjaja on 29/06/20.
 */
class UohListFragment: BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener,
        UohBottomSheetOptionAdapter.ActionListener, UohListItemAdapter.ActionListener,
        UohBottomSheetKebabMenuAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var uohListItemAdapter: UohListItemAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var refreshHandler: RefreshHandler? = null
    private var paramUohOrder = UohListParam()
    private var orderList: UohListOrder.Data.UohOrders = UohListOrder.Data.UohOrders()
    private lateinit var uohBottomSheetOptionAdapter: UohBottomSheetOptionAdapter
    private lateinit var uohBottomSheetKebabMenuAdapter: UohBottomSheetKebabMenuAdapter
    private var bottomSheetOption: BottomSheetUnify? = null
    private var bottomSheetKebabMenu: BottomSheetUnify? = null
    private var currFilterKey: String = ""
    private var currFilterLabel: String = ""
    private var currFilterType: Int = -1
    private var filter1: SortFilterItem? = null
    private var filter2: SortFilterItem? = null
    private var filter3: SortFilterItem? = null
    private var defaultStartDate = ""
    private var defaultStartDateStr = ""
    private var defaultEndDate = ""
    private var defaultEndDateStr = ""
    private var arrayFilterDate = arrayOf<String>()
    private var onLoadMore = false
    private var currPage = 1
    private var textChangedJob: Job? = null
    private var isReset = false

    private val uohListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[UohListViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(): UohListFragment {
            return UohListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadOrderHistoryList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialValue()
        prepareLayout()
        observingOrderHistory()
    }

    override fun onRefresh(view: View?) {
        onLoadMore = false
        currPage = 1
        uohListItemAdapter.showLoader()
        loadOrderHistoryList()
    }

    private fun setInitialValue() {
        defaultStartDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
        defaultStartDateStr = getCalculatedFormattedDate("dd MMM yyyy", -90)
        defaultEndDate = Date().toFormattedString("yyyy-MM-dd")
        defaultEndDateStr = Date().toFormattedString("dd MMM yyyy")
        paramUohOrder.createTimeStart = defaultStartDate
        paramUohOrder.createTimeEnd = defaultEndDate
        paramUohOrder.page = 1

        arrayFilterDate = resources.getStringArray(R.array.filter_date)
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)
        uohListItemAdapter = UohListItemAdapter()
        uohListItemAdapter.setActionListener(this)

        uohBottomSheetKebabMenuAdapter = UohBottomSheetKebabMenuAdapter(this)

        search_bar?.searchBarTextField?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                textChangedJob?.cancel()
                textChangedJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)
                    paramUohOrder.searchableText = s.toString()
                    refreshHandler?.startRefresh()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        rv_order_list?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = uohListItemAdapter
            scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore = true
                    if (orderList.next.isNotEmpty()) {
                        currentPage += 1
                        uohListItemAdapter.showLoader()
                        loadOrderHistoryList()
                    }
                }
            }
            addOnScrollListener(scrollListener)
        }
    }

    private fun loadOrderHistoryList() {
        paramUohOrder.page = currPage
        uohListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.uoh_get_order_history), paramUohOrder)
    }

    private fun observingOrderHistory() {
        uohListItemAdapter.showLoader()
        uohListViewModel.orderHistoryListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    currPage += 1
                    if (currFilterKey.isEmpty() && currFilterType == -1) {
                        if (orderList.filters.isNotEmpty() && orderList.categories.isNotEmpty()) {
                            renderChipsFilter()
                        }
                    }

                    if (orderList.orders.isNotEmpty()) {
                        renderOrderList()
                    } else {
                        renderEmptyList()
                    }

                    if (orderList.tickers.isNotEmpty()) {
                        renderTicker()
                    } else {
                        ticker_info?.gone()
                    }
                }
                is Fail -> {
                    // renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun renderChipsFilter() {
        val chips = arrayListOf<SortFilterItem>()

        val type = if (isReset) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        filter1 = SortFilterItem(UohConsts.ALL_DATE, type, ChipsUnify.SIZE_MEDIUM)
        filter1?.let {
            it.listener = {
                uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
                showBottomSheetOptions(UohConsts.CHOOSE_DATE)
                val mapKey = HashMap<String, String>()
                var i = 0
                arrayFilterDate.forEach { optionDate ->
                    mapKey["$i"] = optionDate
                    i++
                }
                uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
                uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_DATE
                uohBottomSheetOptionAdapter.selectedKey = currFilterKey
                uohBottomSheetOptionAdapter.isReset = isReset
                uohBottomSheetOptionAdapter.notifyDataSetChanged()
            }
            it.title = arrayFilterDate[2]
            chips.add(it)
        }

        filter2 = SortFilterItem(UohConsts.ALL_STATUS, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter2?.let {
            it.listener = {
                uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
                showBottomSheetOptions(UohConsts.CHOOSE_FILTERS)
                val mapKey = HashMap<String, String>()
                orderList.filters.forEach { option ->
                    mapKey[option] = option
                }
                uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
                uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
                uohBottomSheetOptionAdapter.selectedKey = currFilterKey
                uohBottomSheetOptionAdapter.isReset = isReset
                uohBottomSheetOptionAdapter.notifyDataSetChanged()
            }
            chips.add(it)
        }

        filter3 = SortFilterItem(UohConsts.ALL_CATEGORIES, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter3?.let {
            it.listener = {
                uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
                showBottomSheetOptions(UohConsts.CHOOSE_CATEGORIES)
                val mapKey = HashMap<String, String>()
                orderList.categories.forEach { category ->
                    mapKey[category.value] = category.label
                }
                uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
                uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_CATEGORY
                uohBottomSheetOptionAdapter.selectedKey = currFilterKey
                uohBottomSheetOptionAdapter.isReset = isReset
                uohBottomSheetOptionAdapter.notifyDataSetChanged()
            }
            chips.add(it)
        }

        uoh_sort_filter?.addItem(chips)
        uoh_sort_filter?.sortFilterPrefix?.setOnClickListener {
            isReset = true
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("d MMM yyyy")
            val limitDate = inputFormat.parse(orderList.dateLimit)
            val limitDateStr = outputFormat.format(limitDate)
            val resetMsg = resources.getString(R.string.uoh_reset_filter_msg).replace(UohConsts.DATE_LIMIT, limitDateStr)
            showToaster(resetMsg)

            uoh_sort_filter?.resetAllFilters()
            filter1?.title = UohConsts.ALL_DATE
            filter2?.title = UohConsts.ALL_STATUS
            filter3?.title = UohConsts.ALL_CATEGORIES
            paramUohOrder = UohListParam()
            refreshHandler?.startRefresh()
        }

        filter1?.refChipUnify?.setChevronClickListener {  }
        filter2?.refChipUnify?.setChevronClickListener {  }
        filter3?.refChipUnify?.setChevronClickListener {  }
    }

    private fun renderTicker() {
        ticker_info?.visible()

        if (orderList.tickers.size > 1) {
            val listTickerData = arrayListOf<TickerData>()
            orderList.tickers.forEach {
                var desc = it.text
                if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                    desc += " ${getString(R.string.ticker_info_selengkapnya)
                            .replace(UohConsts.TICKER_URL, it.action.appUrl)
                            .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                }
                listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
            }
            context?.let {
                val adapter = TickerPagerAdapter(it, listTickerData)
                adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                        // TODO : cek lagi url applink nya, make sure lagi nanti
                        RouteManager.route(context, linkUrl.toString())
                    }
                })
                ticker_info?.setDescriptionClickEvent(object: TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                    override fun onDismiss() {
                    }

                })
                ticker_info?.addPagerView(adapter, listTickerData)
            }
        } else {
            orderList.tickers.first().let {
                ticker_info?.tickerTitle = it.title
                var desc = it.text
                if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                    desc += " ${getString(R.string.ticker_info_selengkapnya)
                            .replace(UohConsts.TICKER_URL, it.action.appUrl)
                            .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                }
                ticker_info?.setHtmlDescription(desc)
                ticker_info?.tickerType = UohUtils.getTickerType(it.type)
                ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                    }

                })
            }
        }
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()
        empty_state_order_list?.visibility = View.GONE
        rv_order_list?.visibility = View.VISIBLE

        if (!onLoadMore) {
            uohListItemAdapter.addList(orderList.orders)
        } else {
            uohListItemAdapter.appendList(orderList.orders)
            scrollListener.updateStateAfterGetData()
        }
    }

    private fun renderEmptyList() {
        refreshHandler?.finishRefresh()
        rv_order_list?.visibility = View.GONE
        empty_state_order_list?.visibility = View.VISIBLE
        val searchBarIsNotEmpty = search_bar?.searchBarTextField?.text?.isNotEmpty()
        searchBarIsNotEmpty?.let {
            when {
                it -> {
                    ic_empty?.loadImageDrawable(R.drawable.uoh_empty_search_list)
                    title_empty?.text = resources.getString(R.string.uoh_search_empty)
                    desc_empty?.text = resources.getString(R.string.uoh_search_empty_desc)
                    btn_empty?.gone()

                }
                paramUohOrder.status.isNotEmpty() -> {
                    ic_empty?.loadImageDrawable(R.drawable.uoh_empty_order_list)
                    title_empty?.text = resources.getString(R.string.uoh_filter_empty)
                    desc_empty?.text = resources.getString(R.string.uoh_filter_empty_desc)
                    btn_empty?.visible()
                    btn_empty?.text = resources.getString(R.string.uoh_filter_empty_btn)

                }
                else -> {
                    ic_empty?.loadImageDrawable(R.drawable.uoh_empty_order_list)
                    title_empty?.text = resources.getString(R.string.uoh_no_order)
                    desc_empty?.text = resources.getString(R.string.uoh_no_order_desc)
                    btn_empty?.visible()
                    btn_empty?.text = resources.getString(R.string.uoh_no_order_btn)
                    // TODO : larinya kemana klo mulai belanja? answ : home
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerUohListComponent.builder()
                    .uohComponent(UohComponentInstance.getUohComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    private fun showBottomSheetOptions(title: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_option_uoh, null).apply {
            rv_option?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = uohBottomSheetOptionAdapter
            }

            btn_apply?.setOnClickListener {
                when (currFilterType) {
                    UohConsts.TYPE_FILTER_DATE -> {
                        filter1?.type = ChipsUnify.TYPE_SELECTED
                        if (currFilterKey.toInt() == 3) {
                            if (paramUohOrder.createTimeStart.isNotEmpty() && paramUohOrder.createTimeEnd.isNotEmpty()) {
                                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                                filter1?.title = "${splitStartDate[2]}/${splitStartDate[1]}/${splitStartDate[0]} - ${splitEndDate[2]}/${splitEndDate[1]}/${splitEndDate[0]}"
                            }
                        } else {
                            filter1?.title = currFilterLabel
                        }
                    }
                    UohConsts.TYPE_FILTER_STATUS -> {
                        filter2?.type = ChipsUnify.TYPE_SELECTED
                        filter2?.title = currFilterLabel
                    }
                    UohConsts.TYPE_FILTER_CATEGORY -> {
                        filter3?.type = ChipsUnify.TYPE_SELECTED
                        filter3?.title = currFilterLabel
                    }
                }
                bottomSheetOption?.dismiss()
                refreshHandler?.startRefresh()
            }
        }

        bottomSheetOption = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(title)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetOption?.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showBottomSheetKebabMenu(title: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_kebab_menu_uoh, null)
        viewBottomSheet.rv_kebab.adapter = uohBottomSheetKebabMenuAdapter
        viewBottomSheet.rv_kebab.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        bottomSheetKebabMenu = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(title)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetKebabMenu?.show(it, getString(R.string.show_bottomsheet)) }
    }

    override fun onOptionItemClick(option: String, label: String, filterType: Int) {
        currFilterKey = option
        currFilterLabel = label
        currFilterType = filterType

        when (filterType) {
            UohConsts.TYPE_FILTER_DATE -> {
                when {
                    option.toInt() == 0 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        paramUohOrder.createTimeStart = ""
                        paramUohOrder.createTimeEnd = ""

                    }
                    option.toInt() == 1 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        val startDate = getCalculatedFormattedDate("yyyy-MM-dd", -30)
                        val endDate= Date().toFormattedString("yyyy-MM-dd")
                        paramUohOrder.createTimeStart = startDate
                        paramUohOrder.createTimeEnd = endDate

                    }
                    option.toInt() == 2 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        val startDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
                        val endDate= Date().toFormattedString("yyyy-MM-dd")
                        paramUohOrder.createTimeStart = startDate
                        paramUohOrder.createTimeEnd = endDate

                    }
                    option.toInt() == 3 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.visible()
                            tf_start_date?.textFieldInput?.setText(defaultStartDateStr)
                            tf_start_date?.textFieldInput?.isFocusable = false
                            tf_start_date?.textFieldInput?.isClickable = true
                            tf_start_date?.textFieldInput?.setOnClickListener { showDatePicker(START_DATE) }

                            tf_end_date?.textFieldInput?.setText(defaultEndDateStr)
                            tf_end_date?.textFieldInput?.isFocusable = false
                            tf_end_date?.textFieldInput?.isClickable = true
                            tf_end_date?.textFieldInput?.setOnClickListener { showDatePicker(END_DATE) }
                        }
                    }
                }
            }
            UohConsts.TYPE_FILTER_STATUS -> {
                paramUohOrder.status = option
            }
            UohConsts.TYPE_FILTER_CATEGORY -> {
                paramUohOrder.verticalCategory = option
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(flag: String) {
        context?.let {  context ->
            val minDate = Calendar.getInstance()

            val resultMinDate = orderList.dateLimit.split('-')
            minDate.set(Calendar.YEAR, resultMinDate[0].toInt())
            minDate.set(Calendar.MONTH, resultMinDate[1].toInt())
            minDate.set(Calendar.DATE, resultMinDate[2].toInt())
            val maxDate = Calendar.getInstance()
            val isEndDateFilled = paramUohOrder.createTimeEnd.isNotEmpty()
            if (isEndDateFilled && flag.equals(START_DATE, true)) {
                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                maxDate.set(splitEndDate[0].toInt(), splitEndDate[1].toInt() - 1, splitEndDate[2].toInt())
            }
            val isStartDateFilled = paramUohOrder.createTimeStart.isNotEmpty()
            if (isStartDateFilled && flag.equals(END_DATE, true)) {
                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                minDate.set(splitStartDate[0].toInt(), splitStartDate[1].toInt() - 1, splitStartDate[2].toInt())
            }

            val currentDate = Calendar.getInstance()

            val splitDate: List<String>? = if (flag.equals(START_DATE, true)) {
                paramUohOrder.createTimeStart.split('-')
            } else {
                paramUohOrder.createTimeEnd.split('-')
            }

            splitDate?.let {
                currentDate.set(it[0].toInt(), it[1].toInt()-1, it[2].toInt())
                val datePicker = DatePickerUnify(context, minDate, currentDate, maxDate)
                fragmentManager?.let { it1 -> datePicker.show(it1, "") }
                datePicker.datePickerButton.setOnClickListener {
                    val resultDate = datePicker.getDate()
                    val monthInt = resultDate[1]+1
                    var monthStr = monthInt.toString()
                    if (monthStr.length == 1) monthStr = "0$monthStr"

                    var dateStr = resultDate[0].toString()
                    if (dateStr.length == 1) dateStr = "0$dateStr"

                    if (flag.equals(START_DATE, true)) {
                        paramUohOrder.createTimeStart = "${resultDate[2]}-$monthStr-$dateStr"
                        bottomSheetOption?.tf_start_date?.textFieldInput?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        defaultStartDateStr = "$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}"
                    } else {
                        paramUohOrder.createTimeEnd = "${resultDate[2]}-$monthStr-$dateStr"
                        bottomSheetOption?.tf_end_date?.textFieldInput?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        defaultEndDateStr = "$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}"
                    }
                    datePicker.dismiss()
                }
                if (flag.equals(START_DATE, true)) {
                    datePicker.setTitle(getString(R.string.uoh_custom_start_date))
                } else {
                    datePicker.setTitle(getString(R.string.uoh_custom_end_date))
                }
                datePicker.setCloseClickListener { datePicker.dismiss() }
            }
        }
    }

    private fun showToaster(message: String) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, "")
        }
    }

    override fun onKebabMenuClicked(listDotMenu: List<UohListOrder.Data.UohOrders.Order.Metadata.DotMenu>) {
        showBottomSheetKebabMenu(UohConsts.OTHERS)
        uohBottomSheetKebabMenuAdapter.addList(listDotMenu)
    }

    override fun onKebabItemClick(appUrl: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, appUrl))
    }

    override fun onListItemClicked(verticalCategory: String, verticalId: String, upstream: String) {
        if (verticalCategory.equals(UohConsts.LS_PRINT_VERTICAL_CATEGORY, true)) {
            val url = "m.tokopedia.com/order-details/lsprint/$verticalId&upstream=$upstream"
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        } else {
            val applink = "${UohConsts.APPLINK_BASE}${UohConsts.APPLINK_PATH_ORDER}/$verticalId?${UohConsts.APPLINK_PATH_UPSTREAM}$upstream"
            RouteManager.route(context, applink)
        }
    }

    override fun onActionButtonClicked(url: String) {
        RouteManager.route(context, url)
    }
}