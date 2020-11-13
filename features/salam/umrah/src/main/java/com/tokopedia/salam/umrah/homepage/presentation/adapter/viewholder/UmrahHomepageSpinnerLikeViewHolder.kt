package com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.util.UmrahWidthSetting
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBottomSheetData
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageBottomSheetMapper
import com.tokopedia.salam.umrah.homepage.data.UmrohHomepageBottomSheetwithType
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageBottomSheetAdapter
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_umrah_home_page_main.view.*
import kotlinx.android.synthetic.main.partial_umrah_home_page_search_spinner.view.*

/**
 * @author by firman on 23/10/19
 */
class UmrahHomepageSpinnerLikeViewHolder(view: View, private val onBindListener: onItemBindListener)
    : AbstractViewHolder<UmrahSearchParameterEntity>(view), UmrahHomepageBottomSheetAdapter.UmrahBottomSheetListener {

    private var adapterCity   = UmrahHomepageBottomSheetAdapter(view.context, this)
    private var adapterPrice  = UmrahHomepageBottomSheetAdapter(view.context, this)
    private var adapterPeriod = UmrahHomepageBottomSheetAdapter(view.context, this)

    private var departureCityId = ""
    private var departurePeriod = ""
    private var priceMin = 0
    private var priceMax = 0

    private var defaultIndexCities  = 0
    private var defaultIndexPeriods = 0
    private var defaultIndexPrice   = 0

    private val tvLocation: Typography = view.findViewById(R.id.tv_umrah_home_page_search_location_spinner_like)
    private val tvPeriod: Typography   = view.findViewById(R.id.tv_umrah_home_page_search_depart_spinner_like)
    private val tvPrice: Typography    = view.findViewById(R.id.tv_umrah_home_page_search_price_spinner_like)


    override fun bind(element: UmrahSearchParameterEntity) {
        if (element.isLoaded) {
            if(!UmrahHomepageFragment.isRequestedSpinnerLike) {
                with(itemView) {
                    shimmering.hide()
                    section_layout.show()

                    if(UmrahWidthSetting.tabSize(context)){
                        iv_umrah_bg_kabbah.adjustViewBounds = false
                    }

                    defaultIndexCities = element.umrahSearchParameter.depatureCities.defaultOption
                    defaultIndexPeriods = element.umrahSearchParameter.departurePeriods.defaultOption
                    defaultIndexPrice = element.umrahSearchParameter.priceRangeOptions.defaultOption

                    renderDefaultData(element)

                    rl_umrah_home_page_location_parent.setOnClickListener {
                        val listBottomSheet = UmrahHomepageBottomSheetMapper.citiesMapper(element)
                        showBottomSheet(
                                getString(R.string.umrah_home_page_search_depart_city_label), listBottomSheet, defaultIndexCities, adapterCity)
                    }

                    rl_umrah_home_page_calendar_parent.setOnClickListener {
                        val listBottomSheet = UmrahHomepageBottomSheetMapper.periodsMapper(element)
                        showBottomSheet(
                                getString(R.string.umrah_home_page_search_depart_month_label), listBottomSheet, defaultIndexPeriods, adapterPeriod)
                    }

                    rl_umrah_home_page_price_parent.setOnClickListener {
                        val listBottomSheet = UmrahHomepageBottomSheetMapper.priceRangerMapper(element)
                        showBottomSheet(
                                getString(R.string.umrah_home_page_search_price_label), listBottomSheet, defaultIndexPrice, adapterPrice)
                    }

                    btn_umrah_home_page_search.setOnClickListener {
                        onBindListener.onSearchProduct(tvPeriod.text.toString(),
                                tvLocation.text.toString(),
                                tvPrice.text.toString())

                        val umrahSearchDataParam = UmrahSearchProductDataParam(
                                departureCityId = departureCityId,
                                departurePeriod = departurePeriod,
                                priceMin = priceMin,
                                priceMax = priceMax
                        )
                        context?.let {
                            context.startActivity(
                                    UmrahSearchActivity.createIntent(it,
                                            umrahSearchDataParam.departureCityId,
                                            umrahSearchDataParam.departurePeriod,
                                            umrahSearchDataParam.priceMin,
                                            umrahSearchDataParam.priceMax,
                                            element.umrahSearchParameter.durationDaysRangeLimit.minimum,
                                            element.umrahSearchParameter.durationDaysRangeLimit.maximum,
                                            element.umrahSearchParameter.sortMethods.options[element
                                                    .umrahSearchParameter.sortMethods.defaultOption].query))
                        }
                    }
                }
                UmrahHomepageFragment.isRequestedSpinnerLike = true
            }
        }else{
            itemView.section_layout.hide()
            itemView.shimmering.show()
            onBindListener.onBindParameterVH(element.isLoadFromCloud)
        }

    }

    private fun renderDefaultData(element: UmrahSearchParameterEntity){
        if(element.umrahSearchParameter.depatureCities.options.isNotEmpty()) {
            tvLocation.text = element.umrahSearchParameter.depatureCities.options[defaultIndexCities].displayText
            departureCityId = element.umrahSearchParameter.depatureCities.options[defaultIndexCities].query
        }

        if(element.umrahSearchParameter.departurePeriods.options.isNotEmpty()) {
            tvPeriod.text = element.umrahSearchParameter.departurePeriods.options[defaultIndexPeriods].displayText
            departurePeriod = element.umrahSearchParameter.departurePeriods.options[defaultIndexPeriods].query
        }

        if(element.umrahSearchParameter.priceRangeOptions.options.isNotEmpty()) {
            tvPrice.text = element.umrahSearchParameter.priceRangeOptions.options[defaultIndexPrice].rangeDisplayText
            priceMin = element.umrahSearchParameter.priceRangeOptions.options[defaultIndexPrice].minimum
            priceMax = element.umrahSearchParameter.priceRangeOptions.options[defaultIndexPrice].maximum
        }
    }

    override fun getDatafromBottomSheet(data: UmrohHomepageBottomSheetwithType) {
        when (data.type) {
            DEPARTURE_CITIES -> {
                tvLocation.text = data.displayText
                departureCityId = data.query
                defaultIndexCities = data.index
            }
            DEPARTURE_PERIODS -> {
                tvPeriod.text = data.displayText
                departurePeriod = data.query
                defaultIndexPeriods = data.index
            }
            PRICE_RANGE -> {
                tvPrice.text = data.displayText
                priceMin = data.priceMin
                priceMax = data.priceMax
                defaultIndexPrice = data.index
            }
        }

    }

    private fun showBottomSheet(title: String, listBottomSheet: UmrahHomepageBottomSheetData,
                                defaultOption: Int,
                                adapter: UmrahHomepageBottomSheetAdapter) {

        onBindListener.showBottomSheetSearchParam(title,listBottomSheet,defaultOption,adapter)
    }


    companion object {
        const val DEPARTURE_CITIES = "departureCities"
        const val DEPARTURE_PERIODS = "departurePeriods"
        const val PRICE_RANGE = "priceRangeOptions"
        val LAYOUT = R.layout.partial_umrah_home_page_main

    }
}