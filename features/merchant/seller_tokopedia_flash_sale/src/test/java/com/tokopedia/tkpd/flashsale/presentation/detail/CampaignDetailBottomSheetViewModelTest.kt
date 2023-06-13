package com.tokopedia.tkpd.flashsale.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.flashsale.presentation.detail.mapper.CampaignDetailUIMapper
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.viewmodel.CampaignDetailBottomSheetViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CampaignDetailBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    internal lateinit var campaignDetailUIMapper: CampaignDetailUIMapper
    internal lateinit var viewModel: CampaignDetailBottomSheetViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = CampaignDetailBottomSheetViewModel (
            CoroutineTestDispatchersProvider,
            campaignDetailUIMapper
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When setCampaignDetailData, Expect trigger`() {
        with(viewModel) {
            setCampaignDetailData(CampaignDetailBottomSheetModel())
            campaignDetail.getOrAwaitValue()
            fragmentList.getOrAwaitValue()
            showTab.getOrAwaitValue()
        }
    }

}
