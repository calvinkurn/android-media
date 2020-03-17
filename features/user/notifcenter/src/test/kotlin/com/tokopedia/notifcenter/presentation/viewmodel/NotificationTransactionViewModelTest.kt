package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.notifcenter.data.entity.NotificationEntity
import com.tokopedia.notifcenter.data.entity.NotificationUpdateFilter
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.notifcenter.util.InstantTaskExecutorRule
import com.tokopedia.notifcenter.util.coroutines.TestDispatcherProvider
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class NotificationTransactionViewModelTest: Spek({
    InstantTaskExecutorRule(this)

    val notificationInfoTransactionUseCase = mockk<NotificationInfoTransactionUseCase>(relaxed = true)
    val markAllNotificationUseCase = mockk<MarkAllReadNotificationUpdateUseCase>(relaxed = true)
    val markNotificationUseCase = mockk<MarkReadNotificationUpdateItemUseCase>(relaxed = true)
    val notificationTotalUnreadUseCase = mockk<GetNotificationTotalUnreadUseCase>(relaxed = true)
    val notificationFilterMapper  = mockk<GetNotificationUpdateFilterMapper>(relaxed = true)
    val notificationTransactionUseCase = mockk<NotificationTransactionUseCase>(relaxed = true)
    val notificationFilterUseCase = mockk<NotificationFilterUseCase>(relaxed = true)
    val notificationMapper = mockk<GetNotificationUpdateMapper>(relaxed = true)

    val dispatcher = TestDispatcherProvider()

    val viewModel = NotificationTransactionViewModel(
            notificationInfoTransactionUseCase = notificationInfoTransactionUseCase,
            notificationTransactionUseCase = notificationTransactionUseCase,
            getNotificationTotalUnreadUseCase = notificationTotalUnreadUseCase,
            markAllReadNotificationUseCase = markAllNotificationUseCase,
            markNotificationUseCase = markNotificationUseCase,
            notificationFilterUseCase = notificationFilterUseCase,
            notificationFilterMapper = notificationFilterMapper,
            notificationMapper = notificationMapper,
            dispatcher = dispatcher
    )

    Feature("info transaction") {
        val observerSuccess = mockk<Observer<NotificationEntity>>(relaxed = true)
        val observerError = mockk<Observer<String>>(relaxed = true)
        val notificationEntity = NotificationEntity()
        val throwableMock = Throwable("Opps!")

        Scenario("get info transaction") {
            Given("usecase on success properly") {
                every { notificationInfoTransactionUseCase.get(captureLambda(), any()) } answers {
                    val onSuccess = lambda<(NotificationEntity) -> Unit>()
                    onSuccess.invoke(notificationEntity)
                }
                viewModel.infoNotification.observeForever(observerSuccess)
            }
            When("get info transaction data") {
                viewModel.getInfoStatusNotification()
            }
            Then("it should return info transaction correctly") {
                verify { observerSuccess.onChanged(notificationEntity) }
                viewModel.infoNotification.value.shouldBeInstanceOf<NotificationEntity>()
            }
        }

        Scenario("throw exception while get info transaction") {
            Given("usecase with throwable") {
                every { notificationInfoTransactionUseCase.get(any(), captureLambda()) } answers {
                    val onError = lambda<(Throwable) -> Unit>()
                    onError.invoke(throwableMock)
                }
                viewModel.errorMessage.observeForever(observerError)
            }
            When("get info transaction data") {
                viewModel.getInfoStatusNotification()
            }
            Then("it should return throwable message") {
                assert(viewModel.errorMessage.value!!.isNotEmpty())
            }
        }
    }

    Feature("notification filter") {
        val observerSuccess = mockk<Observer<NotificationFilterSectionViewBean>>(relaxed = true)
        val observerError = mockk<Observer<String>>(relaxed = true)
        val notificationUpdateFilterDataView = NotificationFilterSectionViewBean(arrayListOf())
        val notificationUpdateFilter = NotificationUpdateFilter()

        val mockParam = hashMapOf<String, Any>(
                "typeOfNotif" to 2
        )

        Scenario("get notification filter with empty param") {
            Given("empty param") {
                viewModel.filterNotificationParams = hashMapOf()
            }
            When("get notification filter transaction data") {
                viewModel.getNotificationFilter()
            }
            Then("it should be usecase not called") {
                verify(exactly = 0) {
                    notificationFilterUseCase.get(any(), any(), any())
                }
            }
        }

        Scenario("get notification filter") {
            Given("correct param") {
                viewModel.filterNotificationParams = NotificationFilterUseCase.params()
            }
            Given("usecase on success properly") {
                every { notificationFilterUseCase.get(any(), captureLambda(), any()) } answers {
                    val onSuccess = lambda<(NotificationUpdateFilter) -> Unit>()
                    onSuccess.invoke(notificationUpdateFilter)
                }
                viewModel.filterNotification.observeForever(observerSuccess)
            }
            When("get notification filter transaction data") {
                viewModel.getNotificationFilter()
            }
//            Then("it should return filter notification correctly") {
////                assertEquals(mockParam, viewModel.filterNotificationParams)
////                assert(viewModel.filterNotification.value == notificationUpdateFilterDataView)
////            }
        }
    }
})

private inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}