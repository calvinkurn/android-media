package com.tokopedia.product.manage.feature.campaignstock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignMainStockViewModel
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

    protected lateinit var viewModel: CampaignMainStockViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignMainStockViewModel().also {
            it.shouldDisplayVariantStockWarningLiveData.observeForever(shouldDisplayVariantStockWarningObserver)
        }
    }

    @After
    fun cleanup() {
        viewModel.shouldDisplayVariantStockWarningLiveData.removeObserver(shouldDisplayVariantStockWarningObserver)
    }
}