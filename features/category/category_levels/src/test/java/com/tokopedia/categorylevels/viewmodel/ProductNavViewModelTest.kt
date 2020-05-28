package com.tokopedia.categorylevels.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.categorylevels.domain.usecase.SubCategoryV3UseCase
import com.tokopedia.common_category.usecase.DynamicFilterUseCase
import com.tokopedia.common_category.usecase.GetProductListUseCase
import com.tokopedia.common_category.usecase.QuickFilterUseCase
import com.tokopedia.common_category.usecase.SendTopAdsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProductNavViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var trackUrl: String = "test"
    private var subCategoryV3UseCase: SubCategoryV3UseCase = mockk()
    private var dynamicFilterUseCase: DynamicFilterUseCase = mockk()
    private var quickFilterUseCase: QuickFilterUseCase = mockk()
    private var getProductListUseCase: GetProductListUseCase = mockk()
    private var sendTopAdsUseCase: SendTopAdsUseCase = mockk(relaxed = true)
    private var productNavViewModel: ProductNavViewModel = spyk(ProductNavViewModel(
            subCategoryV3UseCase,
            dynamicFilterUseCase,
            quickFilterUseCase,
            getProductListUseCase,
            sendTopAdsUseCase
    ))

    @Test
    fun testSendTopAds() {
        var url = ""
        val slotUrl = slot<String>()
        every { sendTopAdsUseCase.executeOnBackground(capture(slotUrl)) } answers {
            url = slotUrl.captured
        }

        productNavViewModel.sendTopAds(trackUrl)

        Assert.assertEquals(trackUrl, url)
    }
}