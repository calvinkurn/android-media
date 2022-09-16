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
        spyk<TopAdsImageViewUseCase>(TopAdsImageViewUseCase(userId, repository, irisSessionId))

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
        val actual = useCase.getQueryMap("", "", "", 2, 3, "", "", "")
        assertEquals(userId, actual["user_id"])
        assertEquals("banner", actual["ep"])
        assertEquals("android", actual["device"])
        assertEquals(2, actual["item"])
        assertEquals(3, actual["dimen_id"])

    }

    @Test
    fun `test getQueryMap when  values are not empty`() {

        val actual = useCase.getQueryMap("query", "", "page_token", 2, 3, "dep_01", "prod_02", "0")
        assertEquals("0", actual["page"])
        assertEquals("query", actual["q"])
        assertEquals("page_token", actual["page_token"])
        assertEquals("dep_01", actual["dep_id"])
        assertEquals("prod_02", actual["product_id"])

    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}