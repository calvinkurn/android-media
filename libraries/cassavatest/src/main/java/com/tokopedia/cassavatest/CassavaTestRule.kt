package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.analyticsdebugger.cassava.utils.AnalyticsParser
import com.tokopedia.analyticsdebugger.cassava.core.Validator
import com.tokopedia.analyticsdebugger.cassava.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.cassava.core.toDefaultValidator
import com.tokopedia.analyticsdebugger.cassava.data.CassavaDatabase
import com.tokopedia.analyticsdebugger.cassava.database.GtmLogDB
import com.tokopedia.analyticsdebugger.cassava.debugger.data.repository.GtmRepo
import kotlinx.coroutines.runBlocking
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * @param isFromNetwork if True, will use thanos regex validation, if false will use local regex validation
 *                      Default is False because Thanos still in staging
 * @param sendValidationResult if True, will send validation result to Thanos API.
 *                             Default is True, can be False for development purpose
 */
class CassavaTestRule(
    private val isFromNetwork: Boolean = false,
    private val sendValidationResult: Boolean = true
) : TestRule {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dao = CassavaDatabase.getInstance(context).cassavaDao()
    private val repository = GtmRepo(dao)
    private val analyticsParser = AnalyticsParser()
    private val engine = ValidatorEngine(repository, analyticsParser)

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

    fun validate(queryId: String): List<Validator> {
        val cassavaQuery = getQuery(context, queryId, isFromNetwork)
        val validators = cassavaQuery.query.map { it.toDefaultValidator() }
        return runBlocking {
            val validationResult = engine.compute(validators, cassavaQuery.mode.value)
            if (isFromNetwork && sendValidationResult)
                sendTestResult(queryId, validationResult)
            validationResult
        }
    }

    fun validate(query: List<Map<String, Any>>, mode: String = MODE_EXACT): List<Validator> {
        val validators = query.map { it.toDefaultValidator() }
        return runBlocking {
            engine.compute(validators, mode)
        }
    }

    fun getRecent(take: Int = 3): List<GtmLogDB> {
        return runBlocking {
            dao.getLastTracker(take)
        }
    }

    companion object {
        const val MODE_EXACT = "exact"
        const val MODE_SUBSET = "subset"
    }

}