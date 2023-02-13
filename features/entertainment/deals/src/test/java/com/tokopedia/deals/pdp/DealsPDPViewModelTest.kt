package com.tokopedia.deals.pdp

import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.data.DealsRatingResponse
import com.tokopedia.deals.pdp.data.DealsRatingUpdateResponse
import com.tokopedia.deals.pdp.data.DealsTrackingResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class DealsPDPViewModelTest: DealsPDPViewModelTestFixture() {

    @Test
    fun `when getting deals pdp data should run and give the success result`() {
        onGetPDPDetail_thenReturn(createPDPData())
        val expectedResponse = createPDPData()
        var actualResponse: Result<DealsProductDetail>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowPDP.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setPDP("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting deals pdp data should run and give the error result`() {
        onGetPDPDetail_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsProductDetail>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowPDP.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setPDP("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting deals event content by id data should run and give the success result`() {
        onGetContentById_thenReturn(createContentById())
        val expectedResponse = createContentById()
        var actualResponse: Result<DealsProductEventContent>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowContent.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setContent("12344")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting deals event content by id data should run and give the empty result`() {
        onGetContentById_thenReturn(createContentByIdEmpty())
        val expectedResponse = createContentByIdEmpty()
        var actualResponse: Result<DealsProductEventContent>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowContent.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setContent("12344")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
        Assert.assertTrue((actualResponse as Success).data.eventContentById.data.sectionDatas.first().contents.first().valueText.isEmpty())
    }

    @Test
    fun `when getting deals event content by id pdp data should run and give the error result`() {
        onGetContentById_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsProductEventContent>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowContent.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setContent("12344")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting deals recommendation data should run and give the success result`() {
        onGetRecommendation_thenReturn(createRecommendation())
        val expectedResponse = createRecommendation()
        var actualResponse: Result<SearchData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecommendation.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setRecommendation("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting deals recommendation data should run and give the error result`() {
        onGetRecommendation_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<SearchData>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecommendation.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setRecommendation("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting rating data should run and give the success result`() {
        onGetRating_thenReturn(createRating())
        val expectedResponse = createRating()
        var actualResponse: Result<DealsRatingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setRating("")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting rating data should run and give the null result`() {
        onGetRating_thenReturn()
        var actualResponse: Result<DealsRatingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setRating("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting rating data should run and give the error result`() {
        onGetRating_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsRatingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setRating("")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting rating update data should run and give the success result`() {
        onGetRatingUpdate_thenReturn(createRatingUpdate())
        val expectedResponse = createRatingUpdate()
        var actualResponse: Result<DealsRatingUpdateResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdateRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.updateRating("", "", false)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting rating update data should run and give the null result`() {
        onGetRatingUpdate_thenReturn()
        var actualResponse: Result<DealsRatingUpdateResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdateRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.updateRating("", "", false)
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting rating update data should run and give the error result`() {
        onGetRatingUpdate_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsRatingUpdateResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowUpdateRating.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.updateRating("", "", false)
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting tracking recommendation data should run and give the success result`() {
        onGetTrackingRecommendation_thenReturn(createTracking())
        val expectedResponse = createTracking()
        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecommendationTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecommendation("1", "1234")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting tracking recommendation data should run and give the null result`() {
        onGetTrackingRecommendation_thenReturn()
        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecommendationTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecommendation("1", "1234")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting tracking recommendation data should run and give the error result`() {
        onGetTrackingRecommendation_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecommendationTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecommendation("1", "1234")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting tracking recent search data should run and give the success result`() {
        onGetTrackingRecentSearch_thenReturn(createTracking())
        val expectedResponse = createTracking()
        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecentSearchTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecentSearch(createPDPData().eventProductDetail.productDetailData, "1234")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting tracking recent search data but media is empty should run and give the success result`() {
        onGetTrackingRecentSearch_thenReturn(createTracking())
        val expectedResponse = createTracking()
        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecentSearchTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecentSearch(createPDPEmptyMediaData().eventProductDetail.productDetailData, "1234")
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when getting tracking recent search data should run and give the null result`() {
        onGetTrackingRecentSearch_thenReturn()
        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecentSearchTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecentSearch(createPDPData().eventProductDetail.productDetailData, "1234")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
    }

    @Test
    fun `when getting tracking recent search data should run and give the error result`() {
        onGetTrackingRecentSearch_thenReturn(Throwable(errorMessageGeneral))

        var actualResponse: Result<DealsTrackingResponse>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowRecentSearchTracking.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setTrackingRecentSearch(createPDPData().eventProductDetail.productDetailData, "1234")
            collectorJob.cancel()
        }

        Assert.assertTrue(actualResponse is Fail)
        Assert.assertEquals((actualResponse as Fail).throwable.message, errorMessageGeneral)
    }

    @Test
    fun `when getting likes return likes counter`() {
        val likeCounter = 10

        viewModel.totalLikes = likeCounter

        Assert.assertEquals(likeCounter, viewModel.totalLikes)
    }

    @Test
    fun `when getting isLiked return isLiked status`() {
        val isLiked = true

        viewModel.isLiked = isLiked

        Assert.assertEquals(isLiked, viewModel.isLiked)
    }
}
