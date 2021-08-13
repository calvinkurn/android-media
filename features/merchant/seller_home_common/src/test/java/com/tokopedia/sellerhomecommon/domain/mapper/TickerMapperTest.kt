package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.sellerhomecommon.domain.model.GetTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.utils.TestHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created By @ilhamsuaib on 04/09/20
 */

@RunWith(JUnit4::class)
class TickerMapperTest {

    private lateinit var mapper: TickerMapper

    @Before
    fun setup() {
        mapper = TickerMapper()
    }

    @Test
    fun `given remote model ticker list then return ui model ticker list correctly`() {
        val jsonPath = "json/get_tickers_success_response.json"
        val gqlResponse = TestHelper.createSuccessResponse<GetTickerResponse>(jsonPath)
        val tickerData = gqlResponse.getSuccessData<GetTickerResponse>()
        val isFromCache = false

        val expectedTickers = listOf(TickerItemUiModel(
                id = "253",
                title = "ticker seller",
                type = 1,
                message = "Bantu kami menjadi lebih baik dengan membagikan pengalamanmu <a href=\"https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU\">di sini</a><br>",
                color = "#cde4c3",
                redirectUrl = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU"
        ))

        val actualTickers = mapper.mapRemoteDataToUiData(tickerData, isFromCache)

        Assert.assertEquals(expectedTickers, actualTickers)
    }
}