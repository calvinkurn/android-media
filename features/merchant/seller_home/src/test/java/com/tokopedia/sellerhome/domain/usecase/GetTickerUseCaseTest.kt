package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.sellerhome.data.GetTickerRepository
import com.tokopedia.sellerhome.view.model.TickerUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * Created By @ilhamsuaib on 2020-02-24
 */

@ExperimentalCoroutinesApi
class GetTickerUseCaseTest {

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var tickerRepo: GetTickerRepository

    private val getTickerUseCase by lazy {
        GetTickerUseCase(tickerRepo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get ticker`() = runBlocking{
        coEvery {
            tickerRepo.getTicker()
        } returns getDummyTicker()

        val result = getTickerUseCase.executeOnBackground()

        coVerify {
            tickerRepo.getTicker()
        }

        assertTrue(!result.isNullOrEmpty())
    }

    private fun getDummyTicker(): List<TickerUiModel> {
        return listOf(
                TickerUiModel("", "", "", "", "", "",
                        "", "", "", "", "", "", "")
        )
    }
}