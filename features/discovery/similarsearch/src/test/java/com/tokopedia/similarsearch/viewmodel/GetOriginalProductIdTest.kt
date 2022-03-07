package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.testutils.TestException
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

internal class GetOriginalProductIdTest: SimilarSearchTestFixtures() {

    @Test
    fun `get original product id after GetSimilarProducts API success`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        getSimilarProductsUseCase.stubExecute().returns(getSimilarProductModelCommon())
        similarSearchViewModel.onViewCreated()

        val originalProductId = similarSearchViewModel.getOriginalProductId()

        assertThat(
            originalProductId,
            `is`(similarProductModelCommon.getOriginalProduct().id)
        )
    }

    @Test
    fun `get original product id after GetSimilarProducts API failed`() {
        getSimilarProductsUseCase.stubExecute().throws(TestException())
        similarSearchViewModel.onViewCreated()

        val originalProductId = similarSearchViewModel.getOriginalProductId()

        assertThat(
            originalProductId,
            `is`("")
        )
    }
}