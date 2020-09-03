package com.tokopedia.product.manage.feature.campaignstock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import com.tokopedia.product.manage.feature.quickedit.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.usecase.coroutines.Result
import io.mockk.MockKAnnotations
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
    lateinit var editStockUseCase: EditStockUseCase

    @RelaxedMockK
    lateinit var editProductVariantUseCase: EditProductVariantUseCase

    @RelaxedMockK
    lateinit var getStockAllocationLiveDataObserver: Observer<in Result<StockAllocationResult>>

    protected lateinit var viewModel: CampaignStockViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignStockViewModel(campaignStockAllocationUseCase, otherCampaignStockDataUseCase, getProductVariantUseCase, editStockUseCase, editProductVariantUseCase, TestCoroutineDispatchers).also {
            it.getStockAllocationData.observeForever(getStockAllocationLiveDataObserver)
        }
    }

    @After
    fun cleanup() {
        viewModel.getStockAllocationData.removeObserver(getStockAllocationLiveDataObserver)
    }
}