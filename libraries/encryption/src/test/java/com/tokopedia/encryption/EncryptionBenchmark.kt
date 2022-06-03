package com.tokopedia.encryption

import android.os.Build
import com.tokopedia.encryption.security.*
import com.tokopedia.encryption.utils.Constants
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.crypto.SecretKey
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest=Config.NONE)
class EncryptionBenchmark {
    companion object {
        const val dummy_data = "{\"start_time\":1639,\"end_time\":1639,\"acc\":[[-0.26,4.94,8.55,0]]}"
        const val n = 10
        const val ENCRYPTION_KEY_16 = "q(e#%Gf@oi>lkB~h"
        const val ENCRYPTION_KEY_32 = "randomlyGeneratedKeyAES123456789"
    }

    private var aesEncryptorECB = AESEncryptorECB()
    private var aesEncryptorCBC = AESEncryptorCBC("random16bytesabc")
    private var aesEncryptorGCM = AESEncryptorGCM("nonce12bytes", true)
    private var aesEncryptorGCMNoNonce = AESEncryptorGCM("nonce12bytes", false)
    private var rsaEncryptor = RSA()
    private var arc4Encryptor = ARC4()
    private var blowfishEncryptor = Blowfish()
    private var DESEncryptor = DES()
    private var DESedeEncryptor = DESede()
    lateinit var aesKeyECB: SecretKey
    lateinit var aesKeyCBC: SecretKey
    lateinit var aesKeyGCM: SecretKey
    lateinit var arc4Key: SecretKey
    lateinit var blowfishKey: SecretKey
    lateinit var DESKey: SecretKey
    lateinit var DESedeKey: SecretKey

    @Before
    fun init() {
        aesKeyECB = aesEncryptorECB.generateKey(ENCRYPTION_KEY_16)
        aesKeyCBC = aesEncryptorCBC.generateKey(ENCRYPTION_KEY_16)
        aesKeyGCM = aesEncryptorGCM.generateKey(ENCRYPTION_KEY_32)
        rsaEncryptor.generateKeyPair()
        arc4Key = arc4Encryptor.generateKey(ENCRYPTION_KEY_16)
        blowfishKey = blowfishEncryptor.generateKey(ENCRYPTION_KEY_16)
        DESKey = DESEncryptor.generateKey(ENCRYPTION_KEY_16)
        DESedeKey = DESedeEncryptor.generateKey(ENCRYPTION_KEY_16)
    }

    @Test
    fun `AES GCM No Padding Encryption nonce`() {
        var result = ""
        val time = measureTimeMillis {
            repeat(n) {
                result = aesEncryptorGCM.encrypt(
                    dummy_data,
                    aesKeyGCM)
            }
        }
        println(result)
        println("AES gcm encryption: $n runs took $time ms")
    }

    @Test
    fun `AES GCM No Padding Decryption nonce`() {
        val encrypted = aesEncryptorGCM.encrypt(dummy_data, aesKeyGCM)
        println("encrypted: ")
        println(encrypted)
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorGCM.decrypt(encrypted, aesKeyGCM)
            }
        }
        println("AES gcm decryption no nonce: $n runs took $time ms")
        assert(aesEncryptorGCM.decrypt(encrypted, aesKeyGCM) == dummy_data)
    }

    @Test
    fun `AES GCM No Padding Encryption no nonce`() {
        var result = ""
        val time = measureTimeMillis {
            repeat(n) {
                result = aesEncryptorGCMNoNonce.encrypt(dummy_data, aesKeyGCM)
            }
        }
        println(result)
        println("AES gcm encryption no nonce: $n runs took $time ms")
    }

    @Test
    fun `AES GCM No Padding Decrypt no nonce`() {
        val encrypted = aesEncryptorGCMNoNonce.encrypt(dummy_data, aesKeyGCM)
        println("encrypted: ")
        println(encrypted)
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorGCMNoNonce.decrypt(encrypted, aesKeyGCM)
            }
        }
        println("AES gcm decryption no nonce: $n runs took $time ms")
        assert(aesEncryptorGCMNoNonce.decrypt(encrypted, aesKeyGCM) == dummy_data)
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
                aesEncryptorCBC.encrypt(dummy_data, aesKeyCBC)
            }
        }
        println("AES cbc encryption: $n runs took $time ms")
    }

    @Test
    fun `AES decryption test cbc`() {
        val encrypted = aesEncryptorCBC.encrypt(dummy_data, aesKeyCBC)
        val time = measureTimeMillis {
            repeat(n) {
                aesEncryptorCBC.decrypt(encrypted, aesKeyCBC)
            }
        }
        println("AES cbc decryption: $n runs took $time ms")
        assert(aesEncryptorCBC.decrypt(encrypted, aesKeyCBC) == dummy_data)
    }

    @Test
    fun `RSA encryption test PKCS1Padding`() {
        val time = measureTimeMillis {
            repeat(n) {
                rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey,
                    Constants.RSA_PKCS1_ALGORITHM)
            }
        }
        println("RSA encryption: $n runs took $time ms")
    }

    @Test
    fun `RSA decryption test PKCS1Padding`() {
        val encrypted = rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey,
            Constants.RSA_PKCS1_ALGORITHM)
        val time = measureTimeMillis {
            repeat(n) {
                rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey)
            }
        }
        println("RSA decryption: $n runs took $time ms")
        assert(rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey) == dummy_data)
    }

    @Test
    fun `RSA encryption test OAEP`() {
        var result = ""
        val time = measureTimeMillis {
            repeat(n) {
                result = rsaEncryptor.encrypt("randomlyGeneratedKeyAES123456789", rsaEncryptor.publicKey,
                    Constants.RSA_OAEP_ALGORITHM)
            }
        }
        println(result)
        println("RSA oaep encryption: $n runs took $time ms")
    }

    @Test
    fun `RSA decryption test OAEP`() {
        val encrypted = rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey,
            Constants.RSA_OAEP_ALGORITHM)
        val time = measureTimeMillis {
            repeat(n) {
                rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey, Constants.RSA_OAEP_ALGORITHM)
            }
        }
        println("RSA oaep decryption: $n runs took $time ms")
        assert(rsaEncryptor.decrypt(encrypted, rsaEncryptor.privateKey, Constants.RSA_OAEP_ALGORITHM) == dummy_data)
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
