package com.tokopedia.test.application.benchmark_component

import com.tokopedia.test.application.benchmark_component.activity.TkpdIsolationActivity
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class BenchmarkViewRule: TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return base
    }

    fun getBenchmarkActivity() = TkpdIsolationActivity.singleton.get()
}