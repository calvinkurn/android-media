package com.tokopedia.shop.flashsale.presentation.creation.draft

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCancellationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignCancellationListUseCase
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flashsale.presentation.draft.viewmodel.DraftDeleteViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DraftDeleteViewModelTest {
    @RelaxedMockK
    lateinit var cancellationListUseCase: GetSellerCampaignCancellationListUseCase
    @RelaxedMockK
    lateinit var cancellationUseCase: DoSellerCampaignCancellationUseCase
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var viewModel: DraftDeleteViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = DraftDeleteViewModel(
            CoroutineTestDispatchersProvider,
            cancellationListUseCase,
            cancellationUseCase
        )
    }

    @Test
    fun `When cancellationListUseCase success, Expect trigger success livedata`() {
        runBlocking {
            coEvery {
                cancellationListUseCase.execute()
            } returns listOf("what?", "why?", "who?")
            with(viewModel) {
                getQuestionListData()
                val questionlist = questionListData.getOrAwaitValue()
                assert(!(questionlist as? Success)?.data.isNullOrEmpty())
            }
        }
    }

    @Test
    fun `When cancellationListUseCase failed, Expect trigger fail livedata`() {
        runBlocking {
            coEvery {
                cancellationListUseCase.execute()
            } throws MessageErrorException()
            with(viewModel) {
                getQuestionListData()
                val questionlist = questionListData.getOrAwaitValue()
                assert(questionlist is Fail)
            }
        }
    }

    @Test
    fun `When doCancellation success, Expect trigger success livedata`() {
        runBlocking {
            val reasonTest = "reason test"
            coEvery {
                cancellationUseCase.execute(any(), reasonTest)
            } returns true
            with(viewModel) {
                setCancellationReason(reasonTest)
                doCancellation(DraftItemModel(id = 1))
                val status = cancellationStatus.getOrAwaitValue()
                assert(status)
            }
        }
    }

    @Test
    fun `When doCancellation success with failed response, Expect trigger success livedata`() {
        runBlocking {
            val reasonTest = "reason test"
            coEvery {
                cancellationUseCase.execute(any(), reasonTest)
            } returns false
            with(viewModel) {
                setCancellationReason(reasonTest)
                doCancellation(DraftItemModel(id = 1))
                val status = cancellationStatus.getOrAwaitValue()
                assert(!status)
            }
        }
    }

    @Test
    fun `When doCancellation failed, Expect trigger fail livedata`() {
        runBlocking {
            val reasonTest = "reason test"
            coEvery {
                cancellationUseCase.execute(any(), reasonTest)
            } throws MessageErrorException("error")
            with(viewModel) {
                setCancellationReason(reasonTest)
                doCancellation(DraftItemModel(id = 1))
                val cancellationErrorMessage = cancellationError.getOrAwaitValue()
                assert(cancellationErrorMessage.isNotEmpty())
            }
        }
    }

}
