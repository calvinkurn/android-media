package com.tokopedia.product.manage.feature.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

class ProductManageFilterViewModelTest {

    @RelaxedMockK
    lateinit var getProductListMetaUseCase: GetProductListMetaUseCase

    @RelaxedMockK
    lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase

    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ProductManageFilterViewModel(getProductListMetaUseCase, getShopEtalaseByShopUseCase, getCategoryListUseCase, userSession, dispatchers)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getProductListData should execute expected use case`() {
        mockkObject(getProductListMetaUseCase)
        coEvery {
            getProductListMetaUseCase.executeOnBackground()
        }
        viewModel.getProductListData(0)
        coVerify {
            getProductListMetaUseCase.executeOnBackground()
        }
    }

    @Test
    fun `getShopetalaseData should execute expected use case`() {
        val mockedSubscriber = mockk<Subscriber<ArrayList<ShopEtalaseModel>>>()
        val mockedRequestParams = mockk<RequestParams>()
        mockkObject(getProductListMetaUseCase)
        coEvery {
            getShopEtalaseByShopUseCase.execute(mockedRequestParams, mockedSubscriber)
        }
        viewModel.getShopEtalase(0)
        coVerify {
            getShopEtalaseByShopUseCase.execute(mockedRequestParams, mockedSubscriber)
        }
    }

    @Test
    fun `getCategoryList should execute expected use case`() {
        mockkObject(getProductListMetaUseCase)
        coEvery {
            getCategoryListUseCase.executeOnBackground()
        }
        viewModel.getCategories()
        coVerify {
            getCategoryListUseCase.executeOnBackground()
        }
    }
}