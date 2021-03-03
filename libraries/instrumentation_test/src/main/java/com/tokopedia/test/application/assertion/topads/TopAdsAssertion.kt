package com.tokopedia.test.application.assertion.topads

import android.content.Context
import android.util.Log
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsVerificationNetworkSource
import com.tokopedia.analyticsdebugger.debugger.domain.GetTopAdsLogDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.deleteTopAdsVerificatorReportData
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorReportCoverage
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.usecase.RequestParams
import org.junit.Assert

/**
 * Created by DevAra
 * TopAds Assertion for instrumentation test
 */
class TopAdsAssertion(val context: Context,
                      val topAdsVerificatorInterface: TopAdsVerificatorInterface) {
    private var requestParams: RequestParams = RequestParams.create()

    init {
        deleteTopAdsVerificatorReportData(context)
    }

    /**
     * Call this on @After
     */
    fun after() {
        context.deleteDatabase("tkpd_gtm_log_analytics")
    }

    /**
     * Call this to after scrolling and click all posible topAds product
     * This function will assert saved topAds data from database
     */
    fun assert() {
        logTestMessage("Done UI Test")
        logTestMessage("Asserting data...")

        val listTopAdsDbFirst = readDataFromDatabase(context)
        val impressedCount = listTopAdsDbFirst.filter { it.eventType == "impression" }.size
        val clickCount = listTopAdsDbFirst.filter { it.eventType == "click" }.size
        val allCount = listTopAdsDbFirst.size
        val callerClass = Class.forName(Thread.currentThread().stackTrace[3].className).simpleName

        for (item in listTopAdsDbFirst) {
            logTestMessage("eventType: ${item.eventType} componentName: ${item.componentName} sourceName: ${item.sourceName} productName: ${item.productName} imageUrl: ${item.imageUrl}")
        }

        verifyTopAdsExists(allCount)
        verifyImpressionMoreThanClick(allCount, impressedCount, clickCount)
        verifyImpressionMoreThanResponse(impressedCount, topAdsVerificatorInterface)
        verifyUrlWithTopAdsVerificator(callerClass)
    }

    private fun readDataFromDatabase(context: Context): List<TopAdsLogDB> {
        val topAdsLogDBSource = TopAdsLogDBSource(context)
        val graphqlUseCase = GraphqlUseCase()
        val topAdsVerificationNetworkSource = TopAdsVerificationNetworkSource(context, graphqlUseCase)

        val topAdsLogLocalRepository = TopAdsLogLocalRepository(topAdsLogDBSource, topAdsVerificationNetworkSource)
        val getTopAdsLogDataUseCase = GetTopAdsLogDataUseCase(topAdsLogLocalRepository)

        setRequestParams(1, "")
        return getTopAdsLogDataUseCase.getData(requestParams)
    }

    private fun setRequestParams(page: Int, keyword: String) {
        requestParams.putString(AnalyticsDebuggerConst.KEYWORD, keyword)
        requestParams.putInt(AnalyticsDebuggerConst.PAGE, page)
        requestParams.putString(AnalyticsDebuggerConst.ENVIRONMENT, AnalyticsDebuggerConst.ENVIRONMENT_TEST)
    }

    private fun verifyTopAdsExists(allCount: Int) {
        logTestMessage("Check if topads exists (count > 0)...")
        logTestMessage("Topads product recorded on database : " + allCount)
        Assert.assertTrue("Topads should exist (topAdsCount should be more than 0)",allCount > 0)
        logTestMessage("Topads exist (count > 0)! -> PASSED")
    }

    private fun verifyImpressionMoreThanClick(allCount: Int, impressedCount: Int, clickCount: Int) {
        logTestMessage("Check if impression is more than click...")
        logTestMessage("Topads product recorded on database : " + allCount)
        logTestMessage("Impressed count : " + impressedCount)
        logTestMessage("Click count : " + clickCount)
        Assert.assertTrue(impressedCount >= clickCount)
        logTestMessage("Impressed count more than click! -> PASSED")
    }

    private fun verifyImpressionMoreThanResponse(impressedCount: Int, topAdsVerificatorInterface: TopAdsVerificatorInterface) {
        logTestMessage("Check if topads impression product in database reach at least minimum from response...")
        val minimumTopAdsProductFromResponse = topAdsVerificatorInterface.minimumTopAdsProductFromResponse
        logTestMessage("Topads from response (minimum) : " + minimumTopAdsProductFromResponse)
        logTestMessage("Topads impression product recorded on database : " + impressedCount)
        Assert.assertTrue(minimumTopAdsProductFromResponse <= impressedCount)
        logTestMessage("Topads impression product recorded on database is more than minimum! -> PASSED")
    }

    private fun verifyUrlWithTopAdsVerificator(callerClass: String) {
        logTestMessage("Waiting for topads backend verificator ready...")
        waitForVerificatorReady()

        val listTopAdsDb = readDataFromDatabase(context)

        writeTopAdsVerificatorReportCoverage(callerClass, listTopAdsDb)

        listTopAdsDb.assertAllStatusMatch()
    }

    private fun waitForVerificatorReady() {
        //wait for 15 seconds
        Thread.sleep(15000)
    }

    private fun List<TopAdsLogDB>.assertAllStatusMatch() {
        forEach {
            logTestMessage(it.sourceName + " - " + it.eventType + " - " + it.eventStatus)

            val component = if (it.componentName.isEmpty()) it.sourceName else it.componentName

            Assert.assertEquals(
                    "Component $component",
                    STATUS_MATCH, it.eventStatus)
        }
    }

    private fun logTestMessage(message: String) {
        writeTopAdsVerificatorLog(context, message)
        Log.d("TopAdsVerificatorLog", message)
    }
}