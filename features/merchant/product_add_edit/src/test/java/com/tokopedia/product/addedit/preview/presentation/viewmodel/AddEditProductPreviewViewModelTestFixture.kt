package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.domain.usecase.GetProductUseCase
import com.tokopedia.shop.common.domain.interactor.AdminPermissionUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach

abstract class AddEditProductPreviewViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getProductUseCase: GetProductUseCase

    @RelaxedMockK
    lateinit var getProductDraftUseCase: GetProductDraftUseCase

    @RelaxedMockK
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase

    @RelaxedMockK
    lateinit var adminPermissionUseCase: AdminPermissionUseCase

    @RelaxedMockK
    lateinit var getProductMapper: GetProductMapper

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getProductResultObserver: Observer<in Result<Product>>

    protected val viewModel: AddEditProductPreviewViewModel by lazy {
        spyk(AddEditProductPreviewViewModel(getProductUseCase,
                getProductMapper,
                resourceProvider,
                getProductDraftUseCase,
                saveProductDraftUseCase,
                adminPermissionUseCase,
                userSession,
                CoroutineTestDispatchersProvider))
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)

        viewModel.getProductResult.observeForever(getProductResultObserver)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()

        viewModel.getProductResult.removeObserver(getProductResultObserver)
    }
}