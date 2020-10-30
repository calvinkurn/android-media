package com.tokopedia.attachproduct.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.presenter.AttachProductContract
import com.tokopedia.attachproduct.view.presenter.AttachProductPresenter
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class AttachProductPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var view: AttachProductContract.View

    @RelaxedMockK
    private lateinit var activityContract: AttachProductContract.Activity

    @RelaxedMockK
    private lateinit var useCase: AttachProductUseCase

    private lateinit var presenter: AttachProductPresenter

    private val queryTest = "Test Query"
    private val shopIdTest = "0"
    private val pageTest = 0

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = AttachProductPresenter(useCase)
        presenter.attachView(view)
        presenter.attachActivityContract(activityContract)
    }

    @Test
    fun `successfully load product data` () {
        val expectedReturn = mockk<List<AttachProductItemUiModel>>(relaxed = true)
        val testSubscriber: TestSubscriber<List<AttachProductItemUiModel>> = TestSubscriber()

        every {
            useCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.loadProductData(queryTest, shopIdTest, pageTest)

        verify {
            useCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `failed to load product data` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<List<AttachProductItemUiModel>> = TestSubscriber()

        every {
            useCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.loadProductData(queryTest, shopIdTest, pageTest)

        verify {
            useCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `on complete selection` () {
        presenter.updateCheckedList(listOf(AttachProductItemUiModel(
                "testUrl",
                "testName",
                0,
                "testImageFull",
                "testImage",
                "testPrice",
                "testShopName")
        ))
        presenter.completeSelection()

        verify {
            activityContract.finishActivityWithResult(any())
        }
    }

    @Test
    fun `on Reset Checked List` () {
        presenter.updateCheckedList(listOf(mockk()))
        presenter.resetCheckedList()

        assertTrue(presenter.checkedList.isEmpty())
    }

    @Test
    fun `on Detach View` () {
        presenter.detachView()

        verify {
            useCase.unsubscribe()
        }
    }
}