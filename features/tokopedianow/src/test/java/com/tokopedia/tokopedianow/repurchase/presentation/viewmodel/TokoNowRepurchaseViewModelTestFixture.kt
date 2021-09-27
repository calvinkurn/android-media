package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.recentpurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel.TokoNowRecentPurchaseViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TokoNowRepurchaseViewModelTestFixture {

    @RelaxedMockK
    lateinit var getRepurchaseProductListUseCase: GetRepurchaseProductListUseCase
    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase
    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase
    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase
    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase
    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase
    @RelaxedMockK
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel : TokoNowRecentPurchaseViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowRecentPurchaseViewModel(
                getRepurchaseProductListUseCase,
                getMiniCartUseCase,
                getCategoryListUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getRecommendationUseCase,
                getChooseAddressWarehouseLocUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )
    }
}