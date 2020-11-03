package com.tokopedia.topchat.chattemplate.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.EditTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.listener.EditTemplateChatContract
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class EditTemplateChatPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var editTemplateUseCase: EditTemplateUseCase
    @RelaxedMockK
    private lateinit var createTemplateUseCase: CreateTemplateUseCase
    @RelaxedMockK
    private lateinit var deleteTemplateUseCase: DeleteTemplateUseCase
    @RelaxedMockK
    private lateinit var view: EditTemplateChatContract.View;

    private lateinit var presenter: EditTemplateChatPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = EditTemplateChatPresenter(editTemplateUseCase, createTemplateUseCase, deleteTemplateUseCase)
        presenter.attachView(view)
    }

    private fun everySuccessEditTemplate(expectedReturn: EditTemplateUiModel, testSubscriber: TestSubscriber<EditTemplateUiModel>){
        every {
            editTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }
    }

    private fun everyFailedEditTemplate(expectedReturn: Throwable, testSubscriber: TestSubscriber<EditTemplateUiModel>){
        every {
            editTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }
    }

    private fun everySuccessCreateTemplate(expectedReturn: EditTemplateUiModel, testSubscriber: TestSubscriber<EditTemplateUiModel>){
        every {
            createTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }
    }

    private fun everyFailedCreateTemplate(expectedReturn: Throwable, testSubscriber: TestSubscriber<EditTemplateUiModel>){
        every {
            createTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }
    }

    private fun successfullyEditSubmitText(
            expectedReturn: EditTemplateUiModel,
            testSubscriber: TestSubscriber<EditTemplateUiModel>,
            testIsSeller: Boolean,
            testString: String,
            testMessage: String,
            testList: List<String>
    ) {
        presenter.setMode(testIsSeller)
        presenter.submitText(testString, testMessage, testList)

        verify {
            editTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun failedEditSubmitText(
            expectedReturn: Throwable,
            testSubscriber: TestSubscriber<EditTemplateUiModel>,
            testIsSeller: Boolean,
            testString: String,
            testMessage: String,
            testList: List<String>
    ) {

        presenter.setMode(testIsSeller)
        presenter.submitText(testString, testMessage, testList)

        verify {
            editTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun successfullyCreateSubmitText(
            expectedReturn: EditTemplateUiModel,
            testSubscriber: TestSubscriber<EditTemplateUiModel>,
            testIsSeller: Boolean,
            testString: String,
            testMessage: String,
            testList: List<String>
    ) {
        presenter.setMode(testIsSeller)
        presenter.submitText(testString, testMessage, testList)

        verify {
            createTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    private fun failedCreateSubmitText(
            expectedReturn: Throwable,
            testSubscriber: TestSubscriber<EditTemplateUiModel>,
            testIsSeller: Boolean,
            testString: String,
            testMessage: String,
            testList: List<String>
    ) {
        presenter.setMode(testIsSeller)
        presenter.submitText(testString, testMessage, testList)

        verify {
            createTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully submit text edit buyer` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true).apply {
            isSuccess = true
        }
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test1"
        val testMessage = "Test1"
        val testList = listOf<String>("Test1", "Test2", "Test3")

        everySuccessEditTemplate(expectedReturn, testSubscriber)
        successfullyEditSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `successfully submit text edit seller` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true).apply {
            isSuccess = true
        }
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test1"
        val testMessage = "Test1"
        val testList = listOf<String>("Test1", "Test2", "Test3")

        everySuccessEditTemplate(expectedReturn, testSubscriber)
        successfullyEditSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `failed submit text edit buyer` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test1"
        val testMessage = "Test1"
        val testList = listOf<String>("Test1", "Test2", "Test3")

        everyFailedEditTemplate(expectedReturn, testSubscriber)
        failedEditSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `failed submit text edit seller` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test1"
        val testMessage = "Test1"
        val testList = listOf<String>("Test1", "Test2", "Test3")

        everyFailedEditTemplate(expectedReturn, testSubscriber)
        failedEditSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `successfully submit text create buyer` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true).apply {
            isSuccess = true
        }
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test"
        val testMessage = "Test Message"
        val testList = listOf<String>()

        everySuccessCreateTemplate(expectedReturn, testSubscriber)
        successfullyCreateSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `successfully submit text create seller` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true).apply {
            isSuccess = true
        }
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test"
        val testMessage = "Test Message"
        val testList = listOf<String>()

        everySuccessCreateTemplate(expectedReturn, testSubscriber)
        successfullyCreateSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `failed submit text create buyer` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test"
        val testMessage = "Test Message"
        val testList = listOf<String>()

        everyFailedCreateTemplate(expectedReturn, testSubscriber)
        failedCreateSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    @Test
    fun `failed submit text create seller` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString  = "Test"
        val testMessage = "Test Message"
        val testList = listOf<String>()

        everyFailedCreateTemplate(expectedReturn, testSubscriber)
        failedCreateSubmitText(expectedReturn, testSubscriber, testIsSeller, testString, testMessage, testList)
    }

    private fun successfullyDeleteTemplate(expectedReturn: EditTemplateUiModel, testSubscriber: TestSubscriber<EditTemplateUiModel>, testIsSeller: Boolean, testIndex: Int) {
        every {
            deleteTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.setMode(testIsSeller)
        presenter.deleteTemplate(testIndex)

        verify {
            deleteTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()

    }

    private fun failedDeleteTemplate(expectedReturn: Throwable, testSubscriber: TestSubscriber<EditTemplateUiModel>, testIsSeller: Boolean, testIndex: Int) {
        every {
            deleteTemplateUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.setMode(testIsSeller)
        presenter.deleteTemplate(testIndex)

        verify {
            deleteTemplateUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully delete template buyer` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true)
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        successfullyDeleteTemplate(expectedReturn, testSubscriber, testIsSeller, testIndex)
    }

    @Test
    fun `successfully delete template seller` () {
        val expectedReturn = mockk<EditTemplateUiModel>(relaxed = true)
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        successfullyDeleteTemplate(expectedReturn, testSubscriber, testIsSeller, testIndex)
    }

    @Test
    fun `error delete template buyer` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = false
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        failedDeleteTemplate(expectedReturn, testSubscriber, testIsSeller, testIndex)
    }

    @Test
    fun `error delete template seller` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testIsSeller = true
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        failedDeleteTemplate(expectedReturn, testSubscriber, testIsSeller, testIndex)
    }

    @Test
    fun `on detach` () {
        presenter.detachView()
        verify {
            editTemplateUseCase.unsubscribe()
            createTemplateUseCase.unsubscribe()
            deleteTemplateUseCase.unsubscribe()
        }
    }
}