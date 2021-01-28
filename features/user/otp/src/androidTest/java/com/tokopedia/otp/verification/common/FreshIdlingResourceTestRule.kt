package com.tokopedia.otp.verification.common

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class FreshIdlingResourceTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return FreshIdlingResourceStatement(base)
    }

    inner class FreshIdlingResourceStatement(private val baseStatement: Statement) : Statement() {

        override fun evaluate() {
            try {
                val instance = IdlingRegistry.getInstance()
                instance.unregister(*instance.resources.toTypedArray())
                baseStatement.evaluate()
            } finally {
                val instance = IdlingRegistry.getInstance()
                instance.unregister(*instance.resources.toTypedArray())
            }
        }
    }
}