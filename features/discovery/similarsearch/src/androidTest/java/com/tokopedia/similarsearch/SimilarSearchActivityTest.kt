package com.tokopedia.similarsearch

import android.content.pm.PackageManager.NameNotFoundException
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class SimilarSearchActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(SimilarSearchActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.tokopedia.similarsearch.test", appContext.packageName)

        val zzbd = try {
            val packageManager = appContext.packageManager
            val packageInfo = packageManager.getPackageInfo(appContext.packageName, 0)

            packageInfo.versionName
        } catch (e: NameNotFoundException) {
            ""
        }

        println("package info version name: $zzbd")

        Thread.sleep(100)
    }
}