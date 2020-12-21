package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class QuickEditVariantViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: QuickEditVariantViewModel
    protected lateinit var getProductVariantUseCase: GetProductVariantUseCase
    protected lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase
    protected lateinit var userSession: UserSessionInterface

    @Before
    fun setUp() {
        getProductVariantUseCase = mockk(relaxed = true)
        getProductManageAccessUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel = QuickEditVariantViewModel(
                getProductVariantUseCase,
                getProductManageAccessUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )
    }
 }