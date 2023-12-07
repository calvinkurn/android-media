package com.tokopedia.media.loader

import android.os.Build
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.media.loader.data.HEADER_FMT
import com.tokopedia.media.loader.data.HEADER_KEY_AUTH
import com.tokopedia.media.loader.data.HEADER_USER_ID
import com.tokopedia.media.loader.data.HEADER_X_DEVICE
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.fake.FakeFeatureToggleManager
import com.tokopedia.media.loader.utils.generateUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class WebpExtensionTest {

    private var fakeFeatureToggleManager = FakeFeatureToggleManager()

    @Test
    fun `generate normal image url from data properties`() {
        // Given
        val url = "https://ect7.tokopedia.net/img/cache/900/VqbcmM/2023/5/23/bcb24bbc-ffd6-4f6b-9b85-32ce7458115f.jpg"

        // When
        val properties = setupProperties(url)

        // Then
        val result = properties.generateUrl()
        assertEquals(url, result)
        assertTrue(result !is GlideUrl)
    }

    @Test
    fun `generate url and send the webp header during request image url if the image is jpg`() {
        // Given
        val url = "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/23/bcb24bbc-ffd6-4f6b-9b85-32ce7458115f.jpg"
        val expectedMimeType = "image/webp"

        // When
        val properties = setupProperties(url)

        // Then
        val result = properties.generateUrl() as GlideUrl
        assertEquals(expectedMimeType, result.headers[HEADER_FMT])
    }

    @Test
    fun `generate url and avoid sending the webp header during image url if the image is webp`() {
        // Given
        val url = "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/23/bcb24bbc-ffd6-4f6b-9b85-32ce7458115f.jpg.webp"

        // When
        val properties = setupProperties(url)

        // Then
        val result = properties.generateUrl()
        assertEquals(url, result)
        assertTrue(result !is GlideUrl)
    }

    @Test
    fun `generate secure image url with send the data of user sessions`() {
        // Given
        val url = "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/23/bcb24bbc-ffd6-4f6b-9b85-32ce7458115f.jpg"
        val mockUserId = "123"

        // When
        val properties = setupProperties(url) {
            userId(mockUserId)
            isSecure(true)
        }

        // Then
        val result = properties.generateUrl() as GlideUrl
        assertEquals(mockUserId, result.headers[HEADER_USER_ID])
        assertTrue(result.headers.containsKey(HEADER_KEY_AUTH))
        assertTrue(result.headers.containsKey(HEADER_X_DEVICE))
    }

    private inline fun setupProperties(
        url: String,
        crossinline properties: Properties.() -> Unit = {}
    ): Properties {
        return Properties()
            .apply(properties)
            .also {
                it.featureToggle = fakeFeatureToggleManager
                it.data = url
            }
    }
}
