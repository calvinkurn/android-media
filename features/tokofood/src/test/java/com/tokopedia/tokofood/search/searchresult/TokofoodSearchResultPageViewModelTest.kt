package com.tokopedia.tokofood.search.searchresult

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.tokofood.utils.collectFromSharedFlow
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class TokofoodSearchResultPageViewModelTest: TokofoodSearchResultPageViewModelTestFixture() {

    @Test
    fun `when getInitialMerchantSearchResult should success load search result visitables`() {
        val localCacheModel = LocalCacheModel()
        val searchResult = getSearchResultResponse()
        onGetSearchResult_shouldReturn(localCacheModel, null, searchResult)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(null)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.count()
                Assert.assertEquals(searchResult.tokofoodSearchMerchant.merchants.count(), actualVisitableCount)
            }
        )
    }

    @Test
    fun `when setKeyword should fail load search result visitables`() {
        val localCacheModel = LocalCacheModel()
        val throwable = MessageErrorException()
        onGetSearchResult_shouldThrow(localCacheModel, null, throwable)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(null)
            },
            then = {
                val actualVisitable = (it as? Success)?.data
                assert(actualVisitable?.count() == Int.ONE)
                assert(actualVisitable?.getOrNull(Int.ZERO) is TokofoodSearchErrorStateUiModel)
            }
        )
    }

}