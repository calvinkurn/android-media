package com.tokopedia.hotlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_category.usecase.SendTopAdsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HotlistNavViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var trackUrl: String = "testUrl"
    private var trackId: String = "testId"
    private var trackName: String = "testName"
    private var trackImage: String = "testImage"
    private var sendTopAdsUseCase: SendTopAdsUseCase = mockk(relaxed = true)
    private var hotlistNavViewModel: HotlistNavViewModel = spyk(HotlistNavViewModel(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            sendTopAdsUseCase
    ))

    @Test
    fun testSendTopAds() {
        var url = ""
        var id = ""
        var name = ""
        var image = ""
        val slotUrl = slot<String>()
        val slotId = slot<String>()
        val slotName = slot<String>()
        val slotImage = slot<String>()
        every { sendTopAdsUseCase.hitImpressions(capture(slotUrl), capture(slotId), capture(slotName), capture(slotImage)) } answers {
            url = slotUrl.captured
            id = slotId.captured
            name = slotName.captured
            image = slotImage.captured
        }

        hotlistNavViewModel.sendTopAdsImpressions(trackUrl, trackId,trackName,trackImage)

        assertEquals(trackUrl, url)
        assertEquals(trackId, id)
        assertEquals(trackName, name)
        assertEquals(trackImage, image)
    }
}