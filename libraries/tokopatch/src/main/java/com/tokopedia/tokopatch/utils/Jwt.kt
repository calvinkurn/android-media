package com.tokopedia.tokopatch.utils

import android.content.Context
import android.util.Base64
import com.tokopedia.tokopatch.R
import com.tokopedia.tokopatch.domain.data.DataStatus
import com.tokopedia.tokopatch.model.Key
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.*
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.zip.GZIPInputStream

class Jwt(var status: DataStatus) {

    companion object {
        const val ALGO = "RSA"
        const val ISS = "iss"
        const val TARGET = "target_audience"
        const val AUD = "aud"
        const val EXP_TIME = 30
        const val MILISECOND = 60000L
        const val BYTE_SIZE = 1024
        const val OFFSITE = 0
    }

    fun readKey(context: Context): Key =
        try {
            val fileIn = context.resources.openRawResource(R.raw.stability)
            val gzipIn = GZIPInputStream(fileIn)
            val byteOut = ByteArrayOutputStream()
            var count: Int
            val data = ByteArray(BYTE_SIZE)
            while (gzipIn.read(data, OFFSITE, BYTE_SIZE).also { count = it } != -1) {
                byteOut.write(data, OFFSITE, count)
            }
            val byteIn = ByteArrayInputStream(byteOut.toByteArray())
            val oi = ObjectInputStream(byteIn)
            val key: Key = oi.readObject() as Key
            fileIn.close()
            gzipIn.close()
            oi.close()
            key
        } catch (e: Exception) {
            e.printStackTrace()
            Key()
        }


    fun token(context: Context): String {
        val key = readKey(context)
        val encodedKey: ByteArray = Base64.decode(key.pKey, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(encodedKey)

        val kf: KeyFactory = KeyFactory.getInstance(ALGO)
        val privKey = kf.generatePrivate(keySpec)
        val now = Date()
        val exp = Date().add(EXP_TIME)
        status.expired = exp.time.toString()
        val jwt = Jwts.builder()
            .claim(ISS, key.iss)
            .claim(TARGET, key.target)
            .claim(AUD, key.aud)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(privKey, SignatureAlgorithm.RS256)
            .compact()
        return jwt
    }

    private fun Date.add(minutes: Int): Date {
        val minuteMillis: Long = MILISECOND //millisecs
        val curTimeInMs: Long = this.time
        val result = Date(curTimeInMs + minutes * minuteMillis)
        this.time = result.time
        return this
    }

}