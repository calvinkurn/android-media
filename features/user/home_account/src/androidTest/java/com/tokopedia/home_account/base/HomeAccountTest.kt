package com.tokopedia.home_account.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.home_account.common.idling.FragmentTransactionIdle
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.di.DaggerHomeAccountUserComponents
import com.tokopedia.home_account.stub.di.HomeAccountUserComponentsStub
import com.tokopedia.home_account.stub.di.HomeAccountUserComponentsStubBuilder
import com.tokopedia.home_account.stub.domain.usecase.*
import com.tokopedia.home_account.stub.view.activity.HomeAccountUserActivityStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity.Companion.TAG
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

abstract class HomeAccountTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        HomeAccountUserActivityStub::class.java, false, false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Inject
    protected lateinit var getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCaseStub

//    @Inject
//    protected lateinit var getBalanceAndPointUseCase: GetBalanceAndPointUseCaseStub
//
//    @Inject
//    protected lateinit var getTokopointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCaseStub
//
//    @Inject
//    protected lateinit var getSaldoBalanceUseCase: GetSaldoBalanceUseCaseStub
//
//    @Inject
//    protected lateinit var getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCaseStub
//
//    @Inject
//    protected lateinit var getWalletEligibleUseCase: GetWalletEligibleUseCaseStub

    @Inject
    @Named("CentralizedUserAssetDataModelSuccess")
    protected lateinit var centralizedUserAssetDataModelSuccess: CentralizedUserAssetDataModel
//
//    @Inject
//    @Named("CentralizedUserAssetDataModelFailed")
//    protected lateinit var centralizedUserAssetDataModelFailed: CentralizedUserAssetDataModel
//
//    @Inject
//    @Named("GopayBalanceAndPointDataModelSuccess")
//    protected lateinit var gopayBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("GopayBalanceAndPointDataModelFailed")
//    protected lateinit var gopayBalanceAndPointDataModelFailed: BalanceAndPointDataModel
//
//    @Inject
//    @Named("TokopointBalanceAndPointDataModelSuccess")
//    protected lateinit var tokopointBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("TokopointBalanceAndPointDataModelFailed")
//    protected lateinit var tokopointBalanceAndPointDataModelFailed: BalanceAndPointDataModel

//    @Inject
//    @Named("OvoBalanceAndPointDataModelSuccess")
//    protected lateinit var ovoBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("OvoBalanceAndPointDataModelFailed")
//    protected lateinit var ovoBalanceAndPointDataModelFailed: BalanceAndPointDataModel

//    @Inject
//    @Named("SaldoBalanceAndPointDataModelSuccess")
//    protected lateinit var saldoBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("SaldoBalanceAndPointDataModelFailed")
//    protected lateinit var saldoBalanceAndPointDataModelFailed: BalanceAndPointDataModel
//
//    @Inject
//    @Named("GopaylaterBalanceAndPointDataModelSuccess")
//    protected lateinit var gopaylaterBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("GopaylaterBalanceAndPointDataModelFailed")
//    protected lateinit var gopaylaterBalanceAndPointDataModelFailed: BalanceAndPointDataModel
//
//    @Inject
//    @Named("CobrandCCBalanceAndPointDataModelSuccess")
//    protected lateinit var cobrandCCBalanceAndPointDataModelSuccess: BalanceAndPointDataModel
//
//    @Inject
//    @Named("CobrandCCyBalanceAndPointDataModelFailed")
//    protected lateinit var coobrandCCBalanceAndPointDataModelFailed: BalanceAndPointDataModel

    protected open lateinit var activity: HomeAccountUserActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    open fun before() {
        homeAccountUserComponents = HomeAccountUserComponentsStubBuilder.getComponent(applicationContext, context)
        homeAccountUserComponents.inject(this)
    }

    @After
    open fun tearDown() {
        activityTestRule.finishActivity()
    }

    protected fun setupGetCentralizedUserAssetConfigUseCaseResponse(isSuccess: Boolean) {
//        getCentralizedUserAssetConfigUseCase.response = if (isSuccess) centralizedUserAssetDataModelSuccess else centralizedUserAssetDataModelFailed
        getCentralizedUserAssetConfigUseCase.response = centralizedUserAssetDataModelSuccess
    }

//    protected fun setupGetGopayBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getBalanceAndPointUseCase.response = if (isSuccess) gopayBalanceAndPointDataModelSuccess else gopayBalanceAndPointDataModelFailed
//    }
//
//    protected fun setupGetTokopointBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getTokopointsBalanceAndPointUseCase.response = if (isSuccess) tokopointBalanceAndPointDataModelSuccess else tokopointBalanceAndPointDataModelFailed
//    }
//
//    protected fun setupGetOvoBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getBalanceAndPointUseCase.response = if (isSuccess) ovoBalanceAndPointDataModelSuccess else ovoBalanceAndPointDataModelFailed
//    }
//
//    protected fun setupGetSaldoBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getSaldoBalanceUseCase.response = if (isSuccess) saldoBalanceAndPointDataModelSuccess else saldoBalanceAndPointDataModelFailed
//    }
//
//    protected fun setupGetGopaylaterBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getBalanceAndPointUseCase.response = if (isSuccess) gopaylaterBalanceAndPointDataModelSuccess else gopaylaterBalanceAndPointDataModelFailed
//    }
//
//    protected fun setupGetCoBrandCCBalanceAndPointUseCaseResponse(isSuccess: Boolean) {
//        getCoBrandCCBalanceAndPointUseCase.response = if (isSuccess) cobrandCCBalanceAndPointDataModelSuccess else coobrandCCBalanceAndPointDataModelFailed
//    }

    fun runTest(test: () -> Unit) {
        launchDefaultFragment()
        test.invoke()
    }

    protected fun launchDefaultFragment() {
        setupHomeAccountUserActivity {
            it.putExtras(Intent(context, HomeAccountUserActivityStub::class.java))
        }
        inflateTestFragment()
    }

    private fun inflateTestFragment() {
//        activity.setupTestFragment(otpComponent)
//        waitForFragmentResumed()
    }

    private fun setupHomeAccountUserActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    companion object {
        lateinit var homeAccountUserComponents: HomeAccountUserComponentsStub
    }
}