package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.testutils.jsonToObject
import com.tokopedia.track.TrackAppUtils
import org.junit.Test

internal class PriceFilterTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `OnPriceRangeOptionClick with given PriceRangeOptionViewModel to apply price range filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val clickedPriceRangeOption = priceFilterViewModel.getUnselectedPriceRangeOption()
        `When Price Range Option Clicked and applied`(priceFilterViewModel, clickedPriceRangeOption)

        val minPriceFilterValue = clickedPriceRangeOption.option.valMin
        val maxPriceFilterValue = clickedPriceRangeOption.option.valMax
        val expectedMapParameter = mapOf(
                SearchApiConst.PMIN to minPriceFilterValue,
                SearchApiConst.PMAX to maxPriceFilterValue,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert only one price range option is selected`(clickedPriceRangeOption)
        `Then assert min and max value in filter view is updated`(
                priceFilterViewModel, minPriceFilterValue, maxPriceFilterValue
        )
        `Then assert sort filter view is updated`(3)
        `Then assert filter view is expanded`()
        `Then assert tracking price range click`(createTrackingClickPriceRangeDataLayer(clickedPriceRangeOption, true))
    }

    private fun `When Price Range Option Clicked and applied`(
            priceFilterViewModel: PriceFilterViewModel, priceOptionViewModel: PriceOptionViewModel
    ) {
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, priceOptionViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert only one price range option is selected`(selectedPriceOptionViewModel: PriceOptionViewModel) {
        sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!.priceRangeOptionViewModelList.forEach {
            val expectedIsSelected = it == selectedPriceOptionViewModel
            assert(it.isSelected == expectedIsSelected) {
                "Price Range Option ${it.option.name} is selected should be $expectedIsSelected"
            }
        }
    }

    private fun `Then assert min and max value in filter view is updated`(
            priceFilterViewModel: PriceFilterViewModel,
            minValue: String,
            maxValue: String
    ) {
        assert(priceFilterViewModel.minPriceFilterValue == minValue) {
            "Min Price Filter Value is ${priceFilterViewModel.minPriceFilterValue}. Expected is $minValue."
        }

        assert(priceFilterViewModel.maxPriceFilterValue == maxValue) {
            "Max Price Filter Value is ${priceFilterViewModel.maxPriceFilterValue}. Expected is $maxValue."
        }
    }

    private fun createTrackingClickPriceRangeDataLayer(priceOptionViewModel: PriceOptionViewModel, isSelected: Boolean): Map<String, Any> {
        return mapOf(
                TrackAppUtils.EVENT to FilterEventTracking.Event.CLICK_FILTER,
                TrackAppUtils.EVENT_CATEGORY to FilterEventTracking.Category.FILTER_JOURNEY,
                TrackAppUtils.EVENT_ACTION to getTrackingClickPriceRangeEventAction(priceOptionViewModel),
                TrackAppUtils.EVENT_LABEL to isSelected.toString()
        )
    }

    private fun getTrackingClickPriceRangeEventAction(priceOptionViewModel: PriceOptionViewModel): String {
        return String.format(
                FilterEventTracking.Action.CLICK_FILTER_PRICE_RANGE,
                priceOptionViewModel.option.valMin.toInt(), priceOptionViewModel.option.valMax.toInt(), priceOptionViewModel.position
        )
    }

    private fun `Then assert tracking price range click`(expectedClickPriceRangeDataLayer: Map<String, Any>) {
        val clickPriceRangeDataLayer = sortFilterBottomSheetViewModel.trackPriceRangeClickEventLiveData.value?.getContentIfNotHandled() ?: mapOf()

        assert(clickPriceRangeDataLayer == expectedClickPriceRangeDataLayer) {
            "Actual click price range data layer: $clickPriceRangeDataLayer, expected: $expectedClickPriceRangeDataLayer."
        }
    }

    @Test
    fun `OnPriceRangeOptionClick with given PriceRangeOptionViewModel to un-apply price range filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val clickedPriceRangeOption = priceFilterViewModel.getUnselectedPriceRangeOption()
        // Just click twice to remove filter
        `When Price Range Option Clicked twice`(priceFilterViewModel, clickedPriceRangeOption)

        val expectedMapParameter = mapOf(
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(false)
        `Then assert button apply is not shown`()
        `Then assert no price range option is selected`()
        `Then assert min and max value in filter view is updated`(priceFilterViewModel, "", "")
        `Then assert sort filter view is updated`(3)
        `Then assert filter view is expanded`()
        `Then assert tracking price range click`(createTrackingClickPriceRangeDataLayer(clickedPriceRangeOption, false))
    }

    private fun `When Price Range Option Clicked twice`(priceFilterViewModel: PriceFilterViewModel, priceOptionViewModel: PriceOptionViewModel) {
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, priceOptionViewModel)
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, priceOptionViewModel)
    }

    private fun `Then assert no price range option is selected`() {
        sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!.priceRangeOptionViewModelList.forEach {
            assert(!it.isSelected) {
                "Price Range Option ${it.option.name} should not be selected"
            }
        }
    }

    @Test
    fun `OnPriceRangeOptionClick with given PriceRangeOptionViewModel to replace existing price filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val mapParameter = createMapParameterWithPriceFilters(dynamicFilterModel)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val clickedPriceRangeOption = priceFilterViewModel.getUnselectedPriceRangeOption()
        `When Price Range Option Clicked and applied`(priceFilterViewModel, clickedPriceRangeOption)

        val minPriceFilterValue = clickedPriceRangeOption.option.valMin
        val maxPriceFilterValue = clickedPriceRangeOption.option.valMax
        val expectedMapParameter = mapOf(
                SearchApiConst.PMIN to minPriceFilterValue,
                SearchApiConst.PMAX to maxPriceFilterValue,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert only one price range option is selected`(clickedPriceRangeOption)
        `Then assert min and max value in filter view is updated`(
                priceFilterViewModel, minPriceFilterValue, maxPriceFilterValue
        )
        `Then assert sort filter view is updated`(3)
        `Then assert filter view is expanded`()
        `Then assert tracking price range click`(createTrackingClickPriceRangeDataLayer(clickedPriceRangeOption, true))
    }

    private fun createMapParameterWithPriceFilters(dynamicFilterModel: DynamicFilterModel): Map<String, String> {
        val priceFilter = dynamicFilterModel.data.filter[2] // Price Filter
        val selectedPriceRange = priceFilter.options.find { it.key == Option.KEY_PRICE_RANGE_1 }!!
        return mutableMapOf<String, String>().also {
            it[SearchApiConst.PMIN] = selectedPriceRange.valMin
            it[SearchApiConst.PMAX] = selectedPriceRange.valMax
        }
    }

    @Test
    fun `OnMinPriceEdited to apply min price filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val minPriceFilter = 1000
        `When min price filter edited and applied`(priceFilterViewModel, minPriceFilter)

        val expectedMapParameter = mapOf(
                SearchApiConst.PMIN to minPriceFilter.toString(),
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert min and max value in filter view is updated`(priceFilterViewModel, minPriceFilter.toString(), "")
    }

    private fun `When min price filter edited and applied`(priceFilterViewModel: PriceFilterViewModel, minValue: Int) {
        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, minValue)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    @Test
    fun `OnMinPriceEdited will remove filter in map parameter if input value is below min price`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(SearchApiConst.PMIN to 100_000.toString()), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val minPriceFilter = 99
        `When min price filter edited and applied`(priceFilterViewModel, minPriceFilter)

        `Then assert map parameter is as expected`(mapOf(SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE))
        `Then assert button reset visibility`(false)
        `Then assert button apply is shown with loading`()
        `Then assert min and max value in filter view is updated`(priceFilterViewModel, minPriceFilter.toString(), "")
    }

    @Test
    fun `OnMinPriceEdited can show price range option as selected`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val priceRangeFilter = dynamicFilterModel.data.filter[2].options.find { it.key == Option.KEY_PRICE_RANGE_1 }!!
        // Pretend user already filter by max price
        val mapParameter = mapOf(SearchApiConst.PMAX to priceRangeFilter.valMax)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        // Pretend user manually filter min price within price range
        `When min price filter edited and applied`(priceFilterViewModel, priceRangeFilter.valMin.toInt())

        val expectedMapParameter = mapOf(
                SearchApiConst.PMIN to priceRangeFilter.valMin,
                SearchApiConst.PMAX to priceRangeFilter.valMax,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        val expectedSelectedPriceRangeOption =
                priceFilterViewModel.priceRangeOptionViewModelList.find { it.option.key == Option.KEY_PRICE_RANGE_1 }!!

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert only one price range option is selected`(expectedSelectedPriceRangeOption)
        `Then assert min and max value in filter view is updated`(
                priceFilterViewModel, priceRangeFilter.valMin, priceRangeFilter.valMax
        )
    }

    @Test
    fun `OnMaxPriceEdited to apply max price filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val maxPriceFilter = 5_000_000
        `When max price filter edited and applied`(priceFilterViewModel, maxPriceFilter)

        val expectedMapParameter = mapOf(
                SearchApiConst.PMAX to maxPriceFilter.toString(),
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert min and max value in filter view is updated`(priceFilterViewModel, "", maxPriceFilter.toString())
    }

    private fun `When max price filter edited and applied`(priceFilterViewModel: PriceFilterViewModel, maxValue: Int) {
        sortFilterBottomSheetViewModel.onMaxPriceFilterEdited(priceFilterViewModel, maxValue)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    @Test
    fun `OnMaxPriceEdited  will remove filter in map parameter if input value is above max price`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(SearchApiConst.PMAX to 3_000_000.toString()), dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val maxPriceFilter = 50_000_000
        `When max price filter edited and applied`(priceFilterViewModel, maxPriceFilter)

        `Then assert map parameter is as expected`(mapOf(SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE))
        `Then assert button reset visibility`(false)
        `Then assert button apply is shown with loading`()
        `Then assert min and max value in filter view is updated`(priceFilterViewModel, "", maxPriceFilter.toString())
    }

    @Test
    fun `OnMaxPriceEdited can show price range option as selected`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val priceRangeFilter = dynamicFilterModel.data.filter[2].options.find { it.key == Option.KEY_PRICE_RANGE_1 }!!
        // Pretend user already filter by min price
        val mapParameter = mapOf(SearchApiConst.PMIN to priceRangeFilter.valMin)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        // Pretend user manually filter min price within price range
        `When max price filter edited and applied`(priceFilterViewModel, priceRangeFilter.valMax.toInt())

        val expectedMapParameter = mapOf(
                SearchApiConst.PMIN to priceRangeFilter.valMin,
                SearchApiConst.PMAX to priceRangeFilter.valMax,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )

        val expectedSelectedPriceRangeOption =
                priceFilterViewModel.priceRangeOptionViewModelList.find { it.option.key == Option.KEY_PRICE_RANGE_1 }!!

        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert button reset visibility`(true)
        `Then assert button apply is shown with loading`()
        `Then assert only one price range option is selected`(expectedSelectedPriceRangeOption)
        `Then assert min and max value in filter view is updated`(
                priceFilterViewModel, priceRangeFilter.valMin, priceRangeFilter.valMax
        )
    }
}