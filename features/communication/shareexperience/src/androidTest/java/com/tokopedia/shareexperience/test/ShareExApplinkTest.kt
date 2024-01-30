package com.tokopedia.shareexperience.test

import com.tokopedia.shareexperience.test.base.ShareExBaseTest
import org.junit.Test

class ShareExApplinkTest: ShareExBaseTest() {
    @Test
    fun should_open_chat_buyer() {
        // When
        launchActivity()
        stubAllIntents()

        // Then
        Thread.sleep(10000)
    }
}
