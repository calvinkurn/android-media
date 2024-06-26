package com.tokopedia.test.application.environment

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ActivityScenarioTestRule<T : Activity> : TestRule {

    var scenario: ActivityScenario<T>? = null
    var activity: Activity? = null

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    scenario?.close()
                }
            }
        }
    }

    fun launchActivity(intent: Intent) {
        scenario = ActivityScenario.launch(intent)
        scenario?.onActivity {
            activity = it
        }
    }
}
