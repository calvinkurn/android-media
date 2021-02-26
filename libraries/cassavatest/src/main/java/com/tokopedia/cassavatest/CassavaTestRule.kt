package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CassavaTestRule : TestRule {

    val context: Context = ApplicationProvider.getApplicationContext<Context>()
    val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
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

    fun getAnalyticsByQuery(queryPath: String): List<Validator> {
        return getAnalyticsWithQuery(daoSource, context, queryPath)
    }
}