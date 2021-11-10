package com.tokopedia.home_account

import android.content.Intent
import com.tokopedia.home_account.base.HomeAccountTest
import org.junit.Test

class HomeAccountInstrumentTest : HomeAccountTest() {

    @Test
    fun testRun() {
        runTest {
            Thread.sleep(3000)
        }
    }
}