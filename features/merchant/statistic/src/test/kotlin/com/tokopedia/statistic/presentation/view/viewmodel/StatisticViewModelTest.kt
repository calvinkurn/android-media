package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

/**
 * Created By @ilhamsuaib on 20/07/20
 */

@ExperimentalCoroutinesApi
class StatisticViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getLayoutUseCase: GetLayoutUseCase

    @RelaxedMockK
    lateinit var getCardDataUseCase: GetCardDataUseCase

    @RelaxedMockK
    lateinit var getLineGraphDataUseCase: GetLineGraphDataUseCase

    @RelaxedMockK
    lateinit var getProgressDataUseCase: GetProgressDataUseCase

    @RelaxedMockK
    lateinit var getPostDataUseCase: GetPostDataUseCase

    @RelaxedMockK
    lateinit var getCarouselDataUseCase: GetCarouselDataUseCase

    @RelaxedMockK
    lateinit var getTableDataUseCase: GetTableDataUseCase

    @RelaxedMockK
    lateinit var getPieChartDataUseCase: GetPieChartDataUseCase

    @RelaxedMockK
    lateinit var getBarChartDataUseCase: GetBarChartDataUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var statisticViewModel: StatisticViewModel? = null

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        statisticViewModel = StatisticViewModel(userSession, getLayoutUseCase, getCardDataUseCase, getLineGraphDataUseCase,
                getProgressDataUseCase, getPostDataUseCase, getCarouselDataUseCase, getTableDataUseCase,
                getPieChartDataUseCase, getBarChartDataUseCase, Dispatchers.Unconfined)
    }
}