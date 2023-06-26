package com.tokopedia.tokochat.test

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.cache.TokoChatBubblesCache
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatBubbleViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when cache result is null, should show bottom sheet bubble awareness`() {
        runBlocking {
            // given
            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns null

            // when
            val result = viewModel.shouldShowBottomsheetBubblesCache()

            // then
            Assert.assertEquals(true, result)
        }
    }

    @Test
    fun `when cache result is not null, should show bottom sheet bubble awareness`() {
        runBlocking {
            // given
            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns TokoChatBubblesCache()

            // when
            val result = viewModel.shouldShowBottomsheetBubblesCache()

            // then
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `when has shown ticker is false, should show ticker bubble awareness`() {
        runBlocking {
            // given
            viewModel.channelId = CHANNEL_ID_DUMMY
            val expectedResult = TokoChatBubblesCache(
                channelId = CHANNEL_ID_DUMMY,
                hasShownTicker = false
            )

            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns expectedResult

            // when
            val result = viewModel.shouldShowTickerBubblesCache()

            // then
            Assert.assertEquals(true, result)
        }
    }

    @Test
    fun `when has shown ticker is true, should not show ticker bubble awareness`() {
        runBlocking {
            // given
            viewModel.channelId = CHANNEL_ID_DUMMY
            val expectedResult = TokoChatBubblesCache(
                channelId = CHANNEL_ID_DUMMY,
                hasShownTicker = true
            )

            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns expectedResult

            // when
            val result = viewModel.shouldShowTickerBubblesCache()

            // then
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `when has shown ticker is false and channel id is not same, should not show ticker bubble awareness`() {
        runBlocking {
            // given
            viewModel.channelId = CHANNEL_ID_DUMMY
            val expectedResult = TokoChatBubblesCache(
                channelId = "test",
                hasShownTicker = false
            )

            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns expectedResult

            // when
            val result = viewModel.shouldShowTickerBubblesCache()

            // then
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `when has shown ticker is true and channel id is not same, should not show ticker bubble awareness`() {
        runBlocking {
            // given
            viewModel.channelId = CHANNEL_ID_DUMMY
            val expectedResult = TokoChatBubblesCache(
                channelId = "test",
                hasShownTicker = true
            )

            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns expectedResult

            // when
            val result = viewModel.shouldShowTickerBubblesCache()

            // then
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `when cache is null, should not show ticker bubble awareness`() {
        runBlocking {
            // given
            viewModel.channelId = CHANNEL_ID_DUMMY

            every {
                cacheManager.loadCache(any(), TokoChatBubblesCache::class.java)
            } returns null

            // when
            val result = viewModel.shouldShowTickerBubblesCache()

            // then
            Assert.assertEquals(false, result)
        }
    }

    @Test
    fun `when user save cache, should call cacheManager saveCache`() {
        runBlocking {
            // given
            every {
                cacheManager.saveCache(any(), any())
            } returns Unit

            // when
            viewModel.setBubblesPref(hasShownTicker = false)

            // then
            verify(exactly = 1) {
                cacheManager.saveCache(any(), any())
            }
        }
    }

    @Test
    fun `should give correct value from isFromBubble`() {
        runBlocking {
            // when
            viewModel.isFromBubble = true

            // then
            Assert.assertTrue(viewModel.isFromBubble)
        }
    }
}
