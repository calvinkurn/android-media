package com.tokopedia.logger.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.encryption.security.BaseEncryptor
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicImpl
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.datasource.db.LoggerRoomDatabase
import com.tokopedia.logger.model.newrelic.NewRelicConfig
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import javax.crypto.SecretKey

abstract class LoggerRepositoryTestFixture {

    lateinit var loggerDao: LoggerDao

    lateinit var context: Context

    @RelaxedMockK
    lateinit var loggerCloudDataSource: LoggerCloudDataSource

    @RelaxedMockK
    lateinit var loggerCloudNewRelicImpl: LoggerCloudNewRelicImpl

    @RelaxedMockK
    lateinit var scalyrConfigs: List<ScalyrConfig>

    @RelaxedMockK
    lateinit var newRelicConfigs: List<NewRelicConfig>

    @RelaxedMockK
    lateinit var encrpytor: BaseEncryptor

    @RelaxedMockK
    lateinit var secretKey: SecretKey

    protected lateinit var loggerRepository: LoggerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        context = ApplicationProvider.getApplicationContext()
        loggerDao = LoggerRoomDatabase.getDatabase(context).logDao()
        loggerRepository = LoggerRepository(loggerDao, loggerCloudDataSource, loggerCloudNewRelicImpl,
                scalyrConfigs, newRelicConfigs, encrpytor, secretKey)
    }
}