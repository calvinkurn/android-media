package com.tokopedia.filter.bottomsheet

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class InitializationTest : SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `Initialization Test with nulls (degenerate cases)`() {
        sortFilterBottomSheetViewModel.init(mapOf(), null)

        `showKnob should be`(false)
    }

    private fun `showKnob should be`(expectedShowKnob: Boolean) {
        assert(sortFilterBottomSheetViewModel.showKnob == expectedShowKnob) {
            "Show knob should be $expectedShowKnob"
        }
    }

    @Test
    fun `Initialization Test with more than one filter and has sort`() {
        sortFilterBottomSheetViewModel.init(mapOf(), "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>())

        `showKnob should be`(true)
    }

    @Test
    fun `Initialization Test with one filter and has sort`() {
        sortFilterBottomSheetViewModel.init(mapOf(), "dynamic-filter-model-one-filter-and-sort.json".jsonToObject<DynamicFilterModel>())

        `showKnob should be`(false)
    }

    @Test
    fun `Initialization Test with four filter and no sort`() {
        sortFilterBottomSheetViewModel.init(mapOf(), "dynamic-filter-model-four-filter-and-no-sort.json".jsonToObject<DynamicFilterModel>())

        `showKnob should be`(true)
    }

    @Test
    fun `Initialization Test with no filter and has sort`() {
        sortFilterBottomSheetViewModel.init(mapOf(), "dynamic-filter-model-no-filter-and-has-sort.json".jsonToObject<DynamicFilterModel>())

        `showKnob should be`(false)
    }
}