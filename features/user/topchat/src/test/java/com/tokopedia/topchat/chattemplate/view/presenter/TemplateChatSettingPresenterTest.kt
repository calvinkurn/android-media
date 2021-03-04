package com.tokopedia.topchat.chattemplate.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class TemplateChatSettingPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getTemplateUseCase: GetTemplateUseCase
    @RelaxedMockK
    private lateinit var setAvailabilityTemplateUseCase: SetAvailabilityTemplateUseCase
    @RelaxedMockK
    private lateinit var view: TemplateChatContract.View

    private lateinit var presenter: TemplateChatSettingPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = TemplateChatSettingPresenter(getTemplateUseCase, setAvailabilityTemplateUseCase)
        presenter.attachView(view)
    }

    private fun everySuccessGetTemplate(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            getTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }
    }

    private fun everyFailGetTemplate(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            getTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }
    }

    private fun testGetTemplate(testIsSeller: Boolean) {

        presenter.setMode(testIsSeller)
        presenter.getTemplate()

        verify {
            getTemplateUseCase.execute(any(), any())
        }
    }

    private fun successGetTemplate(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun failGetTemplate(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully get template buyer` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsSeller = false

        everySuccessGetTemplate(expectedReturn, testSubscriber)
        testGetTemplate(testIsSeller)
        successGetTemplate(expectedReturn, testSubscriber)
    }

    @Test
    fun `successfully get template seller` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsSeller = true

        everySuccessGetTemplate(expectedReturn, testSubscriber)
        testGetTemplate(testIsSeller)
        successGetTemplate(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed get template buyer` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsSeller = false

        everyFailGetTemplate(expectedReturn, testSubscriber)
        testGetTemplate(testIsSeller)
        failGetTemplate(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed get template seller` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsSeller = true

        everyFailGetTemplate(expectedReturn, testSubscriber)
        testGetTemplate(testIsSeller)
        failGetTemplate(expectedReturn, testSubscriber)
    }

    private fun everySucessSwitchAvailability(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            setAvailabilityTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }
    }

    private fun everyFailSwitchAvailability(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            setAvailabilityTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }
    }

    private fun testSwitchAvailability(testIsEnabled: Boolean) {
        presenter.switchTemplateAvailability(testIsEnabled)

        verify {
            setAvailabilityTemplateUseCase.execute(any(), any())
        }
    }

    private fun successSwitchAvailability(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun failSwitchAvailability(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully switch availability to true` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = true

        everySucessSwitchAvailability(expectedReturn, testSubscriber)
        testSwitchAvailability(testIsEnabled)
        successSwitchAvailability(expectedReturn, testSubscriber)
    }

    @Test
    fun `successfully switch availability to false` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = false

        everySucessSwitchAvailability(expectedReturn, testSubscriber)
        testSwitchAvailability(testIsEnabled)
        successSwitchAvailability(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed switch availability to true` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = true

        everyFailSwitchAvailability(expectedReturn, testSubscriber)
        testSwitchAvailability(testIsEnabled)
        failSwitchAvailability(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed switch availability to false` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = false

        everyFailSwitchAvailability(expectedReturn, testSubscriber)
        testSwitchAvailability(testIsEnabled)
        failSwitchAvailability(expectedReturn, testSubscriber)
    }

    private fun everySuccessSetArrange(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            setAvailabilityTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }
    }

    private fun everyFailSetArrange(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        every {
            setAvailabilityTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }
    }

    private fun testSetArrange(testIsEnabled: Boolean, testArrayList: ArrayList<Int>?, testFrom: Int, testTo: Int) {
        presenter.setArrange(testIsEnabled, testArrayList, testFrom, testTo)

        verify {
            setAvailabilityTemplateUseCase.execute(any(), any())
        }
    }

    private fun successSetArrange(expectedReturn: GetTemplateUiModel, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun failSetArrange(expectedReturn: Throwable, testSubscriber: TestSubscriber<GetTemplateUiModel>) {
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully set arrange to true with null arraylist` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = true
        val testArrayList: ArrayList<Int>? = null
        val testFrom = 0
        val testTo = 1

        everySuccessSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        successSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `successfully set arrange to false with null arraylist` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = false
        val testArrayList: ArrayList<Int>? = null
        val testFrom = 0
        val testTo = 1

        everySuccessSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        successSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `successfully set arrange to true with not null array list` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = true
        val testArrayList: ArrayList<Int> = arrayListOf(0, 1, 2)
        val testFrom = 0
        val testTo = 1

        everySuccessSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        successSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `successfully set arrange to false with not null array list` () {
        val expectedReturn = mockk<GetTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = false
        val testArrayList: ArrayList<Int> = arrayListOf(0, 1, 2)
        val testFrom = 0
        val testTo = 1

        everySuccessSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        successSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed set arrange to true` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = true
        val testArrayList: ArrayList<Int>? = null
        val testFrom = 0
        val testTo = 1

        everyFailSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        failSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `failed set arrange to false` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val testIsEnabled = false
        val testArrayList: ArrayList<Int>? = null
        val testFrom = 0
        val testTo = 1

        everyFailSetArrange(expectedReturn, testSubscriber)
        testSetArrange(testIsEnabled, testArrayList, testFrom, testTo)
        failSetArrange(expectedReturn, testSubscriber)
    }

    @Test
    fun `reload template` () {
        presenter.reloadTemplate()
        verify { view.showLoading() }
    }

    @Test
    fun `on detach` () {
        presenter.detachView()
        verify {
            getTemplateUseCase.unsubscribe()
            setAvailabilityTemplateUseCase.unsubscribe()
        }
    }

}