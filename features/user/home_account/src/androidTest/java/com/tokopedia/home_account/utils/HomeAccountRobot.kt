package com.tokopedia.home_account.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.home_account.R

class HomeAccountRobot {

    fun showPengaturanAplikasi() {
        val afterPengaturanAplikasiPosition = 6
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(
                afterPengaturanAplikasiPosition
            )
        )
        onView(ViewMatchers.withText("Pengaturan Aplikasi")).perform(ViewActions.click())
        onView(withId(R.id.home_account_user_fragment_rv)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(afterPengaturanAplikasiPosition)
        )
        Thread.sleep(1000)
    }
}
