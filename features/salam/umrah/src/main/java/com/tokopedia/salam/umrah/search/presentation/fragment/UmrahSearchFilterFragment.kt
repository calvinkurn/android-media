package com.tokopedia.salam.umrah.search.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahOption
import com.tokopedia.salam.umrah.common.data.PriceRangeLimit
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.di.UmrahSearchComponent
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_CITY_ID
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_PERIOD
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MIN
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MIN
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchFilterAdapter
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchSortAdapter
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchFilterSortViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheets_umrah_search_sort.view.*
import kotlinx.android.synthetic.main.fragment_umrah_search_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by M on 18/10/2019
 */

class UmrahSearchFilterFragment : BaseDaggerFragment() {
    @Inject
    lateinit var umrahSearchFilterSortViewModel: UmrahSearchFilterSortViewModel

    private val departurePeriodAdapter: UmrahSearchFilterAdapter by lazy { UmrahSearchFilterAdapter() }
    private val departureCityAdapter: UmrahSearchFilterAdapter by lazy { UmrahSearchFilterAdapter() }
    private val bottomSheetdeparturePeriodAdapter: UmrahSearchSortAdapter by lazy { UmrahSearchSortAdapter() }
    private val bottomSheetdepartureCityAdapter: UmrahSearchSortAdapter by lazy { UmrahSearchSortAdapter() }
    private lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher
    private lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    private var scrollPositionDepartureCity = 0
    private var scrollPositionDeparturePeriod = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getComponent(UmrahSearchComponent::class.java).inject(this)
        loadData()
    }

    private fun loadData() {
        val searchQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_home_page_search_parameter)
        umrahSearchFilterSortViewModel.getUmrahSearchParameter(searchQuery)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahSearchFilterSortViewModel.umrahSearchParameter.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> Fail(it.throwable)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_umrah_search_filter_save.setOnClickListener {
            saveScrollPosition()
            saveAndPutSelectedFilter()
        }
    }

    private fun saveAndPutSelectedFilter() {
        selectedFilter.departurePeriod = departurePeriodAdapter.getSelectedItem()
        selectedFilter.departureCity = departureCityAdapter.getSelectedItem()
        selectedFilter.durationDaysMinimum = sr_umrah_search_filter_travel_duration.getMinValue()
        selectedFilter.durationDaysMaximum = sr_umrah_search_filter_travel_duration.getMaxValue()
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_DEPARTURE_CITY_ID, selectedFilter.departureCity)
                putExtra(EXTRA_DEPARTURE_PERIOD, selectedFilter.departurePeriod)
                putExtra(EXTRA_PRICE_MIN, selectedFilter.priceMinimum)
                putExtra(EXTRA_PRICE_MAX, selectedFilter.priceMaximum)
                putExtra(EXTRA_DURATION_DAYS_MIN, selectedFilter.durationDaysMinimum)
                putExtra(EXTRA_DURATION_DAYS_MAX, selectedFilter.durationDaysMaximum)
            })
            finish()
        }
    }

    private fun saveScrollPosition() {
        scrollPositionDepartureCity = (rv_umrah_search_filter_departure_city.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        scrollPositionDeparturePeriod = (rv_umrah_search_filter_departure_period.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    private fun restoreScrollPosition() {
        if (rv_umrah_search_filter_departure_period.layoutManager != null) {
            (rv_umrah_search_filter_departure_city.layoutManager as LinearLayoutManager).scrollToPosition(scrollPositionDepartureCity)
            (rv_umrah_search_filter_departure_period.layoutManager as LinearLayoutManager).scrollToPosition(scrollPositionDeparturePeriod)
        }
    }

    private fun onSuccessGetResult(data: UmrahSearchParameterEntity) {
        data.umrahSearchParameter.also {
            if (selectedFilter.departureCity == "-" && selectedFilter.departurePeriod == "-") {
                selectedFilter.departurePeriod = it.departurePeriods.options[it.departurePeriods.defaultOption].query
                selectedFilter.departureCity = it.depatureCities.options[it.depatureCities.defaultOption].query
            }
            setupFilterDeparturePeriod(it.departurePeriods.options)
            setupFilterDepartureCity(it.depatureCities.options)
            setupFilterDurationDaysRange(it.durationDaysRangeLimit)
            setupFilterPriceRange(it.priceRangeLimit)
        }
        restoreScrollPosition()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_umrah_search_filter, container, false)
    }

    private fun setupFilterDeparturePeriod(departurePeriodOptions: List<UmrahOption>) {
        rv_umrah_search_filter_departure_period.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                    RecyclerView.HORIZONTAL))
            adapter = departurePeriodAdapter
        }
        departurePeriodAdapter.apply {
            addOptions(departurePeriodOptions)
            setSelectedOption(selectedFilter.departurePeriod)
            listener = object : UmrahSearchFilterAdapter.OnItemSelected {
                override fun onSelect(option: UmrahOption) {
                    selectedFilter.departurePeriod = option.query
                }
            }
        }
        bottomSheetdeparturePeriodAdapter.apply {
            addOptions(departurePeriodOptions)
            setSelectedOption(selectedFilter.departurePeriod)
        }
        tg_umrah_search_filter_departure_period_see_all.setOnClickListener {
            openBottomSheet(getString(R.string.umrah_search_filter_departure_period), selectedFilter.departurePeriod, departurePeriodAdapter, bottomSheetdeparturePeriodAdapter)
        }
    }

    private fun setupFilterDepartureCity(departureCityOptions: List<UmrahOption>) {
        rv_umrah_search_filter_departure_city.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                    RecyclerView.HORIZONTAL))
            adapter = departureCityAdapter
        }
        departureCityAdapter.apply {
            addOptions(departureCityOptions)
            setSelectedOption(selectedFilter.departureCity)
            listener = object : UmrahSearchFilterAdapter.OnItemSelected {
                override fun onSelect(option: UmrahOption) {
                    selectedFilter.departureCity = option.query
                }
            }
        }
        bottomSheetdepartureCityAdapter.apply {
            addOptions(departureCityOptions)
            setSelectedOption(selectedFilter.departureCity)
        }
        tg_umrah_search_filter_departure_city_see_all.setOnClickListener {
            openBottomSheet(getString(R.string.umrah_search_filter_departure_city), selectedFilter.departureCity, departureCityAdapter, bottomSheetdepartureCityAdapter)
        }
    }

    private fun setupFilterDurationDaysRange(durationDaysRangeLimit: PriceRangeLimit) {
        if (sr_umrah_search_filter_travel_duration.rangeValue == 0) {
            sr_umrah_search_filter_travel_duration.setRange(durationDaysRangeLimit)
        }
        if (selectedFilter.durationDaysMinimum == 0 && selectedFilter.durationDaysMaximum == 0) {
            selectedFilter.durationDaysMinimum = durationDaysRangeLimit.minimum
            selectedFilter.durationDaysMaximum = durationDaysRangeLimit.maximum
        }
        sr_umrah_search_filter_travel_duration.setData(selectedFilter.durationDaysMinimum, selectedFilter.durationDaysMaximum)
    }

    private fun setupFilterPriceRange(priceRangeLimit: PriceRangeLimit) {
        val minPriceEditText = riv_umrah_search_filter_package_price.minValueEditText
        if (::minCurrencyTextWatcher.isInitialized) {
            minPriceEditText.removeTextChangedListener(minCurrencyTextWatcher)
        }
        minCurrencyTextWatcher = CurrencyTextWatcher(minPriceEditText, CurrencyEnum.RP)
        minPriceEditText.addTextChangedListener(minCurrencyTextWatcher)

        val maxPriceEditText = riv_umrah_search_filter_package_price.maxValueEditText
        if (::maxCurrencyTextWatcher.isInitialized) {
            maxPriceEditText.removeTextChangedListener(maxCurrencyTextWatcher)
        }
        maxCurrencyTextWatcher = CurrencyTextWatcher(maxPriceEditText, CurrencyEnum.RP)
        maxPriceEditText.addTextChangedListener(maxCurrencyTextWatcher)
        if (selectedFilter.priceMaximum == 0 || selectedFilter.priceMaximum == priceRangeLimit.maximum) maxCurrencyTextWatcher.format = getString(R.string.umrah_search_filter_max_string_format_with_plus)
        riv_umrah_search_filter_package_price.setPower(1.0)

        val filteredMinPrice = if (selectedFilter.priceMinimum < priceRangeLimit.minimum) priceRangeLimit.minimum else selectedFilter.priceMinimum
        val filteredMaxPrice = if (selectedFilter.priceMaximum == 0 || selectedFilter.priceMaximum > priceRangeLimit.maximum) priceRangeLimit.maximum else selectedFilter.priceMaximum

        selectedFilter.priceMinimum = filteredMinPrice
        selectedFilter.priceMaximum = filteredMaxPrice
        riv_umrah_search_filter_package_price.setData(priceRangeLimit.minimum, priceRangeLimit.maximum, filteredMinPrice, filteredMaxPrice)
        riv_umrah_search_filter_package_price.setOnValueChangedListener { minValue, maxValue, _, maxBound ->
            selectedFilter.priceMinimum = minValue
            var optMaxValue = maxValue
            if (maxValue == minValue && maxValue < maxBound) optMaxValue++
            selectedFilter.priceMaximum = optMaxValue
            if (selectedFilter.priceMaximum >= maxBound) maxCurrencyTextWatcher.format = getString(R.string.umrah_search_filter_max_string_format_with_plus)
            else maxCurrencyTextWatcher.format = getString(R.string.umrah_search_filter_max_string_format)
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheet(title: String, selectedItem: String, adapter: UmrahSearchFilterAdapter, bottomSheetAdapter: UmrahSearchSortAdapter) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_search_sort, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_umrah_search_sort.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = bottomSheetAdapter
        }
        bottomSheetAdapter.apply {
            setSelectedOption(selectedItem)
            listener = object : UmrahSearchSortAdapter.OnSortMenuSelected {
                override fun onSelect(option: UmrahOption) {
                    var selected = ""
                    when (title) {
                        getString(R.string.umrah_search_filter_departure_period) -> {
                            selected = option.query
                            selectedFilter.departurePeriod = selected
                        }
                        getString(R.string.umrah_search_filter_departure_city) -> {
                            selected = option.query
                            selectedFilter.departureCity = option.query
                        }
                    }
                    adapter.setSelectedOption(selected)
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(250)
                        bottomSheets.dismiss()
                    }
                }
            }
        }
        bottomSheets.show(fragmentManager!!, "")
    }


    companion object {
        var selectedFilter: ParamFilter = ParamFilter()
        var instance: UmrahSearchFilterFragment = UmrahSearchFilterFragment()
    }

    fun getInstance(): UmrahSearchFilterFragment {
        return instance
    }
}