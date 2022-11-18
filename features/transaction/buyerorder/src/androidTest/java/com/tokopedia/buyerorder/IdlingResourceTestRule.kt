package com.tokopedia.buyerorder

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * created by @bayazidnasir on 6/9/2022
 *
 * A Test Rule that cleanups Idling Resource before and after running test
 */

class IdlingResourceTestRule: TestRule {

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement(){
            override fun evaluate() {
                try {
                    val instance = IdlingRegistry.getInstance()
                    instance.unregister(*instance.resources.toTypedArray())
                    base.evaluate()
                } finally {
                    val instance = IdlingRegistry.getInstance()
                    instance.unregister(*instance.resources.toTypedArray())
                }
            }
        }
    }
}