package com.tokopedia.accountprofile.biousername

import com.tokopedia.accountprofile.common.ViewIdGenerator
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test

class BioUsernameGenerateIdTest : BioUsernameInstrumentTest() {
    @Test
    fun generate_view_id_file() {
        runTest(true) {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "profilecompletion_bio_username.csv")
        }
    }
}
