package com.tokopedia.similarsearch

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SimilarSearchActivityTest {

    @Test
    fun testOpenActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse("${ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE}/433759643/")
            it.setPackage(context.packageName)
            it.addCategory(Intent.CATEGORY_DEFAULT)
            it.addCategory(Intent.CATEGORY_BROWSABLE)
            it.putExtra(SearchConstant.SimilarSearch.QUERY, "samsung")
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)

        Thread.sleep(10000)
    }
}