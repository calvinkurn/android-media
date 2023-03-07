package com.tokopedia.topads.sdk.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsImageViewUseCaseTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository = mockk<TopAdsRepository>(relaxed = true)
    private val irisSessionId = "1234"
    private val userId = "001"

    private val useCase =
        spyk(TopAdsImageViewUseCase(userId, repository, irisSessionId))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test getImageData`() {
        val map = mutableMapOf<String, Any>()
        runBlockingTest {
            coEvery { repository.getImageData(map, irisSessionId) } returns mockk()
            useCase.getImageData(map)
            coVerify { repository.getImageData(map, irisSessionId) }
        }
    }

    @Test
    fun `test getQueryMap when some values are empty`() {
        val queryMap = useCase.getQueryMap("", "source", "", 2, 3, "", "", "")
        assertEquals(queryMap["user_id"], userId)
        assertEquals( queryMap["ep"], "banner")
        assertEquals(queryMap["inventory_id"], "source")
        assertEquals(queryMap["device"], "android")
        assertEquals(queryMap["item"], 2)
        assertEquals( queryMap["dimen_id"], 3)
    }

    @Test
    fun `test getQueryMap when  values are not empty`() {

        val queryMap = useCase.getQueryMap("query", "source", "page_token", 2, 3, "dep_01", "prod_02", "0")
        assertEquals(queryMap["user_id"], userId)
        assertEquals( queryMap["ep"], "banner")
        assertEquals(queryMap["inventory_id"], "source")
        assertEquals(queryMap["device"], "android")
        assertEquals(queryMap["item"], 2)
        assertEquals( queryMap["dimen_id"], 3)
        assertEquals(queryMap["page"], "0")
        assertEquals(queryMap["q"], "query")
        assertEquals( queryMap["page_token"], "page_token")
        assertEquals(queryMap["dep_id"], "dep_01")
        assertEquals(queryMap["product_id"], "prod_02")

    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}