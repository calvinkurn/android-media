package com.tokopedia.hotel.search.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.list.decoration.SpaceItemDecoration
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.SpanningLinearLayoutManager
import com.tokopedia.hotel.search.data.model.Filter
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchFilterAdapter
import com.tokopedia.hotel.search.presentation.modelholder.FilterStar
import kotlinx.android.synthetic.main.fragment_hotel_search_filter.*

class HotelSearchFilterFragment: BaseDaggerFragment() {
    var filter: Filter = Filter()
    var selectedFilter: ParamFilter = ParamFilter()
    lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher
    lateinit var manager: SaveInstanceCacheManager
    private val starAdapter: HotelSearchFilterAdapter<HotelSearchFilterAdapter.HotelFilterItem> by lazy {
        HotelSearchFilterAdapter<HotelSearchFilterAdapter.HotelFilterItem>(HotelSearchFilterAdapter.MODE_MULTIPLE)
    }
    private val propertyTypeAdapter: HotelSearchFilterAdapter<HotelSearchFilterAdapter.HotelFilterItem> by lazy {
        HotelSearchFilterAdapter<HotelSearchFilterAdapter.HotelFilterItem>(HotelSearchFilterAdapter.MODE_MULTIPLE)
    }
    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotel_search_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val objectId = arguments?.getString(CommonParam.ARG_CACHE_FILTER_ID)
        context?.run {
            manager = if (savedInstanceState == null) SaveInstanceCacheManager(this, objectId)
                else SaveInstanceCacheManager(this, savedInstanceState)
            filter = manager.get(CommonParam.ARG_FILTER, Filter::class.java) ?: Filter()
            selectedFilter = manager.get(CommonParam.ARG_SELECTED_FILTER, ParamFilter::class.java) ?: ParamFilter()
        }
        setupStarFilter(filter.filterStar)
        setupPriceFilter(filter.price)
        setupAccomodationType(filter.accomodation)
        setupRating(filter.filterReview)
        setupPayAtHotel()
        save_filter.button.setOnClickListener {
            selectedFilter.star = starAdapter.selectedItems.mapNotNull { it.toIntOrNull() }
            selectedFilter.propertyType = propertyTypeAdapter.selectedItems.mapNotNull { it.toIntOrNull() }
            selectedFilter.paymentType = if (switch_pay_at_hotel.isChecked) PAYMENT_TYPE_PAY_AT_HOTEL else 0
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    val cacheManager = SaveInstanceCacheManager(this@run, true).also {
                        it.put(CommonParam.ARG_SELECTED_FILTER, selectedFilter)
                    }
                    putExtra(CommonParam.ARG_CACHE_FILTER_ID, cacheManager.id)
                })
                finish()
            }
        }
        activity?.findViewById<View>(R.id.text_view_menu)?.setOnClickListener {
            selectedFilter = ParamFilter()
            starAdapter.clearSelection()
            propertyTypeAdapter.clearSelection()
            switch_pay_at_hotel.isChecked = false
            price_range_input_view.setData(filter.price.minPrice, filter.price.maxPrice,
                    filter.price.minPrice, filter.price.maxPrice)
            rating_seekbar.progress = rating_seekbar.max
        }
    }

    private fun setupPayAtHotel() {
        switch_pay_at_hotel.isChecked = selectedFilter.paymentType == PAYMENT_TYPE_PAY_AT_HOTEL
    }

    private fun setupAccomodationType(accomodation: List<Filter.FilterAccomodation>) {
        filter_accomodation_type.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filter_accomodation_type.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
                LinearLayoutManager.HORIZONTAL))
        filter_accomodation_type.adapter = propertyTypeAdapter
        propertyTypeAdapter.updateItems(accomodation, selectedFilter.propertyType.map { it.toString() }.toSet())
    }

    private fun setupPriceFilter(price: Filter.FilterPrice) {
        val minPriceEditText = price_range_input_view.minValueEditText
        if (::minCurrencyTextWatcher.isInitialized){
            minPriceEditText.removeTextChangedListener(minCurrencyTextWatcher)
        }
        minCurrencyTextWatcher = CurrencyTextWatcher(minPriceEditText, CurrencyEnum.RP)
        minPriceEditText.addTextChangedListener(minCurrencyTextWatcher)

        val maxPriceEditText = price_range_input_view.maxValueEditText
        if (::maxCurrencyTextWatcher.isInitialized){
            maxPriceEditText.removeTextChangedListener(maxCurrencyTextWatcher)
        }
        maxCurrencyTextWatcher = CurrencyTextWatcher(maxPriceEditText, CurrencyEnum.RP)
        maxPriceEditText.addTextChangedListener(maxCurrencyTextWatcher)
        if (selectedFilter.maxPrice == 0 || selectedFilter.maxPrice == price.maxPrice) maxCurrencyTextWatcher.format = getString(R.string.hotel_search_filter_max_string_format_with_plus)
        price_range_input_view.setPower(1.0)

        val filteredMinPrice = if (selectedFilter.minPrice < price.minPrice) price.minPrice else selectedFilter.minPrice
        val filteredMaxPrice = if (selectedFilter.maxPrice == 0 || selectedFilter.maxPrice > price.maxPrice) price.maxPrice else selectedFilter.maxPrice

        price_range_input_view.setData(price.minPrice, price.maxPrice, filteredMinPrice, filteredMaxPrice)
        price_range_input_view.setOnValueChangedListener { minValue, maxValue, minBound, maxBound ->
            selectedFilter.minPrice = minValue
            selectedFilter.maxPrice = maxValue
            if (selectedFilter.maxPrice == maxBound) maxCurrencyTextWatcher.format = getString(R.string.hotel_search_filter_max_string_format_with_plus)
            else maxCurrencyTextWatcher.format = getString(R.string.hotel_search_filter_max_string_format)
        }
    }

    private fun setupRating(filterReview: Filter.FilterReview) {
        val ratingStep = (filterReview.minReview.toInt()..filterReview.maxReview.toInt()).toList()
        selectedFilter.reviewScore = if (ratingStep.first() != 0 && selectedFilter.reviewScore == 0) ratingStep.last() else selectedFilter.reviewScore
        rating_seekbar.max = ratingStep.size - 1
        rating_seekbar.progress = filterReview.maxReview.toInt() - selectedFilter.reviewScore
        ratingStep.forEachIndexed { index, item ->
            val stepView = LayoutInflater.from(context).inflate(R.layout.item_hotel_filter_rating_step, null)
            stepView.findViewById<TextViewCompat>(R.id.title_step).text = String.format("%.1f", item.toFloat())
            base_rating_step.addView(stepView)
            if (index < ratingStep.size - 1){
                val separator = View(context)
                val lp = LinearLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.dp_0),
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                separator.layoutParams = lp
                base_rating_step.addView(separator)
            }
        }
        rating_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                selectedFilter.reviewScore = ratingStep[ratingStep.size - p1 - 1]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun setupStarFilter(filterStar: Filter.FilterStar) {
        val filterStars = filterStar.stars.map { FilterStar(it.toString(), it.toString()) }
        filter_star.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false, resources.getDimensionPixelSize(R.dimen.dp_8))
        filter_star.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
                LinearLayoutManager.HORIZONTAL))
        filter_star.adapter = starAdapter
        starAdapter.updateItems(filterStars, selectedFilter.star.map { it.toString() }.toSet())
    }

    companion object {
        const val TAG = "Filter"
        const val PAYMENT_TYPE_PAY_AT_HOTEL = 1

        fun createInstance(filterCacheId: String) = HotelSearchFilterFragment().also {
            it.arguments = Bundle().apply {
                putString(CommonParam.ARG_CACHE_FILTER_ID, filterCacheId)
            }
        }
    }
}