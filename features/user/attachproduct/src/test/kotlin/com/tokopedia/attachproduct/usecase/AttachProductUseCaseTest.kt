package com.tokopedia.attachproduct.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.attachproduct.data.repository.AttachProductRepository
import com.tokopedia.attachproduct.domain.model.mapper.DataModelToDomainModelMapper
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class AttachProductUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var mapperTest: DataModelToDomainModelMapper

    @RelaxedMockK
    private lateinit var repositoryTest: AttachProductRepository

    private lateinit var usecase: AttachProductUseCase

    private val queryTest = "Test Query"
    private val shopIdTest = "0"
    private val pageTest = 0

    @Before
    fun before() {
        MockKAnnotations.init(this)
        usecase = AttachProductUseCase(repositoryTest, mapperTest)
    }

    @Test
    fun `Successfully Get Product List`() {
        val expectedResult = mockk<List<AttachProductItemUiModel>>(relaxed = true)
        val testSubscriber: TestSubscriber<List<AttachProductItemUiModel>> = TestSubscriber()

        every {
            repositoryTest.loadProductFromShop(any()).map(mapperTest)
        } returns Observable.just(expectedResult)

        val observable: Observable<List<AttachProductItemUiModel>> = usecase.getExecuteObservable(AttachProductUseCase.
            createRequestParams(queryTest, shopIdTest, pageTest)
        )

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed Get Product List`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<List<AttachProductItemUiModel>> = TestSubscriber()

        every {
            repositoryTest.loadProductFromShop(any()).map(mapperTest)
        } returns Observable.error(expectedResult)

        val observable: Observable<List<AttachProductItemUiModel>> = usecase.getExecuteObservable(AttachProductUseCase.
        createRequestParams(queryTest, shopIdTest, pageTest)
        )

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}