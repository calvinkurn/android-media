package com.tokopedia.encryption

import android.os.Build
import android.util.Base64
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest= Config.NONE)
class LoggerEncryptionTest {

    var ENCRYPTION_KEY = String(
        charArrayOf(
            113.toChar(),
            40.toChar(),
            101.toChar(),
            35.toChar(),
            37.toChar(),
            71.toChar(),
            102.toChar(),
            64.toChar(),
            111.toChar(),
            105.toChar(),
            62.toChar(),
            108.toChar(),
            107.toChar(),
            66.toChar(),
            126.toChar(),
            104.toChar()
        )
    )

    lateinit var rsaEncrpytor: RSA
    
    val publicRsaKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoHZ7Cq/9VO0ynSSThX8cPf5uoK7vXs5l" +
        "2hN41Otg6ys6b9VB5MWhcZ2GYmq4MDwC/eF0Baby6aNZMZT61Fh88fMbW+glGLEd7YpCL0bhqd3r" +
        "n8Z4IelMlq3CrEVfFd+ZD2cGsGINM1RNCvpwHVTiQTWs8ygBVC7ynsJZrk+abWsCBCYnl1lFkMd9" +
        "uv0tQ06nR2dN1jcsmUkysNhhvB0zoJYF7gK9U0WEyKngCrlrw4iiKhy95AVyVejRIO3MB2pD6Ioz" +
        "m0hWZNPoWs1EgDGeC2l6Xw5KjN/z6X8wIPLYMZqq8434ZFdxtJQZBQHa3OJaEX5kiohMqkeLWZnG" +
        "OtNpEwIDAQAB"

    val privateRsaKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgdnsKr/1U7TKdJJOFfxw9/m6g" +
        "ru9ezmXaE3jU62DrKzpv1UHkxaFxnYZiargwPAL94XQFpvLpo1kxlPrUWHzx8xtb6CUYsR3tikIv" +
        "RuGp3eufxngh6UyWrcKsRV8V35kPZwawYg0zVE0K+nAdVOJBNazzKAFULvKewlmuT5ptawIEJieX" +
        "WUWQx326/S1DTqdHZ03WNyyZSTKw2GG8HTOglgXuAr1TRYTIqeAKuWvDiKIqHL3kBXJV6NEg7cwH" +
        "akPoijObSFZk0+hazUSAMZ4LaXpfDkqM3/PpfzAg8tgxmqrzjfhkV3G0lBkFAdrc4loRfmSKiEyq" +
        "R4tZmcY602kTAgMBAAECggEAGNqhLkOaIphm2Y47x1ar4jJRolPWQcIIZqHTem9G4MYeVAodHbk6" +
        "y2VR1lafVuFEuVw9Y067m1Kw0ww3WcKm/7bfCvpHHK76aruaisYY1/3GrEXKf74qHcCmQZaxJqsu" +
        "gwM7NwDI+KVbFZM64GAwd1dABZ2xrv9ilxKVX3l2+vF0XbWvRK/2zFhm8ZGFHszVB1OhTzV8Rp8M" +
        "vsa/HpREgwugEW7YI6zFTGeH6+G4ZFBWE9Toj5RAlu+8QrTJ3PgjaqJKCvCtD75kdKKvWkBWQuTF" +
        "ojtm1DVI5abujx4UN7ztsbRQEPpICrifCBniLAEbQ0Dmkmx0wTDdRLdRLNc0oQKBgQDPgH5aYNeu" +
        "6QE5ZrGKCR8jvsh9vWldx71tNo0M/tP3tVWDQbyx7XplobbW8qqf4oPgNNuRnMIzS5s48iMdfDyo" +
        "/UT6+BHy68gWjiAK1mpMP+sF1YWs5o4mimC0gvp4wSyakr9Gi/3XMHguAjT1x4RFkD53CCvLkCil" +
        "vCgOlXyZMQKBgQDF93xmYoh2CgX9D4LTNhnuNPOiWnRkxOcoEFx7kZi/dFCY8KUhtBnurSuRmFTm" +
        "GyfaVztDA4dVdJGOImhp2kprw89N8SWXsPIXRxZJ5+0vkQ9tUw8vEsjPForlFR1i2S/P5uXNH/0s" +
        "vKI93OxcjLKbq7CCuRYWe/SeNHEj24AVgwKBgQCKQfBuocY0OfZh28YB1lAUTiNifJk/Lj5ODaHv" +
        "/b8Ydo9ytJNY7oW2QAkGsYv0mkYKVs3R+OQAr6jOSbASDlyQsKiMnEvBU07vkGVTM0iCY7hiG8JE" +
        "0R34Gg2TWQ4w9W2V1J90DwTko9v/s+K7vOOqQhEM2LntmbErtPHvvbTWkQKBgA7RygQ4GBMOCQi6" +
        "ooaBJEvKtvpOXywz+UHsh5/QdGjQoj/ws8vkGZTebcaGasSW/9K+ePc5SmZBRawoMzzJVIzgqaIi" +
        "DThfzJ+leKurqvvGlisO7rINa+7ktx0TCZIexA+CL229mUxCCrsq0J7VAYHZxmrixB6GxPca4eVX" +
        "Ry1VAoGBALdU3Cxofdhj3YM1dEhmntyaPmFbPcqQC9qlPblRJoj8Z9aOMnHm7rYZ/SRfbSobUuSR" +
        "knxS3Z723sl71q6g6ykZaqwYlZBSjWIQGUYhl3f9E6x+pwqFk1AVzpxhRTI7GsR/M0uk+WdmNo+y" +
        "2YCzL1IygDeov3aWBJJDeCvAafql"

    
    @Before
    fun setup() {
        rsaEncrpytor = RSA()
    }

    @Test
    fun `AES encrypt`() {
        val newRelicApiKey = intArrayOf(78, 82, 73, 73, 45, 99, 84, 100, 57, 84, 52, 87, 50, 113, 120, 72, 103, 103, 70, 110, 121, 113, 56, 49, 98, 78, 115, 100, 86, 97, 65, 100, 85, 116, 120, 84, 51)
        val decodeApiKey =  decodeKey(newRelicApiKey)
        val userIdNr = intArrayOf(51, 48, 53, 57, 53, 50, 56)
        val decodeUserIdNr = decodeKey(userIdNr)

        println("userIdNr: $decodeUserIdNr")
        println("newrelicApikey: $decodeApiKey")

        println(publicRsaKeyStr)
        println(privateRsaKeyStr)

        val publicKeyRSA = rsaEncrpytor.stringToPublicKey(publicRsaKeyStr)
        val privateKeyRSA = rsaEncrpytor.stringToPrivateKey(privateRsaKeyStr)

        val encryptedKey = rsaEncrpytor.encrypt(decodeApiKey, publicKeyRSA, Constants.RSA_OAEP_ALGORITHM)

        println("encrpytedApiKey: $encryptedKey")

        val decryptKey = rsaEncrpytor.decrypt(encryptedKey, privateKeyRSA, Constants.RSA_OAEP_ALGORITHM)

        println("expected: $decryptKey")
        println("actual: $decodeApiKey")

        assertEquals(decryptKey, decodeApiKey)
    }

    private fun decodeKey(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }

    @Test
    fun `AES decrypt`() {

    }
}
