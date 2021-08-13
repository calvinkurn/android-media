package com.tokopedia.product.manage.feature.campaignstock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignMainStockViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class CampaignMainStockViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var shouldDisplayVariantStockWarningObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var showStockInfoObserver: Observer<in Boolean>

    protected lateinit var viewModel: CampaignMainStockViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignMainStockViewModel(CoroutineTestDispatchersProvider).also {
            it.shouldDisplayVariantStockWarningLiveData.observeForever(shouldDisplayVariantStockWarningObserver)
            it.showStockInfo.observeForever(showStockInfoObserver)
        }
    }

    @After
    fun cleanup() {
        viewModel.shouldDisplayVariantStockWarningLiveData.removeObserver(shouldDisplayVariantStockWarningObserver)
        viewModel.showStockInfo.removeObserver(showStockInfoObserver)
    }

    protected fun verifyShowStockInfo() {
        viewModel.showStockInfo
            .verifyValueEquals(true)
    }

    protected fun verifyHideStockInfo() {
        viewModel.showStockInfo
            .verifyValueEquals(false)
    }

    protected fun verifyShowVariantStockWarning() {
        viewModel.shouldDisplayVariantStockWarningLiveData
            .verifyValueEquals(true)
    }

    protected fun verifyHideVariantStockWarning() {
        viewModel.shouldDisplayVariantStockWarningLiveData
            .verifyValueEquals(false)
    }
}