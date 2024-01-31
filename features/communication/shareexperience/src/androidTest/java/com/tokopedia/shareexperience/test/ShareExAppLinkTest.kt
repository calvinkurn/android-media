package com.tokopedia.shareexperience.test

import com.tokopedia.shareexperience.test.base.ShareExBaseTest
import org.junit.Test

class ShareExAppLinkTest : ShareExBaseTest() {
    @Test
    fun should_open_affiliate_registration() {
        // When
        launchActivity()
        stubAllIntents()

        // Then
        Thread.sleep(10000)
    }

    // TODO: check wa intent
    // TODO: check fb feed
    // TODO: check fb story
    // TODO: check ig feed
    // TODO: check ig story
    // TODO: check ig dm
    // TODO: check line
    // TODO: check twitter
    // TODO: check telegram
}
