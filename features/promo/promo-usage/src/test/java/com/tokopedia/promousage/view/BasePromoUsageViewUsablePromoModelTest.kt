package com.tokopedia.promousage.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.domain.usecase.PromoUsageClearCacheAutoApplyStackUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageValidateUseUseCase
import com.tokopedia.promousage.util.logger.PromoUsageLogger
import com.tokopedia.promousage.view.mapper.PromoUsageClearCacheAutoApplyStackMapper
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
import com.tokopedia.promousage.view.mapper.PromoUsageValidateUseMapper
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import org.junit.Before
import org.junit.Rule

open class BasePromoUsageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchers

    lateinit var viewModel: PromoUsageViewModel

    @MockK(relaxed = true)
    lateinit var getPromoListRecommendationUseCase: PromoUsageGetPromoListRecommendationUseCase

    @MockK(relaxed = true)
    lateinit var validateUseUseCase: PromoUsageValidateUseUseCase

    @MockK(relaxed = true)
    lateinit var clearCacheAutoApplyStackUseCase: PromoUsageClearCacheAutoApplyStackUseCase

    var getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper =
        PromoUsageGetPromoListRecommendationMapper()

    var validateUseMapper: PromoUsageValidateUseMapper = PromoUsageValidateUseMapper()

    @MockK(relaxed = true)
    lateinit var clearCacheAutoApplyStackMapper: PromoUsageClearCacheAutoApplyStackMapper

    @MockK(relaxed = true)
    lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PromoUsageViewModel(
            dispatchers,
            getPromoListRecommendationUseCase,
            validateUseUseCase,
            clearCacheAutoApplyStackUseCase,
            getPromoListRecommendationMapper,
            validateUseMapper,
            clearCacheAutoApplyStackMapper,
            chosenAddressRequestHelper
        )

        mockkObject(PromoUsageLogger)
        every {
            PromoUsageLogger.logOnErrorLoadPromoUsagePage(any())
        } just runs
        every {
            PromoUsageLogger.logOnErrorApplyPromo(any())
        } just runs
    }
}
