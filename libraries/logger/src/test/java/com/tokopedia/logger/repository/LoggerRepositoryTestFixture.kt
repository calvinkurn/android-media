package com.tokopedia.logger.repository

import com.tokopedia.encryption.security.BaseEncryptor
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

    @RelaxedMockK
    lateinit var encrpytor: BaseEncryptor

    @RelaxedMockK
    lateinit var secretKey: SecretKey

    protected lateinit var loggerRepository: LoggerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        loggerRepository = LoggerRepository(loggerDao, loggerCloudDataSource, loggerCloudNewRelicImpl,
                scalyrConfigs, newRelicConfigs, encrpytor, secretKey)
    }

}