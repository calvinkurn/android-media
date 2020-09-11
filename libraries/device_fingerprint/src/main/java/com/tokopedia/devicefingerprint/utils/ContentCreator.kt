package com.tokopedia.devicefingerprint.utils

import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.devicefingerprint.payload.DeviceInfoPayload
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RSA
import java.security.MessageDigest
import java.security.PublicKey
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


class ContentCreator @Inject constructor(
        private val gson: Gson,
        private val rsa: RSA
){

    // TODO Should be moved somewhere else. This key is for staging only.
    private val publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaDPpCjDLd15gwys1oYW+90ALTYKShRvz3ZzOlYLxfh6uBrWqb05UD+nilZ84z3UmHWZ+LqkE+GhifwI965iTUg4Oz67K52SJ19BXs8SpyLxRmovi539CiRCu6ZMQSAGl9GC/Zv1/tV8UFx2XuprRkwbUJU0oA0xXcP2sqCkN/EwIDAQAB"

//    fun createContent(deviceInfoPayload: DeviceInfoPayload): String {
//        // Create key from 32-byte random byte array, encrypted with RSA using publicKey
//        val secretKeyInByteArray = RandomHelper.randomByteArray(32)
//        val key = rsa.encrypt(secretKeyInByteArray, publicKey, Utils::byteToHex)
//
//        // Create iv
//        val ivInString = RandomHelper.randomNumber(16)
//
//        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
//        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
//        val deviceInfoPayloadInJson = gson.toJson(deviceInfoPayload)
//        val secretKey = aesEncryptorCBC.generateKey(secretKeyInByteArray)
//        val encryptedPayload = aesEncryptorCBC.encrypt(deviceInfoPayloadInJson, secretKey, Utils::byteToHex)
//
//        return "${key}${ivInString}${encryptedPayload}"
//    }

    fun createContent(deviceInfoPayload: DeviceInfoPayload): String {
        // Create key from 32-byte random byte array, encrypted with RSA using publicKey
        val secretKeyInString = "passphrasewhichneedstobe32bytes!"
        val publicKey = rsa.stringToPublicKey(publicKeyString)
        val key = encrypt(secretKeyInString, publicKey)

        // Create iv
        val ivInString = "1234567890123456"

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val deviceInfoPayloadInJson = """{"device_os":"android","is_rooted":true,"user_agent":"TkpdConsumer\/3.80","is_tablet":false,"ads_id":"testing-adsid","pid":"wptoipeotieprot34534534","android_id":"asd234sdfsdfdgfh","serial_number":"SERIALNUM","build_fingerprint":"BUILDFP","build_serial":"BUILDSERIAL","build_id":"BUILDID","build_version_incremental":"3","app_version":"3.78","is_from_playstore":true,"uuid":"UUID1234","user_id":123123123123,"dvmod": "deviceModel", "dvmanu": "deviceManufacturer", "tzone": "timezone", "scrres": "screenRes", "lang": "deviceLanguage", "ssid": "SSID", "carri": "deviceCarrier", "lat": "latitude", "long": "longitude", "cpu": "cpuInfo", "bdispl": "buildDisplay", "bboard": "buildBoard", "bsupabis": "buildSupportedAbis", "bhost": "buildHost", "pkgname": "packageName", "wifiip": "wifiIP", "font": "sysFontMap", "1stinstall": "FirstInstallTime", "lastupdate": "LastUpdateTime", "tsinceboot": "timeSinceBoot", "1stboot": "firstBootTime", "scrinf": "screenInfo", "simid": "SIMID", "imsi": "IMSI", "mcc": "MCC", "mnc": "MNC", "bootcount": 1, "permissionapp": ["test"]}"""

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
        val secretKey = aesEncryptorCBC.generateKey(secretKeyInString)
        val encryptedPayload = aesEncryptorCBC.encrypt(deviceInfoPayloadInJson, secretKey) { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        return "${key}${ivInString}${encryptedPayload}"
    }

    private fun encrypt(text: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        val encryptedBytes = cipher.doFinal(text.toByteArray());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    private fun aesEncrypt(payload: String, iv: String, key: String): String {
        val ivParameterSpec = IvParameterSpec(iv.toByteArray())
        val ivSize = 16

        // Hashing key.
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.update(key.toByteArray(Charsets.UTF_8))
        val keyBytes = ByteArray(16)
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.size)
        val secretKeySpec = SecretKeySpec(keyBytes, "AES")

        // Encrypt.
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(payload.toByteArray())

        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }
}
