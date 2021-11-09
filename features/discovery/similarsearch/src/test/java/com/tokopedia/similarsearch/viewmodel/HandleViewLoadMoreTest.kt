package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.discovery.common.State
import com.tokopedia.similarsearch.SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.shouldBeInstanceOf
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelThreePage
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelTwoPage
import org.junit.Test

internal class HandleViewLoadMoreTest: SimilarSearchTestFixtures() {

    @Test
    fun `Handle View Load More when similar product list has 2 page of data`() {
        val similarProductModelTwoPage = getSimilarProductModelTwoPage()
        `Given view already created and has similar search data`(similarProductModelTwoPage)

        `When handle view load more`()

        `Then assert similar search state is success and contains two page of product list`(
            similarProductModelTwoPage
        )
        `Then assert has next page is false`()
    }

    private fun `When handle view load more`() {
        similarSearchViewModel.onViewLoadMore()
    }

    private fun `Then assert similar search state is success and contains two page of product list`(
        similarProductModel: SimilarProductModel
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val expectedSimilarProductList = similarProductModel.getSimilarProductList().subList(
            0,
            similarProductModel.getSimilarProductList().size
        )

        similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
        similarSearchLiveData.shouldHaveCorrectViewModelListWithoutLoadingMore(expectedSimilarProductList)
    }

    private fun `Then assert has next page is false`() {
        similarSearchViewModel.getHasNextPage().shouldBe(false,
            "Has next page should be false")
    }

    @Test
    fun `Handle View Load More when similar product list has 3 page of data`() {
        val similarProductModelThreePage = getSimilarProductModelThreePage()
        `Given view already created and has similar search data`(similarProductModelThreePage)

        `When handle view load more`()

        `Then assert similar search state is success and contains three page of product list`(
            similarProductModelThreePage
        )
        `Then assert has next page is true`()
    }

    private fun `Then assert similar search state is success and contains three page of product list`(
        similarProductModel: SimilarProductModel
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val expectedSimilarProductList = similarProductModel.getSimilarProductList().subList(
            0,
            SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE * 2
        )

        similarSearchLiveData.shouldBeInstanceOf<State.Success<*>>()
        similarSearchLiveData.shouldHaveCorrectViewModelListWithLoadingMore(expectedSimilarProductList)
    }

    private fun `Then assert has next page is true`() {
        similarSearchViewModel.getHasNextPage().shouldBe(true,
            "Has next page should be true")
    }
}