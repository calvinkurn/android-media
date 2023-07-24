package com.tokopedia.media.loader.rule

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.rules.ExternalResource
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class ActivityCustomRule(
    val inner: ActivityScenarioRule<*>
) : ExternalResource() {

    override fun apply(base: Statement, description: Description): Statement =
        super.apply(inner.apply(base, description), description)

    override fun before() = Unit
    override fun after() = Unit
}
