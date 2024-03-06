package com.tokopedia.tokopedianow.annotation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import com.tokopedia.tokopedianow.annotation.presentation.viewmodel.TokoNowAllAnnotationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import org.junit.Before
import org.junit.Rule

abstract class TokoNowAllAnnotationViewModelTestFixture {
    @RelaxedMockK
    protected lateinit var getAllAnnotationPageUseCase: GetAllAnnotationPageUseCase

    protected lateinit var viewModel: TokoNowAllAnnotationViewModel

    protected val categoryId = "123"
    protected val warehouses = "1234%232h%2C54321%23f"
    protected val annotationType = "BRAND"

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowAllAnnotationViewModel(
            getAllAnnotationPageUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun stubGetAllAnnotation(
        pageLastId: String,
        response: TokoNowGetAnnotationListResponse.GetAnnotationListResponse
    ) {
        coEvery {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                annotationType = AnnotationType.valueOf(annotationType),
                pageLastId = pageLastId
            )
        } returns response
    }

    protected fun stubGetAllAnnotation(
        pageLastId: String,
        throwable: Throwable
    ) {
        coEvery {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                annotationType = AnnotationType.valueOf(annotationType),
                pageLastId = pageLastId
            )
        } throws throwable
    }

    protected fun verifyGetAllAnnotation(
        pageLastId: String,
        inverse: Boolean = false
    ) {
        coVerify(inverse = inverse) {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                annotationType = AnnotationType.valueOf(annotationType),
                pageLastId = pageLastId
            )
        }
    }
}
