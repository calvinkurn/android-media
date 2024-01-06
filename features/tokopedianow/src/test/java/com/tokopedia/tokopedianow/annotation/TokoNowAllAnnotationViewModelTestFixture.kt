package com.tokopedia.tokopedianow.annotation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import com.tokopedia.tokopedianow.annotation.presentation.viewmodel.TokoNowAllAnnotationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TokoNowAllAnnotationViewModelTestFixture {
    @RelaxedMockK
    protected lateinit var getAllAnnotationPageUseCase: GetAllAnnotationPageUseCase

    protected lateinit var viewModel: TokoNowAllAnnotationViewModel

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
        categoryId: String,
        warehouses: String,
        annotationType: String,
        pageLastId: String,
        response: TokoNowGetAnnotationListResponse.GetAnnotationListResponse
    ) {
        coEvery {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                warehouses = warehouses,
                annotationType = annotationType,
                pageLastId = pageLastId
            )
        } returns response
    }

    protected fun stubGetAllAnnotation(
        categoryId: String,
        warehouses: String,
        annotationType: String,
        pageLastId: String,
        throwable: Throwable
    ) {
        coEvery {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                warehouses = warehouses,
                annotationType = annotationType,
                pageLastId = pageLastId
            )
        } throws throwable
    }

    protected fun verifyGetAllAnnotation(
        categoryId: String,
        warehouses: String,
        annotationType: String,
        pageLastId: String,
        inverse: Boolean = false
    ) {
        coVerify(inverse = inverse) {
            getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                warehouses = warehouses,
                annotationType = annotationType,
                pageLastId = pageLastId
            )
        }
    }
}
