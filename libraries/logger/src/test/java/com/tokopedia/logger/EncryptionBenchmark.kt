package com.tokopedia.logger

import com.tokopedia.logger.security.*
import com.tokopedia.logger.utils.*
import org.junit.Before
import org.junit.Test
import javax.crypto.SecretKey
import kotlin.system.measureTimeMillis

class EncryptionBenchmark {
    companion object{
        val dummy_data = "P1#TagA#offline#Screen froze for 2s when opening activity"
        val secretKey = generateKey(Constants.ENCRYPTION_KEY)
        val n = 1000
    }

    // Encryptors
    private var rsaEncryptor = RSA()
    private var arc4Encryptor = ARC4()
    private var blowfishEncryptor = Blowfish()
    private var DESEncryptor = DES()
    private var DESedeEncryptor = DESede()
    lateinit var arc4Key: SecretKey
    lateinit var blowfishKey: SecretKey
    lateinit var DESKey: SecretKey
    lateinit var DESedeKey: SecretKey

    @Before
    fun init(){
        rsaEncryptor.generateKeyPair()
        arc4Key = arc4Encryptor.generateKey(Constants.ENCRYPTION_KEY)
        blowfishKey = blowfishEncryptor.generateKey(Constants.ENCRYPTION_KEY)
        DESKey = DESEncryptor.generateKey()
        DESedeKey = DESedeEncryptor.generateKey()
    }

    @Test
    fun `AES encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                encrypt(dummy_data,secretKey)
            }
        }
        println("AES encryption: $n runs took $time ms")
    }

    @Test
    fun `AES decryption test` (){
        val encrypted = encrypt(dummy_data, secretKey)
        val time = measureTimeMillis {
            repeat(n){
                decrypt(encrypted,secretKey)
            }
        }
        println("AES decryption: $n runs took $time ms")
    }

    @Test
    fun `RSA encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                rsaEncryptor.encrypt(dummy_data,rsaEncryptor.publicKey)
            }
        }
        println("RSA encryption: $n runs took $time ms")
    }

    @Test
    fun `RSA decryption test`(){
        val encrypted = rsaEncryptor.encrypt(dummy_data, rsaEncryptor.publicKey)
        val time = measureTimeMillis {
            repeat(n){
                rsaEncryptor.decrypt(encrypted,rsaEncryptor.privateKey)
            }
        }
        println("RSA decryption: $n runs took $time ms")
    }

    @Test
    fun `ARC4 encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                arc4Encryptor.encrypt(dummy_data,arc4Key)
            }
        }
        println("ARC4 encryption: $n runs took $time ms")
    }

    @Test
    fun `ARC4 decryption test`(){
        val encrypted = arc4Encryptor.encrypt(dummy_data,arc4Key)
        val time = measureTimeMillis {
            repeat(n){
                arc4Encryptor.decrypt(encrypted,arc4Key)
            }
        }
        println("ARC4 decryption: $n runs took $time ms")
    }

    @Test
    fun `Blowfish encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                blowfishEncryptor.encrypt(dummy_data,blowfishKey)
            }
        }
        println("Blowfish encryption: $n runs took $time ms")
    }

    @Test
    fun `Blowfish decryption test`(){
        val encrypted = blowfishEncryptor.encrypt(dummy_data,blowfishKey)
        val time = measureTimeMillis {
            repeat(n){
                blowfishEncryptor.decrypt(encrypted,blowfishKey)
            }
        }
        println("Blowfish decryption: $n runs took $time ms")
    }

    @Test
    fun `DES encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                DESEncryptor.encrypt(dummy_data,DESKey)
            }
        }
        println("DES encryption: $n runs took $time ms")
    }

    @Test
    fun `DES decryption test`(){
        val encrypted = DESEncryptor.encrypt(dummy_data,DESKey)
        val time = measureTimeMillis {
            repeat(n){
                DESEncryptor.decrypt(encrypted,DESKey)
            }
        }
        println("DES decryption: $n runs took $time ms")
    }

    @Test
    fun `DESede encryption test`(){
        val time = measureTimeMillis {
            repeat(n){
                DESedeEncryptor.encrypt(dummy_data,DESedeKey)
            }
        }
        println("DESede encryption: $n runs took $time ms")
    }

    @Test
    fun `DESede decryption test`(){
        val encrypted = DESedeEncryptor.encrypt(dummy_data,DESedeKey)
        val time = measureTimeMillis {
            repeat(n){
                DESedeEncryptor.decrypt(encrypted,DESedeKey)
            }
        }
        println("DESede decryption: $n runs took $time ms")
    }



}