package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class TokoNowCategoryMainViewModelTestFixture {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    protected lateinit var viewModel: TokoNowCategoryMainViewModel

    @RelaxedMockK
    lateinit var getCategoryDetailUseCase: GetCategoryDetailUseCase

    @RelaxedMockK
    lateinit var getCategoryProductUseCase: GetCategoryProductUseCase

    @RelaxedMockK
    lateinit var localAddress: TokoNowLocalAddress

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase:DeleteCartUseCase

    @RelaxedMockK
    lateinit var affiliateService: NowAffiliateService

    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val categoryIdL1: String = "123"

    @Before
    fun setUp() {
        viewModel = TokoNowCategoryMainViewModel(
            getCategoryDetailUseCase = getCategoryDetailUseCase,
            getCategoryProductUseCase = getCategoryProductUseCase,
            categoryIdL1 = categoryIdL1,
            addressData = localAddress,
            userSession = userSession,
            getMiniCartUseCase = getMiniCartListSimplifiedUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            affiliateService = affiliateService,
            getTargetedTickerUseCase = getTargetedTickerUseCase,
            dispatchers = CoroutineTestDispatchersProvider
        )
    }

//    protected fun onGetCategoryList_thenReturn(response: CategoryListResponse) {
//        coEvery { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) } returns response
//    }
//
//    protected fun onGetCategoryList_thenReturn(error: Throwable) {
//        coEvery { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) } throws error
//    }
//
//    protected fun verifyGetCategoryListCalled() {
//        coVerify { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) }
//    }
}
