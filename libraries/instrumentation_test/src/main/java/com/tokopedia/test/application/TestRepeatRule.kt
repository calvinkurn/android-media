package com.tokopedia.test.application

/**
 * @author ricoharisin .
 */
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A [TestRule] that will repeat each test repeatTestCount times if the test run was started with a
 * `repeatTestCount` test runner argument.
 */
class TestRepeatRule : TestRule {
    override fun apply(
            base: Statement,
            description: Description
    ): Statement {
        val args = InstrumentationRegistry.getArguments()
        val repeatTestCount = args?.getString("repeatTestCount")?.toInt() ?: 1
        return if (repeatTestCount > 1) {
            object : Statement() {
                override fun evaluate() {
                    for (i in 1..repeatTestCount) {
                        base.evaluate()
                    }
                }
            }
        } else {
            base
        }
    }
}
