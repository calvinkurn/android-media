package com.tokopedia.hotel.search_map.presentation.fragment

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
import com.tokopedia.hotel.databinding.FragmentHotelSearchFilterBinding
import com.tokopedia.hotel.search_map.data.model.Filter
import com.tokopedia.hotel.search_map.data.model.params.ParamFilter
import com.tokopedia.hotel.search_map.data.util.CommonParam
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search_map.presentation.adapter.viewholder.SpaceItemDecoration
import com.tokopedia.hotel.search_map.presentation.widget.HotelFilterPriceRangeSlider
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlin.math.max

class HotelSearchFilterFragment: BaseDaggerFragment() {
    var filter: Filter = Filter()
    var selectedFilter: ParamFilter = ParamFilter()

    private var binding by autoClearedNullable<FragmentHotelSearchFilterBinding>()

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
        binding = FragmentHotelSearchFilterBinding.inflate(inflater, container, false)
        return binding?.root
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
        binding?.saveFilter?.setOnClickListener {
            selectedFilter.star = starAdapter.selectedItems.mapNotNull { it.toIntOrNull() }
            selectedFilter.propertyType = propertyTypeAdapter.selectedItems.mapNotNull { it.toIntOrNull() }
            selectedFilter.paymentType = if (binding?.switchPayAtHotel?.isChecked == true) PAYMENT_TYPE_PAY_AT_HOTEL else 0
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
            binding?.switchPayAtHotel?.isChecked = false
            binding?.priceRangeInputView?.initView(filter.price.minPrice, filter.price.maxPrice, filter.price.maxPrice)
            binding?.ratingSeekbar?.progress = binding?.ratingSeekbar?.max ?: 0
        }
    }

    private fun setupPayAtHotel() {
        binding?.switchPayAtHotel?.isChecked = selectedFilter.paymentType == PAYMENT_TYPE_PAY_AT_HOTEL

        // need to hide pay at hotel filter for now
        hidePayAtHotelFilter()
    }

    private fun setupAccomodationType(accomodation: List<Filter.FilterAccomodation>) {
        val filterAccomodation = accomodation.map {
            HotelSearchResultFilterAdapter.HotelFilterItem(it.id.toString(), it.displayName, false) }
        binding?.filterAccomodationType?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.filterAccomodationType?.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                LinearLayoutManager.HORIZONTAL))
        binding?.filterAccomodationType?.adapter = propertyTypeAdapter
        propertyTypeAdapter.updateItems(filterAccomodation, selectedFilter.propertyType.map { it.toString() }.toSet())
    }

    private fun setupPriceFilter(price: Filter.FilterPrice) {
        binding?.priceRangeInputView?.initView(selectedFilter.minPrice, selectedFilter.maxPrice, price.maxPrice)
        binding?.priceRangeInputView?.onValueChangedListener = object: HotelFilterPriceRangeSlider.OnValueChangedListener{
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
        binding?.ratingSeekbar?.max = ratingStep.size - 1

        if (selectedFilter.reviewScore == 0) binding?.ratingSeekbar?.progress = binding?.ratingSeekbar?.max ?: 0
        else binding?.ratingSeekbar?.progress = filterReview.maxReview.toInt() - max(selectedFilter.reviewScore, 5)

        ratingStep.forEachIndexed { index, item ->
            val stepView = LayoutInflater.from(context).inflate(R.layout.item_hotel_filter_rating_step, null)

            when {
                item > 0 -> stepView.findViewById<TextView>(R.id.title_step).text = String.format("  %.1f   ", item.toFloat())
                else -> stepView.findViewById<TextView>(R.id.title_step).text = getString(R.string.hotel_search_filter_rating_all)
            }

            binding?.baseRatingStep?.addView(stepView)
            if (index < ratingStep.size - 1){
                val separator = View(context)
                val lp = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                separator.layoutParams = lp
                binding?.baseRatingStep?.addView(separator)
            }
        }

        binding?.ratingSeekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
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
        binding?.filterStar?.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false, resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
        binding?.filterStar?.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                LinearLayoutManager.HORIZONTAL))
        binding?.filterStar?.adapter = starAdapter
        starAdapter.updateItems(filterStars, selectedFilter.star.map { it.toString() }.toSet())
    }

    private fun hidePayAtHotelFilter() {
        binding?.let {
            it.layoutFilterPayAtHotel.visibility = View.GONE
            it.dividerLayoutPayAtHotel.visibility = View.GONE
            it.hotelFilterRatingTitle.setMargin(0,0,0,0)
        }
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