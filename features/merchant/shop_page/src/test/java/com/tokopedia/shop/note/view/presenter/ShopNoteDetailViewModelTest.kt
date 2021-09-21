package com.tokopedia.shop.note.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import org.junit.Rule
import org.junit.Test

internal class ShopNoteDetailViewModelTest: ShopNoteDetailViewModelTestFixtures() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun showShopNoteWhenSuccess() {
        val noteTitleSample = "Title Note"
        val shopNoteModel = ShopNoteModel(
                title = noteTitleSample
        )
        coEvery {
            getShopNoteUseCase.executeOnBackground()
        } returns listOf(shopNoteModel)

        //when
        shopNoteDetailViewModel.getShopNoteList("1177", "7711")

        //then
        coVerify { getShopNoteUseCase.executeOnBackground() }
        assert(shopNoteDetailViewModel.shopNoteDetailData.value is Success)
        assert((shopNoteDetailViewModel.shopNoteDetailData.value as? Success)?.data?.title == noteTitleSample)
    }

    @Test
    fun showErrorShopNoteWhenFailed() {
        coEvery {
            getShopNoteUseCase.executeOnBackground()
        } throws Exception()

        //when
        shopNoteDetailViewModel.getShopNoteList("1177", "7711")

        //then
        coVerify { getShopNoteUseCase.executeOnBackground() }
        assert(shopNoteDetailViewModel.shopNoteDetailData.value is Fail)
    }

}