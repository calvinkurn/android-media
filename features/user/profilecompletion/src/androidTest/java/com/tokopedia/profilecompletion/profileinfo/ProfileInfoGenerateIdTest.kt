package com.tokopedia.profilecompletion.profileinfo

import com.tokopedia.profilecompletion.common.ViewIdGenerator
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test

class ProfileInfoGenerateIdTest: ProfileInfoInstrumentTest() {
    @Test
    fun generate_view_id_file() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "profilecompletion_profile_info.csv")
        }
    }
}
