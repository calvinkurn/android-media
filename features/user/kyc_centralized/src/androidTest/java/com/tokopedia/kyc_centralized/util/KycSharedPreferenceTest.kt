package com.tokopedia.kyc_centralized.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel.Companion.KYC_IV_KTP_CACHE
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class KycSharedPreferenceTest {

    private val ctx = ApplicationProvider.getApplicationContext<Context>()
    private val pref = ctx.getSharedPreferences("kyc_centralized", Context.MODE_PRIVATE)
    private val cipherProvider = CipherProviderImpl()
    lateinit var sut: KycSharedPreferenceImpl

    @Before
    fun setup() {
        sut = KycSharedPreferenceImpl(pref)
    }

    @Test
    fun whenSavingByteArray_ThenShouldReturnTheSameByteArray() {
        val iv = cipherProvider.initAesEncrypt().iv
        sut.saveByteArrayCache(KYC_IV_KTP_CACHE, iv)

        val actual = sut.getByteArrayCache(KYC_IV_KTP_CACHE)

        assertThat(actual, `is`(iv))
    }

    @Test
    fun whenRetrievingNullKey_ShouldReturnEmptyArray() {
        val actual = sut.getByteArrayCache("shouldNullKey")

        assertEquals(actual?.size, 0)
    }

    @Test
    fun whenSavedAndRetrievedArray_ShouldReturnTheSameCipher() {
        val firstCipher = cipherProvider.initAesEncrypt()

        sut.saveByteArrayCache(KYC_IV_KTP_CACHE, firstCipher.iv)
        Thread.sleep(1_000)
        val savedIv = sut.getByteArrayCache(KYC_IV_KTP_CACHE)
        val actual = cipherProvider.initAesDecrypt(savedIv)

        // different object for both cipher and IV
        // same iv value
        assertTrue(firstCipher.iv contentEquals actual.iv)
    }

}