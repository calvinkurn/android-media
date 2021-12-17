package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class ApplyKeywordFilterTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `add keyword filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-keyword-filter.json"
            .jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val keywordFilterDataView = sortFilterList!!.filterIsInstance<KeywordFilterDataView>().first()
        `When add keyword`(keywordFilterDataView)

        val expectedMapParameter = existingMapParameter.modifyKeyword(keywordFilterDataView)

        `Then assert button apply is shown with loading`()
        `Then assert map parameter is as expected`(expectedMapParameter)
    }

    private fun `When add keyword`(keywordFilterDataView: KeywordFilterDataView) {
        keywordFilterDataView.addKeyword(
            keyword = "remote",
            onSuccess = {
                sortFilterBottomSheetViewModel.onChangeKeywordFilter(keywordFilterDataView)
            },
            onError = { },
        )
    }

    private fun Map<String, String>.modifyKeyword(keywordFilterDataView: KeywordFilterDataView) =
        toMutableMap().also {
            it[SearchApiConst.Q] = keywordFilterDataView.generateKeyword()
        }

    @Test
    fun `add and then remove keyword filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-keyword-filter.json"
            .jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val keywordFilterDataView = sortFilterList!!.filterIsInstance<KeywordFilterDataView>().first()
        `When add and then remove keyword`(keywordFilterDataView)

        `Then assert button apply is not shown`()
        `Then assert map parameter is as expected`(existingMapParameter)
    }

    private fun `When add and then remove keyword`(keywordFilterDataView: KeywordFilterDataView) {
        val addedNegativeKeyword = "remote"
        keywordFilterDataView.addKeyword(
            keyword = addedNegativeKeyword,
            onSuccess = {
                sortFilterBottomSheetViewModel.onChangeKeywordFilter(keywordFilterDataView)
            },
            onError = { },
        )

        keywordFilterDataView.removeKeyword(addedNegativeKeyword)
        sortFilterBottomSheetViewModel.onChangeKeywordFilter(keywordFilterDataView)
    }
}