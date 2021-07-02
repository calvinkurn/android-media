package com.tokopedia.hotel.search.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.SpanningLinearLayoutManager
import com.tokopedia.hotel.search.data.model.Filter
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SpaceItemDecoration
import com.tokopedia.hotel.search.presentation.widget.HotelFilterPriceRangeSlider
import com.tokopedia.kotlin.extensions.view.setMargin
import kotlinx.android.synthetic.main.fragment_hotel_search_filter.*
import kotlin.math.max

class HotelSearchFilterFragment: BaseDaggerFragment() {
    var filter: Filter = Filter()
    var selectedFilter: ParamFilter = ParamFilter()

    lateinit var manager: SaveInstanceCacheManager
    private val starAdapter: HotelSearchResultFilterAdapter by lazy {
        HotelSearchResultFilterAdapter(HotelSearchResultFilterAdapter.MODE_MULTIPLE)
    }
    private val propertyTypeAdapter: HotelSearchResultFilterAdapter by lazy {
        HotelSearchResultFilterAdapter(HotelSearchResultFilterAdapter.MODE_MULTIPLE)
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
        save_filter.setOnClickListener {
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
            price_range_input_view.initView(filter.price.minPrice, filter.price.maxPrice, filter.price.maxPrice)
            rating_seekbar.progress = rating_seekbar.max
        }
    }

    private fun setupPayAtHotel() {
        switch_pay_at_hotel.isChecked = selectedFilter.paymentType == PAYMENT_TYPE_PAY_AT_HOTEL

        // need to hide pay at hotel filter for now
        hidePayAtHotelFilter()
    }

    private fun setupAccomodationType(accomodation: List<Filter.FilterAccomodation>) {
        val filterAccomodation = accomodation.map {
            HotelSearchResultFilterAdapter.HotelFilterItem(it.id.toString(), it.displayName, false) }
        filter_accomodation_type.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        filter_accomodation_type.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                LinearLayoutManager.HORIZONTAL))
        filter_accomodation_type.adapter = propertyTypeAdapter
        propertyTypeAdapter.updateItems(filterAccomodation, selectedFilter.propertyType.map { it.toString() }.toSet())
    }

    private fun setupPriceFilter(price: Filter.FilterPrice) {
        price_range_input_view.initView(selectedFilter.minPrice, selectedFilter.maxPrice, price.maxPrice)
        price_range_input_view.onValueChangedListener = object: HotelFilterPriceRangeSlider.OnValueChangedListener{
            override fun onValueChanged(startValue: Int, endValue: Int) {
                onFilterPriceValueChangedListener(startValue, endValue)
            }
        }
    }

    private fun onFilterPriceValueChangedListener(minValue: Int, maxValue: Int) {
        if (minValue >= 0) selectedFilter.minPrice = minValue
        if (maxValue >= 0) selectedFilter.maxPrice = maxValue
    }

    private fun setupRating(filterReview: Filter.FilterReview) {
        val ratingStep = (filterReview.minReview.toInt()..filterReview.maxReview.toInt()).toMutableList()
        ratingStep.add(0, 0)

        selectedFilter.reviewScore = if (ratingStep.first() != 0 && selectedFilter.reviewScore == 0) ratingStep.first() else selectedFilter.reviewScore
        rating_seekbar.max = ratingStep.size - 1

        if (selectedFilter.reviewScore == 0) rating_seekbar.progress = rating_seekbar.max
        else rating_seekbar.progress = filterReview.maxReview.toInt() - max(selectedFilter.reviewScore, 5)

        ratingStep.forEachIndexed { index, item ->
            val stepView = LayoutInflater.from(context).inflate(R.layout.item_hotel_filter_rating_step, null)

            when {
                item > 0 -> stepView.findViewById<TextView>(R.id.title_step).text = String.format("  %.1f   ", item.toFloat())
                else -> stepView.findViewById<TextView>(R.id.title_step).text = getString(R.string.hotel_search_filter_rating_all)
            }

            base_rating_step.addView(stepView)
            if (index < ratingStep.size - 1){
                val separator = View(context)
                val lp = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                separator.layoutParams = lp
                base_rating_step.addView(separator)
            }
        }

        rating_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 == ratingStep.size - 1) selectedFilter.reviewScore = 0
                else selectedFilter.reviewScore = ratingStep[ratingStep.size - p1 - 1]
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun setupStarFilter(filterStar: Filter.FilterStar) {
        val filterStars
                = filterStar.stars.map { HotelSearchResultFilterAdapter.HotelFilterItem(it.toString(), it.toString(), true) }
        filter_star.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false, resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
        filter_star.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                LinearLayoutManager.HORIZONTAL))
        filter_star.adapter = starAdapter
        starAdapter.updateItems(filterStars, selectedFilter.star.map { it.toString() }.toSet())
    }

    private fun hidePayAtHotelFilter() {
        layout_filter_pay_at_hotel.visibility = View.GONE
        divider_layout_pay_at_hotel.visibility = View.GONE
        hotel_filter_rating_title.setMargin(0,0,0,0)
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