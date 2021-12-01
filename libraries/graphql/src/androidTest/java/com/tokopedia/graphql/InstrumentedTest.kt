package com.tokopedia.graphql


import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.graphql.data.GraphqlClient
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    lateinit var targetContext: Context

    @Before
    fun before() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tokopedia.graphql", appContext.packageName)
    }

    @Test
    fun callFunction() {
        val function = GraphqlClient.Function(targetContext)

        assert(function.akamaiValue.equals("lalala"), { "lalala" })

    }
}