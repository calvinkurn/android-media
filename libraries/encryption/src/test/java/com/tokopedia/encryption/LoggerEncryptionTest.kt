package com.tokopedia.encryption

import com.tokopedia.encryption.security.AESEncryptorECB
import org.junit.Test

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

    val encryptor: AESEncryptorECB = AESEncryptorECB()

    @Test
    fun `AES encrypt`() {
    }

    @Test
    fun `AES decrypt`() {
    }
}
