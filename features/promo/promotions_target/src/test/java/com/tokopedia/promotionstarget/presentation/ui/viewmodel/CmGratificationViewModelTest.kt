package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import com.tokopedia.promotionstarget.InstantTaskExecutorRuleSpek
import com.tokopedia.promotionstarget.data.LiveDataResult
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.domain.usecase.AutoApplyUseCase
import com.tokopedia.promotionstarget.domain.usecase.UpdateGratifNotification
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.spekframework.spek2.Spek

@ExperimentalCoroutinesApi
class CmGratificationViewModelTest : Spek({
    lateinit var viewModel: CmGratificationViewModel
    lateinit var uiDispatcher: TestCoroutineDispatcher
    lateinit var workerDispatcher: TestCoroutineDispatcher
    lateinit var autoApplyUseCase: AutoApplyUseCase
    lateinit var updateGratifNotification: UpdateGratifNotification
    lateinit var app: Application

    InstantTaskExecutorRuleSpek(this)
    beforeGroup {
        uiDispatcher = TestCoroutineDispatcher()
        workerDispatcher = TestCoroutineDispatcher()
    }

    beforeEachTest {
        autoApplyUseCase = mockk()
        updateGratifNotification = mockk()
        app = mockk()
        viewModel = CmGratificationViewModel(uiDispatcher = uiDispatcher, workerDispatcher = workerDispatcher, autoApplyUseCase = autoApplyUseCase, updateGratifNotificationUsecase = updateGratifNotification, app = app)
    }

    afterEachGroup {
        uiDispatcher.cleanupTestCoroutines()
        workerDispatcher.cleanupTestCoroutines()
    }

    group("autoApply") {
        test("autoApply success") {
            val code = "123"
            val queryParams: HashMap<String, Any> = mockk()
            val autoApplyResponse: AutoApplyResponse = mockk()
            val liveDataResultList = ArrayList<LiveDataResult<AutoApplyResponse>>()
            viewModel.autoApplyLiveData.observeForever {
                liveDataResultList.add( it)
            }
            every { autoApplyUseCase.getQueryParams(code) } returns queryParams
            coEvery { autoApplyUseCase.getResponse(queryParams) } returns autoApplyResponse

            viewModel.autoApply(code)

            assertEquals(liveDataResultList[0].status, LiveDataResult.STATUS.LOADING)
            assertEquals(liveDataResultList[1].status, LiveDataResult.STATUS.SUCCESS)
        }

        test("autoApply fail") {
            val code = "123"
            val queryParams: HashMap<String, Any> = mockk()
            val liveDataResultList = ArrayList<LiveDataResult<AutoApplyResponse>>()
            viewModel.autoApplyLiveData.observeForever {
                liveDataResultList.add( it)
            }
            every { autoApplyUseCase.getQueryParams(code) } returns queryParams
            coEvery { autoApplyUseCase.getResponse(queryParams) } throws Exception()

            viewModel.autoApply(code)

            assertEquals(liveDataResultList[0].status, LiveDataResult.STATUS.LOADING)
            assertEquals(liveDataResultList[1].status, LiveDataResult.STATUS.ERROR)
        }
    }

    group("updateGratification"){
        test("updateGratification success"){
            val notificationId = "1"
            val notificationEntryType = 1
            val notificationPopType = 1
            val screenName = javaClass.name
            val inAppId = 1L

            val queryParams:HashMap<String,Any> = mockk()
            every { updateGratifNotification.getQueryParams(notificationId.toInt(),notificationEntryType,notificationPopType,screenName) } returns queryParams
            coEvery { updateGratifNotification.getResponse(queryParams) } returns mockk()
            viewModel.updateGratification(notificationID = notificationId,
                    notificationEntryType = notificationEntryType,
                    popupType = notificationPopType,
                    screenName = screenName,
                    inAppId = inAppId)

            coVerifyOrder {
                updateGratifNotification.getQueryParams(notificationId.toInt(),notificationEntryType,notificationPopType,screenName)
                updateGratifNotification.getResponse(queryParams)
            }
        }

        test("updateGratification do nothing"){
            val notificationId = null
            val notificationEntryType = 1
            val notificationPopType = 1
            val screenName = javaClass.name
            val inAppId = 1L

            val queryParams:HashMap<String,Any> = mockk()
            every { updateGratifNotification.getQueryParams(any(),notificationEntryType,notificationPopType,screenName) } returns queryParams
            coEvery { updateGratifNotification.getResponse(queryParams) } returns mockk()
            viewModel.updateGratification(notificationID = notificationId,
                    notificationEntryType = notificationEntryType,
                    popupType = notificationPopType,
                    screenName = screenName,
                    inAppId = inAppId)

            coVerify(exactly = 0) {
                updateGratifNotification.getQueryParams(any(),notificationEntryType,notificationPopType,screenName)
                updateGratifNotification.getResponse(queryParams)
            }
        }
    }
})