package com.tokopedia.logger.repository

import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicImpl
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import javax.crypto.SecretKey

abstract class LoggerRepositoryTestFixture {

    @RelaxedMockK
    lateinit var loggerDao: LoggerDao

    @RelaxedMockK
    lateinit var loggerCloudDataSource: LoggerCloudDataSource

    @RelaxedMockK
    lateinit var loggerCloudNewRelicImpl: LoggerCloudNewRelicImpl

    @RelaxedMockK
    lateinit var scalyrConfigs: List<ScalyrConfig>

    @RelaxedMockK
    lateinit var newRelicConfigs: NewRelicConfig

    protected var encrypt: ((String) -> String)? = null
    protected var decrypt: ((String) -> String)? = null

    protected lateinit var loggerRepository: LoggerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val encryptor = AESEncryptorECB()
        val ENCRYPTION_KEY = String(charArrayOf(113.toChar(), 40.toChar(), 101.toChar(), 35.toChar(), 37.toChar(), 71.toChar(), 102.toChar(), 64.toChar(), 111.toChar(), 105.toChar(), 62.toChar(), 108.toChar(), 107.toChar(), 66.toChar(), 126.toChar(), 104.toChar()))
        val secretKey: SecretKey = encryptor.generateKey(ENCRYPTION_KEY)

        encrypt = {s : String -> encryptor.encrypt(s, secretKey)}
        decrypt = {s : String -> encryptor.decrypt(s, secretKey)}
        loggerRepository = LoggerRepository(loggerDao, loggerCloudDataSource, loggerCloudNewRelicImpl,
                scalyrConfigs, newRelicConfigs, encrypt, decrypt)
    }

}