package com.tokopedia.media.loader

import android.os.Build
import com.tokopedia.media.loader.utils.RemoteCdnService
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 *
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RemoteCDNServiceTest {

    @Test
    fun `given the malformed_image_url when isValidUrl should return true`() {
        val malformedImageUrl = "https://images.tokopedia.net/img/https:/images.tokopedia.net/img/android/digital/common_topup_bills/common_topup_ic_illustration_not_found.png"
        val result = RemoteCdnService.isValidUrl(malformedImageUrl)
        assertEquals(true, result)
    }

    @Test
    fun `given the local storage url when isValidUrl should return true`() {
        val wrongImageUrl = "/storage/emulated/0/Android/data/com.tokopedia.tkpd/cache/Tokopedia/373287.jpg"
        val result = RemoteCdnService.isValidUrl(wrongImageUrl)
        assertEquals(false, result)
    }

    @Test
    fun `given the correct image url when isValidUrl should return true`() {
        val correctImageUrl = "https://images.tokopedia.net/img/android/digital/common_topup_bills/common_topup_ic_illustration_not_found.png"
        val result = RemoteCdnService.isValidUrl(correctImageUrl)
        assertEquals(true, result)
    }
}
