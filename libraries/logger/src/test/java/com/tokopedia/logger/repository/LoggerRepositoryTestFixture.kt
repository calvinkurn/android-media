package com.tokopedia.logger.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tokopedia.logger.datasource.cloud.LoggerCloudDataSource
import com.tokopedia.logger.datasource.cloud.LoggerCloudEmbraceImpl
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicApiImpl
import com.tokopedia.logger.datasource.cloud.LoggerCloudNewRelicSdkImpl
import com.tokopedia.logger.datasource.cloud.LoggerCloudSlardarApmDataSourceImpl
import com.tokopedia.logger.datasource.db.LoggerDao
import com.tokopedia.logger.model.scalyr.ScalyrConfig
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

abstract class LoggerRepositoryTestFixture {

    @RelaxedMockK
    lateinit var loggerDao: LoggerDao

    @RelaxedMockK
    lateinit var loggerCloudDataSource: LoggerCloudDataSource

    @RelaxedMockK
    lateinit var loggerCloudNewRelicSdkImpl: LoggerCloudNewRelicSdkImpl

    @RelaxedMockK
    lateinit var loggerCloudNewRelicApiImpl: LoggerCloudNewRelicApiImpl

    @RelaxedMockK
    lateinit var loggerCloudEmbraceImpl: LoggerCloudEmbraceImpl

    @RelaxedMockK
    lateinit var loggerCloudSlardarApmDataSourceImpl: LoggerCloudSlardarApmDataSourceImpl

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences

    @RelaxedMockK
    lateinit var scalyrConfigs: List<ScalyrConfig>

    protected var encrypt: ((String) -> String)? = null
    protected var decrypt: ((String) -> String)? = null

    protected var decryptNrKey: ((String) -> String)? = null

    protected lateinit var loggerRepository: LoggerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        encrypt = { s: String -> s }
        decrypt = { s: String -> s }
        loggerRepository = LoggerRepository(
            Gson(),
            loggerDao, loggerCloudDataSource, loggerCloudNewRelicSdkImpl, loggerCloudNewRelicApiImpl,
            loggerCloudEmbraceImpl, loggerCloudSlardarApmDataSourceImpl,
            scalyrConfigs, sharedPreferences, encrypt, decrypt, decryptNrKey
        )
    }
}
