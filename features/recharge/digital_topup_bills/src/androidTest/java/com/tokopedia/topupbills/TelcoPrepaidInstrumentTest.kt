package com.tokopedia.topupbills

import android.util.Log
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.tokopedia.topupbills.telco.view.activity.TelcoPrepaidActivity
import org.junit.Rule
import org.junit.Test

//@RunWith(AndroidJUnit4.class)
@SmallTest
class TelcoPrepaidInstrumentTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<TelcoPrepaidActivity> = ActivityTestRule(TelcoPrepaidActivity::class.java)

//    @Throws(Throwable::class)
//    private fun setContentView(layoutId: Int) {
//        val activity: Activity = mActivityRule.getActivity()
//        mActivityRule.runOnUiThread { activity.setContentView(layoutId) }
//    }

    @Test
    @Throws(Throwable::class)
    fun inflation() {
        Log.d("Test", "test")
    }
//
//    private fun getActivity(): Activity? {
//        return mActivityRule.getActivity()
//    }
//
//    private fun getInstrumentation(): Instrumentation? {
//        return InstrumentationRegistry.getInstrumentation()
//    }
}