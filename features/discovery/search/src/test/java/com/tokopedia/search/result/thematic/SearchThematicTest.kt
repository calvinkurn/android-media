package com.tokopedia.search.result.thematic

import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.home_component.usecase.thematic.ThematicResponse
import com.tokopedia.home_component.usecase.thematic.ThematicUsecaseUtil
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.SearchState
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import io.mockk.slot
import org.junit.Test

private const val thematicShowResponse =
    "searchproduct/thematic/successful-response-thematic-show.json"

private const val thematicDarkResponse =
    "searchproduct/thematic/successful-response-thematic-dark.json"

private const val thematicLightResponse =
    "searchproduct/thematic/successful-response-thematic-light.json"

internal class SearchThematicTest {

    private val thematicUseCase = mockk<UseCase<ThematicModel>>(relaxed = true)
    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `should show thematic state on search result page`() {
        val viewModel = SearchViewModel(
            SearchState(),
            CoroutineTestDispatchersProvider,
            thematicUseCase
        )

        `Given Get Thematic Success Show`()

        `When Ui Get Thematic`(viewModel)
        `Then Assert shouldShowThematic is true and captured parameters is SRP`(viewModel)
    }

    private fun `Then Assert shouldShowThematic is true and captured parameters is SRP`(
        viewModel: SearchViewModel
    ) {
        assert(viewModel.stateFlow.value.shouldShowThematic())
        assert(
            requestParamsSlot.captured.parameters[ThematicUsecaseUtil.THEMATIC_PARAM] ==
                ThematicUsecaseUtil.THEMATIC_PAGE_SRP
        )
    }

    private fun `When Ui Get Thematic`(viewModel: SearchViewModel) {
        viewModel.getThematic()
    }

    private fun `Given Get Thematic Success Show`() {
        val thematicResponse = thematicShowResponse.jsonToObject<ThematicResponse>()
        val thematicModel = ThematicModel.fromResponse(thematicResponse.getThematic.thematic)

        thematicUseCase.stubExecute(requestParamsSlot) returns thematicModel
    }

    @Test
    fun `thematic should isThematicDarkMode`() {
        val viewModel = SearchViewModel(
            SearchState(),
            CoroutineTestDispatchersProvider,
            thematicUseCase
        )

        `Given Get Thematic Success Dark`()

        `When Ui Get Thematic`(viewModel)

        `Then Assert That Thematic Is Dark Mode`(viewModel)
    }

    private fun `Then Assert That Thematic Is Dark Mode`(viewModel: SearchViewModel) {
        assert(viewModel.stateFlow.value.isThematicDarkMode())
    }

    private fun `Given Get Thematic Success Dark`() {
        val thematicResponse = thematicDarkResponse.jsonToObject<ThematicResponse>()
        val thematicModel = ThematicModel.fromResponse(thematicResponse.getThematic.thematic)

        thematicUseCase.stubExecute(requestParamsSlot) returns thematicModel
    }

    @Test
    fun `thematic should isThematicLightMode`() {
        val viewModel = SearchViewModel(
            SearchState(),
            CoroutineTestDispatchersProvider,
            thematicUseCase
        )

        `Given Get Thematic Success Light`()

        `When Ui Get Thematic`(viewModel)

        `Then Assert That Thematic Is Light Mode`(viewModel)
    }

    private fun `Then Assert That Thematic Is Light Mode`(viewModel: SearchViewModel) {
        assert(viewModel.stateFlow.value.isThematicLightMode())
    }

    private fun `Given Get Thematic Success Light`() {
        val thematicResponse = thematicLightResponse.jsonToObject<ThematicResponse>()
        val thematicModel = ThematicModel.fromResponse(thematicResponse.getThematic.thematic)

        thematicUseCase.stubExecute(requestParamsSlot) returns thematicModel
    }
}
