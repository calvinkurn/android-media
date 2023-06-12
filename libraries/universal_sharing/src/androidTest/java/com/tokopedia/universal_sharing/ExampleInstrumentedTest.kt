//package com.tokopedia.universal_sharing
//
//import PdpShareTest
//import android.content.Intent
//import androidx.test.espresso.intent.rule.IntentsTestRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.tokopedia.common.UniversalShareTestActivity
//
//import org.junit.Test
//import org.junit.runner.RunWith
//
//import org.junit.Assert.*
//import org.junit.Rule
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//
//    @get:Rule
//    var activityTestRule = IntentsTestRule(
//        UniversalShareTestActivity::class.java, false, false
//    )
//
//
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        runTest {
//            Thread.sleep(3000)
//        }
//    }
//
//    private fun runTest(block: () -> Unit) {
//        activityTestRule.activity.setShareFragment(PdpShareTest())
//        activityTestRule.launchActivity(Intent());
//        block.invoke()
//    }
//}
