package com.tokopedia.talk.analytics

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_CREATE_NEW_QUESTION_PATH
import com.tokopedia.talk.feature.reading.presentation.activity.TalkReadingActivity
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.PRODUCT_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID_VALUE
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.PRODUCT_ID
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkReadingActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<TalkReadingActivity> = object: IntentsTestRule<TalkReadingActivity>(TalkReadingActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            fakeLogin()
        }

        override fun getActivityIntent(): Intent {
            return Intent(targetContext, TalkReadingActivity::class.java).apply {
                putExtra(PRODUCT_ID, PRODUCT_ID_VALUE)
                putExtra(PARAM_SHOP_ID, SHOP_ID_VALUE)
            }
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(TalkMockResponse())
    }

    @After
    fun tear() {
        clearLogin()
    }

    @Test
    fun validateClickCreateNewQuestion() {
        actionTest {
            clickAction(R.id.addFloatingActionButton)
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_CLICK_CREATE_NEW_QUESTION_PATH)
            gtmLogDBSource.finishTest()
        }
    }
}