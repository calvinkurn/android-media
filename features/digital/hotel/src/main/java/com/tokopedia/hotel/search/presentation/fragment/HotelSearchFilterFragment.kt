package com.tokopedia.hotel.search.presentation.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.list.decoration.SpaceItemDecoration
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Filter
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchFilterAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchFilterRatingStepAdapter
import com.tokopedia.hotel.search.presentation.modelholder.FilterStar
import kotlinx.android.synthetic.main.fragment_hotel_search_filter.*

class HotelSearchFilterFragment: BaseDaggerFragment() {
    var filter: Filter = Filter()
    lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher
    lateinit var manager: SaveInstanceCacheManager
    private val starAdapter by lazy {
        HotelSearchFilterAdapter<FilterStar>(HotelSearchFilterAdapter.MODE_MULTIPLE)
    }
    private val propertyTypeAdapter by lazy {
        HotelSearchFilterAdapter<Filter.FilterAccomodation>(HotelSearchFilterAdapter.MODE_MULTIPLE)
    }
    private val preferenceAdapter by lazy {
        HotelSearchFilterAdapter<Filter.FilterPreference>(HotelSearchFilterAdapter.MODE_MULTIPLE)
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
        }
        setupStarFilter()
        setupPriceFilter(filter.price)
        setupAccomodationType(filter.accomodation)
        setupPreference(filter.preferences)
        setupRating()
    }

    private fun setupPreference(preferences: List<Filter.FilterPreference>) {
        filter_preference.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filter_preference.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
                LinearLayoutManager.HORIZONTAL))
        filter_preference.adapter = preferenceAdapter
        preferenceAdapter.updateItems(preferences)
    }

    private fun setupAccomodationType(accomodation: List<Filter.FilterAccomodation>) {
        filter_accomodation_type.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filter_accomodation_type.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
                LinearLayoutManager.HORIZONTAL))
        filter_accomodation_type.adapter = propertyTypeAdapter
        propertyTypeAdapter.updateItems(accomodation)
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
        price_range_input_view.setPower(1.0)
        price_range_input_view.setData(price.minPrice.toInt(), price.maxPrice.toInt(), price.minPrice.toInt(), price.maxPrice.toInt())
        price_range_input_view.setOnValueChangedListener { minValue, maxValue, minBound, maxBound ->  }
    }

    private fun setupRating() {
        val ratingStep = listOf(5,6,7,8,9)
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
    }

    private fun setupStarFilter() {
        val filterStars = (1..5).toList().map { FilterStar(it.toString(), it.toString()) }
        filter_star.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filter_star.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
                LinearLayoutManager.HORIZONTAL))
        filter_star.adapter = starAdapter
        starAdapter.updateItems(filterStars)
    }

    companion object {
        const val TAG = "Filter"

        fun createInstance(filterCacheId: String) = HotelSearchFilterFragment().also {
            it.arguments = Bundle().apply {
                putString(CommonParam.ARG_CACHE_FILTER_ID, filterCacheId)
            }
        }
    }
}