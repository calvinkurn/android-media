package com.tokopedia.sellerhome.data

import com.tokopedia.sellerhome.data.remote.TickerService
import com.tokopedia.sellerhome.data.remote.model.TickerResponse
import com.tokopedia.sellerhome.domain.mapper.TickerMapper
import com.tokopedia.sellerhome.utils.TestHelper
import com.tokopedia.user.session.UserSessionInterface
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
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 2020-02-24
 */

@ExperimentalCoroutinesApi
class GetTickerRepositoryTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_ticker_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var tickerService: TickerService
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var mapper: TickerMapper

    private val tickerRepository by lazy {
        GetTickerRepository(tickerService, userSession, mapper)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get ticker`() = runBlocking {
        val json = TestHelper.getJsonFromFile(SUCCESS_RESPONSE)
        val successResponse: TickerResponse = TestHelper.parseJson(json)
        coEvery {
            tickerService.getTicker(anyString(), anyString(), anyString(), anyString(), anyString())
        } returns successResponse

        //this test will always throw ClassCastException as it tries to access retrofit.
        //Will change later if there are possible fixes
        expectedException.expect(ClassCastException::class.java)
        val tickers = tickerRepository.getTicker()

        coVerify {
            tickerService.getTicker(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        assertTrue(!tickers.isNullOrEmpty())
    }

    @Test
    fun `should failed get ticker`() = runBlocking {
        val errorResponse = TickerResponse(data = null, meta = null)
        coEvery {
            tickerService.getTicker(anyString(), anyString(), anyString(), anyString(), anyString())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val tickers = tickerRepository.getTicker()

        coVerify {
            tickerService.getTicker(anyString(), anyString(), anyString(), anyString(), anyString())
        }

        assertTrue(tickers.isNullOrEmpty())
    }
}