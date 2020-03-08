package com.tokopedia.salam.umrah.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.salam.umrah.common.usecase.UmrahTravelAgentsUseCase
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageModel
import com.tokopedia.salam.umrah.homepage.presentation.usecase.*
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahHomepageViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahHomepageEmptyDataUseCase: UmrahHomepageEmptyDataUseCase
    @RelaxedMockK
    lateinit var umrahCategoriesFeaturedUseCase: UmrahHomepageCategoryFeaturedUseCase
    @RelaxedMockK
    lateinit var umrahCategoriesUseCase: UmrahHomepageCategoryUseCase
    @RelaxedMockK
    lateinit var umrahSearchParamUseCase: UmrahSearchParameterUseCase
    @RelaxedMockK
    lateinit var umrahHomepageMyUmrahUseCase: UmrahHomepageMyUmrahUseCase
    @RelaxedMockK
    lateinit var umrahHomepageBannerUseCase: UmrahHomepageBannerUseCase
    @RelaxedMockK
    lateinit var umrahTravelAgentsUseCase: UmrahTravelAgentsUseCase

    private val dispatcher = UmrahDispatchersProviderTest()
    private lateinit var umrahHomepageViewModel: UmrahHomepageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        umrahHomepageViewModel = UmrahHomepageViewModel(umrahHomepageEmptyDataUseCase,
                umrahCategoriesFeaturedUseCase,
                umrahCategoriesUseCase,
                umrahSearchParamUseCase,
                umrahHomepageMyUmrahUseCase,
                umrahHomepageBannerUseCase,
                umrahTravelAgentsUseCase,
                dispatcher)
    }

    @Test
    fun `responseUmrahData_EmptyHomepageData_ShowEmptyHomepage`(){
        //given
        coEvery {
            umrahHomepageEmptyDataUseCase.requestEmptyViewModels(true)
        } returns listOf()

        umrahHomepageViewModel.getIntialList(true)

        val actual = umrahHomepageViewModel.homePageModel.value
        val isError = umrahHomepageViewModel.isError.value
        assert(actual == listOf<UmrahHomepageModel>())
        assert(isError==false)

    }
}