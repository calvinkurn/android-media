package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class AutoCompleteHeadlineAdsTest : AutoCompleteTestFixtures() {
    @Test
    fun `on suggestion success with cpm id and suggestion id no match`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        val suggestionModel =
            AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        val shopAdsModel = HeadlineAdsRumahSuccessJSON.jsonToObject<ShopAdsModel>()
        suggestionModel.data.cpmModel = shopAdsModel.cpmModel

        `Given Suggestion Use Case Is Successful`(suggestionModel.data)
        `When screen is initialized`(viewModel)
        `Then assert suggestion ads is cpm first item`(suggestionModel, viewModel, shopAdsModel)
    }

    private fun `When screen is initialized`(viewModel: AutoCompleteViewModel) {
        viewModel.onScreenInitialized()
    }

    private fun `Then assert suggestion ads is cpm first item`(
        suggestionModel: SuggestionUnifyModel,
        viewModel: AutoCompleteViewModel,
        shopAdsModel: ShopAdsModel
    ) {
        val isAdsPosition = suggestionModel.data.data.indexOfFirst { it.isAds }
        val isAdsDomainModel = viewModel.stateValue.resultList[isAdsPosition].domainModel
        assertThat(
            isAdsDomainModel.suggestionId,
            `is`(shopAdsModel.cpmModel.data[0].cpm.cpmShop.id)
        )
        assertThat(
            isAdsDomainModel.title.text,
            `is`(shopAdsModel.cpmModel.data[0].cpm.name)
        )
        assertThat(
            isAdsDomainModel.subtitle.text,
            `is`(shopAdsModel.cpmModel.data[0].cpm.cpmShop.location)
        )
    }

    @Test
    fun `on suggestion success with first cpm id matching suggestion`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        val suggestionModel =
            AutoCompleteSuggestionRumahSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        print(suggestionModel.toString())
        val shopAdsModel = HeadlineAdsRumahSuccessJSON.jsonToObject<ShopAdsModel>()
        suggestionModel.data.cpmModel = shopAdsModel.cpmModel

        `Given Suggestion Use Case Is Successful`(suggestionModel.data)
        `When screen is initialized`(viewModel)

        `Then assert suggestion ads is cpm second item`(suggestionModel, viewModel, shopAdsModel)
    }

    private fun `Then assert suggestion ads is cpm second item`(
        suggestionModel: SuggestionUnifyModel,
        viewModel: AutoCompleteViewModel,
        shopAdsModel: ShopAdsModel
    ) {
        val isAdsPosition = suggestionModel.data.data.indexOfFirst { it.isAds }
        val isAdsDomainModel = viewModel.stateValue.resultList[isAdsPosition].domainModel
        assertThat(
            isAdsDomainModel.suggestionId,
            `is`(shopAdsModel.cpmModel.data[1].cpm.cpmShop.id)
        )
        assertThat(
            isAdsDomainModel.title.text,
            `is`(shopAdsModel.cpmModel.data[1].cpm.name)
        )
        assertThat(
            isAdsDomainModel.subtitle.text,
            `is`(shopAdsModel.cpmModel.data[1].cpm.cpmShop.location)
        )
    }

    @Test
    fun `on suggestion success with both cpm id matching suggestion should not show ads`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        val suggestionModel =
            AutoCompleteSuggestionRumahBothSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        print(suggestionModel.toString())
        val shopAdsModel = HeadlineAdsRumahSuccessJSON.jsonToObject<ShopAdsModel>()
        suggestionModel.data.cpmModel = shopAdsModel.cpmModel

        `Given Suggestion Use Case Is Successful`(suggestionModel.data)
        `When screen is initialized`(viewModel)

        `Then assert that no domain model is ads`(viewModel)
    }

    private fun `Then assert that no domain model is ads`(viewModel: AutoCompleteViewModel) {
        viewModel.stateValue.resultList.forEach {
            assertThat(it.domainModel.isAds, `is`(false))
        }
    }

    @Test
    fun `on ads model impressed should call tracker topads`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        val suggestionModel =
            AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        val shopAdsModel = HeadlineAdsRumahSuccessJSON.jsonToObject<ShopAdsModel>()
        val className = this.javaClass.simpleName
        suggestionModel.data.cpmModel = shopAdsModel.cpmModel

        `Given Suggestion Use Case Is Successful`(suggestionModel.data)
        `When screen is initialized`(viewModel)
        val isAdsPosition = suggestionModel.data.data.indexOfFirst { it.isAds }
        val isAdsDataView = viewModel.stateValue.resultList[isAdsPosition]

        val classNameCaptureSlot = slot<String>()
        val urlCaptureSlot = slot<String>()
        val productIdCaptureSlot = slot<String>()
        val productNameCaptureSlot = slot<String>()
        val imageUrlCaptureSlot = slot<String>()

        `Given Topads Track Impress Is Successful`(
            classNameCaptureSlot,
            urlCaptureSlot,
            productIdCaptureSlot,
            productNameCaptureSlot,
            imageUrlCaptureSlot,
        )

        `When topads model impressed`(viewModel, isAdsDataView, className)
        `Then assert topads track impression called with correct parameters`(
            classNameCaptureSlot,
            className,
            urlCaptureSlot,
            shopAdsModel,
            imageUrlCaptureSlot
        )
    }

    private fun `Then assert topads track impression called with correct parameters`(
        classNameCaptureSlot: CapturingSlot<String>, className: String?,
        urlCaptureSlot: CapturingSlot<String>,
        shopAdsModel: ShopAdsModel,
        imageUrlCaptureSlot: CapturingSlot<String>
    ) {
        assertThat(classNameCaptureSlot.captured, `is`(className))
        assertThat(urlCaptureSlot.captured, `is`(shopAdsModel.cpmModel.data[0].cpm.cpmImage.fullUrl))
        assertThat(imageUrlCaptureSlot.captured, `is`(shopAdsModel.cpmModel.data[0].cpm.cpmImage.fullEcs))
        verify(exactly = 1) {
            topAdsUrlHitter.hitImpressionUrl(
                any<String>(),
                any(),
                any(),
                any(),
                any(),
            )
        }
    }

    private fun `When topads model impressed`(
        viewModel: AutoCompleteViewModel,
        isAdsDataView: AutoCompleteUnifyDataView,
        className: String
    ) {
        viewModel.impressTopAds(isAdsDataView, className)
    }

    @Test
    fun `on ads model clicked should call tracker topads`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        val suggestionModel =
            AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        val shopAdsModel = HeadlineAdsRumahSuccessJSON.jsonToObject<ShopAdsModel>()
        val className = this.javaClass.simpleName
        suggestionModel.data.cpmModel = shopAdsModel.cpmModel

        `Given Suggestion Use Case Is Successful`(suggestionModel.data)
        `When screen is initialized`(viewModel)
        val isAdsPosition = suggestionModel.data.data.indexOfFirst { it.isAds }
        val isAdsDataView = viewModel.stateValue.resultList[isAdsPosition]

        val classNameCaptureSlot = slot<String>()
        val urlCaptureSlot = slot<String>()
        val productIdCaptureSlot = slot<String>()
        val productNameCaptureSlot = slot<String>()
        val imageUrlCaptureSlot = slot<String>()

        `Given Topads Track Click Is Successful`(
            classNameCaptureSlot,
            urlCaptureSlot,
            productIdCaptureSlot,
            productNameCaptureSlot,
            imageUrlCaptureSlot,
        )

        `When autocomplete item model clicked`(viewModel, isAdsDataView, className)
        assertThat(classNameCaptureSlot.captured, `is`(className))
        assertThat(urlCaptureSlot.captured, `is`(shopAdsModel.cpmModel.data[0].adClickUrl))
        assertThat(imageUrlCaptureSlot.captured, `is`(shopAdsModel.cpmModel.data[0].cpm.cpmImage.fullEcs))
        verify(exactly = 1) {
            topAdsUrlHitter.hitClickUrl(
                any<String>(),
                any(),
                any(),
                any(),
                any(),
            )
        }
    }

    private fun `When autocomplete item model clicked`(viewModel: AutoCompleteViewModel, isAdsDataView: AutoCompleteUnifyDataView, className: String) {
        viewModel.onAutoCompleteItemClick(isAdsDataView, className)
    }
}
