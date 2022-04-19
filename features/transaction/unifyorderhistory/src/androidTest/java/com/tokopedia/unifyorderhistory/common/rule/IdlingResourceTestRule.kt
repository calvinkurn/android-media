package com.tokopedia.buyerorder.common.rule

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Created by fwidjaja on 07/11/20.
 */
class IdlingResourceTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return IdlingResourceStatement(base)
    }

    inner class IdlingResourceStatement(private val baseStatement: Statement) : Statement() {

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