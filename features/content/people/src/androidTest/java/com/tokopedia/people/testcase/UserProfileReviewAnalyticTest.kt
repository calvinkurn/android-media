package com.tokopedia.people.testcase

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.people.robot.UserProfileRobot
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class UserProfileReviewAnalyticTest {

    private val userProfileRobot = UserProfileRobot()

    init {
        userProfileRobot.init()
    }

    @Before
    fun setUp() {
        userProfileRobot.setUpForReviewAnalyticTest()
    }

    @Test
    fun testAnalytic_userProfileReview() {
        userProfileRobot
            .launch()
            .clickReviewTab()
            .verifyEventAction("click - review tab")

            .clickProfileOptionButton()
            .verifyEventAction("click - gear icon")

            .clickReviewMedia(0, 0)
            .verifyEventAction("click - review media")
    }
}
