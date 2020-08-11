package com.tokopedia.shop_showcase.viewmodel.shopshowcaseadd

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.shop_showcase.coroutine.TestCoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.AppendShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.RemoveShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.UpdateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel.ShopShowcaseAddViewModel
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class ShopShowCaseAddViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var createShopShowcaseUseCase: CreateShopShowcaseUseCase

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var updateShopShowcaseUseCase: UpdateShopShowcaseUseCase

    @RelaxedMockK
    lateinit var appendShopShowcaseProductUseCase: AppendShopShowcaseProductUseCase

    @RelaxedMockK
    lateinit var removeShopShowcaseProductUseCase: RemoveShopShowcaseProductUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    protected lateinit var shopShowCaseAddViewModel: ShopShowcaseAddViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopShowCaseAddViewModel = ShopShowcaseAddViewModel(
                createShopShowcaseUseCase,
                getProductListUseCase,
                updateShopShowcaseUseCase,
                appendShopShowcaseProductUseCase,
                removeShopShowcaseProductUseCase,
                userSession,
                TestCoroutineDispatchers.main()
        )
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        TestCase.assertEquals(expected, actual)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }

}