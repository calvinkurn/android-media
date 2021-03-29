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

class CassavaTestRule : TestRule {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
    private val daoSource = GtmLogDBSource(context)

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

    fun validateByQuery(path: String): List<Validator> {
        val cassavaQuery = getQuery(context, path)
        val validators = cassavaQuery.query.map { it.toDefaultValidator() }
        return runBlocking {
            ValidatorEngine(daoSource).computeCo(validators, cassavaQuery.mode.value)
        }
    }

}