package com.tokopedia.tokomart.searchcategory

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class UpdateCartTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        private val callback: Callback,
) {

    private val miniCartSimplifiedData = MiniCartSimplifiedData(
            miniCartWidgetData = MiniCartWidgetData(
                    totalProductCount = 10,
                    totalProductPrice = 1000000,
            )
    )

    fun `onViewResumed should update mini cart`() {
        callback.`Given first page API will be successful`()
        `Given get mini cart simplified use case will be successful`()
        `Given view already created`()

        `When view resumed`()

        `Then assert get mini cart simplified use case params`()
        `Then assert mini cart widget live data is updated`()
    }

    private fun `Given get mini cart simplified use case will be successful`() {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `When view resumed`() {
        baseViewModel.onViewResumed()
    }

    private fun `Then assert get mini cart simplified use case params`() {
        val shopIdListSlot = slot<List<String>>()

        verify {
            getMiniCartListSimplifiedUseCase.setParams(capture(shopIdListSlot))
        }

        // Assert shop id list
        //        shopIdListSlot.captured ....
    }

    private fun `Then assert mini cart widget live data is updated`() {
        val expectedMiniCartWidgetData = miniCartSimplifiedData.miniCartWidgetData

        assertThat(baseViewModel.miniCartWidgetLiveData.value, shouldBe(expectedMiniCartWidgetData))
    }

    interface Callback {
        fun `Given first page API will be successful`()
    }
}