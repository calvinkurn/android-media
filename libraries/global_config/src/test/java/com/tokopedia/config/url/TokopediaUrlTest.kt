package com.tokopedia.config.url

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyString
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

/**
 * @author okasurya on 4/9/19.
 */
@RunWith(MockitoJUnitRunner::class)
class TokopediaUrlTest {
    lateinit var sharedPrefs: SharedPreferences
    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun before() {
        sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        context = Mockito.mock(Context::class.java)
        Mockito.`when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)
    }

    @Test
    fun init_multiThread_consistentInstance() {
        Mockito.`when`(sharedPrefs.getString(KEY_ENV, Env.LIVE.value)).thenReturn("STAGING")
        val initThread = Thread {
            val baseUrl = TokopediaUrl.getInstance(context)
            Assert.assertNotNull(baseUrl)
//            Assert.assertEquals(Env.LIVE, baseUrl.TYPE)
            Assert.assertEquals(Env.STAGING, baseUrl.TYPE)
            println(baseUrl.TYPE.value)
        }

        val execThread = Thread {
            val baseUrlTest = TokopediaUrl.getInstance(context)
            Assert.assertNotNull(baseUrlTest)
            Assert.assertEquals(Env.STAGING, baseUrlTest.TYPE)
            println(baseUrlTest.TYPE.value)
        }

        TokopediaUrl.deleteInstance()
        initThread.start().run {
            execThread.start()
        }
    }

    @Test
    fun selectInstanceTest_inputStaging_equalsStaging() {
        Assert.assertEquals(TokopediaUrl.selectInstance("STAGING").TYPE, Env.STAGING)
    }
}