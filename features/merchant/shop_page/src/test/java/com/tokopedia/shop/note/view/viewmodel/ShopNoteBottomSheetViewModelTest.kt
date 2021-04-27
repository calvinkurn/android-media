package com.tokopedia.shop.note.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopNoteBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getShopNotesByShopIdUseCase: GetShopNotesByShopIdUseCase

    private lateinit var shopEditBasicInfoViewModel: ShopNoteBottomSheetViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopEditBasicInfoViewModel = ShopNoteBottomSheetViewModel(
                coroutineTestRule.dispatchers,
                getShopNotesByShopIdUseCase
        )
    }

    @Test
    fun `when get shop notes data should return success`() {
        runBlocking {
            onCheckGetShopNotesSuccess_thenReturn()

            shopEditBasicInfoViewModel.getShopNotes("1000")

            verifyGetShopNotesCalled()

            val isGetShopBasicDataSubscribe = shopEditBasicInfoViewModel.shopNotes.observeAwaitValue()

            TestCase.assertTrue(isGetShopBasicDataSubscribe is Success)
        }
    }

    @Test
    fun `when get shop notes data should return fail`() {
        runBlocking {
            onCheckGetShopNotesFail_thenReturn()

            shopEditBasicInfoViewModel.getShopNotes("1000")

            verifyGetShopNotesCalled()

            val isGetShopBasicDataSubscribe = shopEditBasicInfoViewModel.shopNotes.observeAwaitValue()

            TestCase.assertTrue(isGetShopBasicDataSubscribe is Fail)
        }
    }

    private fun onCheckGetShopNotesSuccess_thenReturn() {
        coEvery { getShopNotesByShopIdUseCase.executeOnBackground() } returns listOf()
    }

    private fun onCheckGetShopNotesFail_thenReturn() {
        coEvery { getShopNotesByShopIdUseCase.executeOnBackground() } throws  Exception()
    }

    private fun verifyGetShopNotesCalled() {
        coVerify { getShopNotesByShopIdUseCase.executeOnBackground() }
    }

}