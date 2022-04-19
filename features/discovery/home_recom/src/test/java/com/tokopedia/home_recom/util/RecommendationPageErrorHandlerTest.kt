package com.tokopedia.home_recom.util

import android.content.Context
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Lukas on 2019-07-13
 */
class RecommendationPageErrorHandlerTest{

    @MockK
    lateinit var context: Context

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun testErrorMessageWhenThrowableNull(){
        assertNotNull(ErrorHandler.getErrorMessage(context, null))
    }

//    @Test
//    fun testErrorMessageWhenUsingMessageErrorException(){
//        val defaultError = "default_error"
//        val throwable = MessageErrorException(defaultError)
//        assertEquals(RecommendationPageErrorHandler.getErrorMessage(context, throwable), defaultError)
//    }

    @Test
    fun testErrorMessageWhenUsingIOException(){
        val defaultError = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"
        every { context.getString(any()) } returns defaultError
        val throwable = IOException()
//        assertEquals(RecommendationPageErrorHandler.getErrorMessage(context, throwable), defaultError)
    }

    @Test
    fun testErrorMessageWhenSocketTimeoutException(){
        val defaultError = "Koneksi timeout. Silakan coba beberapa saat lagi"
        every { context.getString(any()) } returns defaultError
        val throwable = SocketTimeoutException()
        assertTrue(ErrorHandler.getErrorMessage(context, throwable).startsWith("Koneksi timeout. Silakan coba beberapa saat lagi"))
    }

    @Test
    fun testErrorMessageWhenNoInternetException(){
        val defaultError = "Tidak ada koneksi internet"
        every { context.getString(any()) } returns defaultError
        val throwable = UnknownHostException()
        assertTrue(ErrorHandler.getErrorMessage(context, throwable).startsWith("Tidak ada koneksi internet"))
    }
}