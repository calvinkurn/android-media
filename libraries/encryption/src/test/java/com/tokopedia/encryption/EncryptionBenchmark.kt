package com.tokopedia.encryption

import com.tokopedia.encryption.security.*
import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Utils
import org.junit.Before
import org.junit.Test
import javax.crypto.SecretKey
import kotlin.system.measureTimeMillis

class EncryptionBenchmark {
    companion object {
        const val dummy_data = "P1#TagA#offline#Screen froze for 2s when opening activity"
        const val n = 1000
        const val ENCRYPTION_KEY = "q(e#%Gf@oi>lkB~h"
    }

    private var aesEncryptorECB = AESEncryptorECB()
    private var aesEncryptorCBC = AESEncryptorCBC("random16bytesabc")
    private var rsaEncryptor = RSA()
    private var arc4Encryptor = ARC4()
    private var blowfishEncryptor = Blowfish()
    private var DESEncryptor = DES()
    private var DESedeEncryptor = DESede()
    lateinit var aesKeyECB: SecretKey
    lateinit var aesKeyCBC: SecretKey
    lateinit var arc4Key: SecretKey
    lateinit var blowfishKey: SecretKey
    lateinit var DESKey: SecretKey
    lateinit var DESedeKey: SecretKey

    @Before
    fun init() {
        aesKeyECB = aesEncryptorECB.generateKey(ENCRYPTION_KEY)
        aesKeyCBC = aesEncryptorCBC.generateKey(ENCRYPTION_KEY)
        rsaEncryptor.generateKeyPair()
        arc4Key = arc4Encryptor.generateKey(ENCRYPTION_KEY)
        blowfishKey = blowfishEncryptor.generateKey(ENCRYPTION_KEY)
        DESKey = DESEncryptor.generateKey(ENCRYPTION_KEY)
        DESedeKey = DESedeEncryptor.generateKey(ENCRYPTION_KEY)
    }

    @Test
    fun `AES encryption est ecb`() {
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorECB.encrypt(dummy_data, aesKeyECB)
            }
        }
        println("AES ecb encryption: $n runs took $time ms")
    }

    @Test
    fun `AES decryption test ecb`() {
        val encrypted = aesEncryptorECB.encrypt(dummy_data, aesKeyECB)
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorECB.decrypt(encrypted, aesKeyECB)
            }
        }
        println("AES ecb decryption: $n runs took $time ms")
        assert(aesEncryptorECB.decrypt(encrypted, aesKeyECB) == dummy_data)
    }

    @Test
    fun `AES encryption test cbc`() {
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorCBC.encrypt(dummy_data, aesKeyCBC, Utils::byteToHex)
            }
        }
        println("AES cbc encryption: $n runs took $time ms")
    }

    @Test
    fun `AES decryption test cbc`() {
        val encrypted = aesEncryptorCBC.encrypt(dummy_data, aesKeyCBC, Utils::byteToHex)
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorCBC.decrypt(encrypted, aesKeyCBC, Utils::decodeHex)
            }
        }
        println("AES cbc decryption: $n runs took $time ms")
        assert(aesEncryptorCBC.decrypt(encrypted, aesKeyCBC, Utils::decodeHex) == dummy_data)
    }

    @Test
    fun `RSA encryption test`() {
        val time = measureTimeMillis {
            repeat(n) {
                rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey,
                        Constants.RSA_ALGORITHM, Utils::byteToHex)
            }
        }
        println("RSA encryption: $n runs took $time ms")
    }

    @Test
    fun `RSA decryption test`() {
        val encrypted = rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey,
                Constants.RSA_ALGORITHM, Utils::byteToHex)
        val time = measureTimeMillis {
            repeat(n) {
                rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey, Utils::decodeHex)
            }
        }
        println("RSA decryption: $n runs took $time ms")
        assert(rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey, Utils::decodeHex) == dummy_data)
    }

    @Test
    fun `ARC4 encryption test`() {
        val time = measureTimeMillis {
            repeat(n) {
                arc4Encryptor.encrypt(dummy_data, arc4Key)
            }
        }
        println("ARC4 encryption: $n runs took $time ms")
    }

    @Test
    fun `ARC4 decryption test`() {
        val encrypted = arc4Encryptor.encrypt(dummy_data, arc4Key)
        val time = measureTimeMillis {
            repeat(n) {
                arc4Encryptor.decrypt(encrypted, arc4Key)
            }
        }
        println("ARC4 decryption: $n runs took $time ms")
        assert(arc4Encryptor.decrypt(encrypted, arc4Key) == dummy_data)
    }

    @Test
    fun `Blowfish encryption test`() {
        val time = measureTimeMillis {
            repeat(n) {
                blowfishEncryptor.encrypt(dummy_data, blowfishKey)
            }
        }
        println("Blowfish encryption: $n runs took $time ms")
    }

    @Test
    fun `Blowfish decryption test`() {
        val encrypted = blowfishEncryptor.encrypt(dummy_data, blowfishKey)
        val time = measureTimeMillis {
            repeat(n) {
                blowfishEncryptor.decrypt(encrypted, blowfishKey)
            }
        }
        println("Blowfish decryption: $n runs took $time ms")
        assert(blowfishEncryptor.decrypt(encrypted, blowfishKey) == dummy_data)
    }

    @Test
    fun `DES encryption test`() {
        val time = measureTimeMillis {
            repeat(n) {
                DESEncryptor.encrypt(dummy_data, DESKey)
            }
        }
        println("DES encryption: $n runs took $time ms")
    }

    @Test
    fun `DES decryption test`() {
        val encrypted = DESEncryptor.encrypt(dummy_data, DESKey)
        val time = measureTimeMillis {
            repeat(n) {
                DESEncryptor.decrypt(encrypted, DESKey)
            }
        }
        println("DES decryption: $n runs took $time ms")
        assert(DESEncryptor.decrypt(encrypted, DESKey) == dummy_data)
    }

    @Test
    fun `DESede encryption test`() {
        val time = measureTimeMillis {
            repeat(n) {
                DESedeEncryptor.encrypt(dummy_data, DESedeKey)
            }
        }
        println("DESede encryption: $n runs took $time ms")
    }

    @Test
    fun `DESede decryption test`() {
        val encrypted = DESedeEncryptor.encrypt(dummy_data, DESedeKey)
        val time = measureTimeMillis {
            repeat(n) {
                DESedeEncryptor.decrypt(encrypted, DESedeKey)
            }
        }
        println("DESede decryption: $n runs took $time ms")
        assert(DESedeEncryptor.decrypt(encrypted, DESedeKey) == dummy_data)
    }

}
