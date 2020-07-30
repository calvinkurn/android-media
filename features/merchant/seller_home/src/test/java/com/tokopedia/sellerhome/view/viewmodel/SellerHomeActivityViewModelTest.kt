package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Created By @ilhamsuaib on 24/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun createViewModel() =
        SellerHomeActivityViewModel(userSession, getNotificationUseCase, getShopInfoUseCase, Dispatchers.Unconfined)

    @Test
    fun `get notifications then returns success result`() {

        val notifications = NotificationUiModel(NotificationChatUiModel(), NotificationCenterUnreadUiModel(),
            NotificationSellerOrderStatusUiModel())

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getNotifications()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                getNotificationUseCase.executeOnBackground()
            }
            assertEquals(Success(notifications), viewModel.notifications.value)
        }
    }

    @Test
    fun `get notifications then returns failed result`() = runBlocking {

        val throwable = MessageErrorException()

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.notifications.value is Fail)
    }

    @Test
    fun `get shop info then returns success result`() {
        val userId = "123456"
        val shopInfo = ShopInfoUiModel()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } returns shopInfo

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getShopInfo()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                userSession.userId
            }
            coVerify {
                getShopInfoUseCase.executeOnBackground()
            }
        }

        assertEquals(Success(shopInfo), viewModel.shopInfo.value)
    }

    @Test
    fun `get shop info then returns failed result`() = runBlocking {
        val userId = "123456"
        val throwable = MessageErrorException()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getShopInfo()

        verify {
            userSession.userId
        }

        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.shopInfo.value is Fail)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block without custom context`() = runBlocking {
        val customOnErrorViewModel = CustomOnErrorViewModel(Job())
        customOnErrorViewModel.noCustomContext()
        customOnErrorViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block with custom context`() = runBlocking {
        val job = Job()
        val customOnErrorViewModel = CustomOnErrorViewModel(job)
        customOnErrorViewModel.withCustomContext()
        job.children.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    inner class CustomOnErrorViewModel(private val job: Job) : CustomBaseViewModel(Dispatchers.Unconfined) {

        private val _mockLiveData = MutableLiveData<Result<Any>>()
        val mockLiveData: LiveData<Result<Any>>
            get() = _mockLiveData

        fun noCustomContext() = executeCall(_mockLiveData, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        fun withCustomContext() = executeCall(_mockLiveData, job, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        private fun getResult(): Any {
            throw RuntimeException()
        }
    }
}