package com.tokopedia.encryption

import android.os.Build
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Constants
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest = Config.NONE)
class NewRelicKeyEncryptionTest {

    private lateinit var rsaEncrpytor: RSA

    private val PUBLIC_KEY_RSA_STR = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoHZ7Cq/9VO0ynSSThX8cPf5uoK7vXs5l" +
        "2hN41Otg6ys6b9VB5MWhcZ2GYmq4MDwC/eF0Baby6aNZMZT61Fh88fMbW+glGLEd7YpCL0bhqd3r" +
        "n8Z4IelMlq3CrEVfFd+ZD2cGsGINM1RNCvpwHVTiQTWs8ygBVC7ynsJZrk+abWsCBCYnl1lFkMd9" +
        "uv0tQ06nR2dN1jcsmUkysNhhvB0zoJYF7gK9U0WEyKngCrlrw4iiKhy95AVyVejRIO3MB2pD6Ioz" +
        "m0hWZNPoWs1EgDGeC2l6Xw5KjN/z6X8wIPLYMZqq8434ZFdxtJQZBQHa3OJaEX5kiohMqkeLWZnG" +
        "OtNpEwIDAQAB"

    private val PRIVATE_KEY_RSA_STR = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgdnsKr/1U7TKdJJOFfxw9/m6g" +
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
    fun `Encrypt New Relic Key (plain) using the RSA key`() {
        // new relic key sample
//        val newRelicIntArray = intArrayOf(78, 82, 73, 73, 45, 99, 84, 100, 57, 84, 52, 87, 50, 113, 120, 72, 103, 103, 70, 110, 121, 113, 56, 49, 98, 78, 115, 100, 86, 97, 65, 100, 85, 116, 120, 84, 51)
//        val newRelicKey = decodeKey(newRelicIntArray)

        // TODO add New Relic Key
        val newRelicKey = ""

        println("New Relic Key origin: $newRelicKey")

        val publicKeyRSA = rsaEncrpytor.stringToPublicKey(PUBLIC_KEY_RSA_STR)

        val newRelicKeyEncrypted = rsaEncrpytor.encrypt(newRelicKey, publicKeyRSA, Constants.RSA_OAEP_ALGORITHM)

        println("New Relic Key Encrypted: $newRelicKeyEncrypted")
    }

    @Test
    fun `Decrypt New Relic Key after encrypted using the RSA key`() {
        // new relic key after encrypted (sample)
//        val newRelicKeyEncrypted = "XuAUTL1JGjjxphMsmGO9EUL3o9f/C8M9TN0FSOifkiG5yZoscpZr4qTOGPcwa6jvoISwXRu3GQEL\n" +
//            "x0fig7ZuEZS7UoL8rpf73NF3Mk0zv3BVhjLPR8py4lDRyMfGSVxNZN1FB/L8T4rGjMODgtkJ4g8K\n" +
//            "/wCLw9435g8zr499m0xbowcSwdI+gCXtxtoYRdGv6ybCWVkJiY7m4O8GpEX+HqffSjXORQ9Jy3Fm\n" +
//            "Qp9nVze3hDJEoSIRXy3K5TBzxwq18+SWng54Hdbxw1ZvQtswEdLhwgQjy0ojE6G3nDWiAvz3C9k/\n" +
//            "FPNwse6VwyvMNvFsCFWsV/VTnGo4IGFrbK2UKA=="

        // TODO add New Relic Key after encrypted
        val newRelicKeyEncrypted = ""

        println("New Relic Key Encrypted: $newRelicKeyEncrypted")

        val privateKeyRSA = rsaEncrpytor.stringToPrivateKey(PRIVATE_KEY_RSA_STR)

        val newRelicKeyDecrypted = rsaEncrpytor.decrypt(newRelicKeyEncrypted, privateKeyRSA, Constants.RSA_OAEP_ALGORITHM)

        println("New Relic Key Decrypted: $newRelicKeyDecrypted")
    }

    private fun decodeKey(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }
}
