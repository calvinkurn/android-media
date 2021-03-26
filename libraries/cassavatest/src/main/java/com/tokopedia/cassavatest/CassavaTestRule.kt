package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.cassava.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.cassava.validator.core.toDefaultValidator
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import kotlinx.coroutines.runBlocking
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CassavaTestRule(
        queryPath: String
) : TestRule {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
    private val daoSource = GtmLogDBSource(context)

    private val cassavaQuery = getQuery(context, queryPath)

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                dao.deleteAll()
                try {
                    base?.evaluate()
                } finally {
                    dao.deleteAll()
                }
            }
        }
    }

    fun validate(): List<Validator> {
        val validators = cassavaQuery.query.map { it.toDefaultValidator() }
        // run blocking because it runs on test thread
        return runBlocking {
            ValidatorEngine(daoSource).computeCo(validators, cassavaQuery.mode.value)
        }
    }

}