package com.tokopedia.loginregister.redefineregisteremail.stub.common

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity

fun ActivityTestRule<RedefineRegisterEmailActivity>.launchFragment(
    destinationId: Int,
    argBundle: Bundle? = null
) {
    val launchFragmentIntent = buildLaunchFragmentIntent(destinationId, argBundle)
    this.launchActivity(launchFragmentIntent)
}

private fun buildLaunchFragmentIntent(destinationId: Int, argBundle: Bundle?): Intent =
    NavDeepLinkBuilder(InstrumentationRegistry.getInstrumentation().targetContext)
        .setGraph(R.navigation.navigation_redefine_register)
        .setComponentName(RedefineRegisterEmailActivity::class.java)
        .setDestination(destinationId)
        .setArguments(argBundle)
        .createTaskStackBuilder().intents[0]
