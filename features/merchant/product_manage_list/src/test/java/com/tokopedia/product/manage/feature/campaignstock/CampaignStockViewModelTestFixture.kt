package com.tokopedia.product.manage.feature.campaignstock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStatusUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class CampaignStockViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var campaignStockAllocationUseCase: CampaignStockAllocationUseCase

    @RelaxedMockK
    lateinit var otherCampaignStockDataUseCase: OtherCampaignStockDataUseCase

    @RelaxedMockK
    lateinit var getProductVariantUseCase: GetProductVariantUseCase

    @RelaxedMockK
    lateinit var editStatusUseCase: EditStatusUseCase

    @RelaxedMockK
    lateinit var editStockUseCase: UpdateProductStockWarehouseUseCase

    @RelaxedMockK
    lateinit var editProductVariantUseCase: EditProductVariantUseCase

    @RelaxedMockK
    lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase

    @RelaxedMockK
    lateinit var getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getStockAllocationLiveDataObserver: Observer<in Result<StockAllocationResult>>

    protected lateinit var viewModel: CampaignStockViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignStockViewModel(
            campaignStockAllocationUseCase,
            otherCampaignStockDataUseCase,
            getProductVariantUseCase,
            editStatusUseCase,
            editStockUseCase,
            editProductVariantUseCase,
            getProductManageAccessUseCase,
            getAdminInfoShopLocationUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        ).also {
            it.getStockAllocationData.observeForever(getStockAllocationLiveDataObserver)
        }

        val locationList = listOf(
            ShopLocationResponse("1", MAIN_LOCATION),
            ShopLocationResponse("2", OTHER_LOCATION)
        )
        onGetWarehouseId_thenReturn(locationList)
        onGetIsShopOwner_thenReturn(true)
    }

    @After
    fun cleanup() {
        viewModel.getStockAllocationData.removeObserver(getStockAllocationLiveDataObserver)
    }

    protected fun onGetIsShopOwner_thenReturn(isShopOwner: Boolean) {
        every { userSession.isShopOwner } returns isShopOwner
    }

    protected fun onGetWarehouseId_thenReturn(locationList: List<ShopLocationResponse>) {
        coEvery {
            getAdminInfoShopLocationUseCase.execute(any())
        } returns locationList
    }

    protected companion object LocationType {
        const val MAIN_LOCATION = 1
        const val OTHER_LOCATION = 99
    }
}