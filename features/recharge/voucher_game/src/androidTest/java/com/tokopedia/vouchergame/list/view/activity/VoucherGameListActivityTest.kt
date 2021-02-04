package com.tokopedia.vouchergame.list.view.activity

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class VoucherGameListActivityTest{
    @get:Rule
    var activityRule: ActivityTestRule<VoucherGameListActivity> = ActivityTestRule(VoucherGameListActivity::class.java)

    @Test
    fun testHomeLayout() {
        Thread.sleep(1000)
    }
}