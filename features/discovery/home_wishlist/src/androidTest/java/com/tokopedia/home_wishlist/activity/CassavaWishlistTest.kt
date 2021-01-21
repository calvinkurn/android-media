//package com.tokopedia.home_wishlist.activity
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.rule.ActivityTestRule
//import com.tokopedia.abstraction.base.app.BaseMainApplication
//import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
//import com.tokopedia.home_wishlist.component.HasComponent
//import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
//import com.tokopedia.home_wishlist.di.WishlistComponent
//import com.tokopedia.home_wishlist.test.R
//import com.tokopedia.home_wishlist.view.fragment.WishlistFragment
//import com.tokopedia.test.application.util.InstrumentationAuthHelper.clearUserSession
//import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestUser1
//import com.tokopedia.test.application.util.setupGraphqlMockResponse
//import org.junit.Before
//import org.junit.Rule
//
//class CassavaWishlistTest {
//
//    @get:Rule
//    var activityRule = object: ActivityTestRule<InstrumentationWishlistTestActivity>(InstrumentationWishlistTestActivity::class.java) {
//        override fun beforeActivityLaunched() {
//            gtmLogDBSource.deleteAll().subscribe()
//            super.beforeActivityLaunched()
//            setupGraphqlMockResponse()
//        }
//    }
//
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//    private val gtmLogDBSource = GtmLogDBSource(context)
//
//    @Before
//    fun resetAll() {
//        gtmLogDBSource.deleteAll().subscribe()
//    }
//
//    @Test
//    fun testWishlistImpressionAndClick() {
//        initTest()
//
//        doActivityTest()
//
//        doHomeCassavaTest()
//
//        onFinishTest()
//
//        addDebugEnd()
//    }
//
//    private fun initTest() {
//        clearUserSession()
//        waitForData()
//    }
//}