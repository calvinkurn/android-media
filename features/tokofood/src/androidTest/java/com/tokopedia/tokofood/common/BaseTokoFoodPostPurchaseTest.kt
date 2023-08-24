package com.tokopedia.tokofood.common

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.AndroidTestUtil
import com.tokopedia.tokofood.stub.common.util.TokoFoodOrderTrackingComponentStubInstance
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import com.tokopedia.tokofood.stub.common.util.onClick
import com.tokopedia.tokofood.stub.common.util.onIdView
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.DriverPhoneNumberMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderDetailMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderStatusMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetDriverPhoneNumberUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderDetailUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderStatusUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetUnreadChatCountUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.TokoChatConfigGroupBookingUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.presentation.activity.TokoFoodOrderTrackingActivityStub
import com.tokopedia.unifycomponents.R
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseTokoFoodPostPurchaseTest {

    @get:Rule
    var activityRule = IntentsTestRule(TokoFoodOrderTrackingActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var userSessionStub: UserSessionStub
    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub
    protected lateinit var getDriverPhoneNumberUseCaseStub: GetDriverPhoneNumberUseCaseStub
    protected lateinit var getTokoFoodOrderDetailUseCaseStub: GetTokoFoodOrderDetailUseCaseStub
    protected lateinit var getTokoFoodOrderStatusUseCaseStub: GetTokoFoodOrderStatusUseCaseStub
    protected lateinit var getUnreadChatCountUseCaseStub: GetUnreadChatCountUseCaseStub
    protected lateinit var tokoChatConfigGroupBookingUseCaseStub: TokoChatConfigGroupBookingUseCaseStub
    protected lateinit var driverPhoneNumberMapperStub: DriverPhoneNumberMapperStub
    protected lateinit var tokoFoodOrderDetailMapperStub: TokoFoodOrderDetailMapperStub
    protected lateinit var tokofoodOrderStatusMapperStub: TokoFoodOrderStatusMapperStub
    protected lateinit var remoteConfig: RemoteConfig

    protected val applicationContext: Context = ApplicationProvider.getApplicationContext()
    protected val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext


    protected val getTokoFoodOrderTrackingComponentStub by lazy {
        TokoFoodOrderTrackingComponentStubInstance.getTokoFoodOrderTrackingComponentStub(
            applicationContext
        )
    }

    protected val driverPhoneNumberResponse: DriverPhoneNumberResponse
        get() = AndroidTestUtil.parse(
            "raw/postpurchase/tokofood_post_purchase_driver_phone_number.json",
            DriverPhoneNumberResponse::class.java
        )!!

    protected val orderCompletedResponse: TokoFoodOrderDetailResponse
        get() = AndroidTestUtil.parse(
            "raw/postpurchase/tokofood_post_purchase_order_completed.json",
            TokoFoodOrderDetailResponse::class.java
        )!!

    protected val liveTrackingResponse: TokoFoodOrderDetailResponse
        get() = AndroidTestUtil.parse(
            "raw/postpurchase/tokofood_post_purchase_order_otw_pickup.json",
            TokoFoodOrderDetailResponse::class.java
        )!!


    @Before
    open fun setup() {
        graphqlRepositoryStub =
            getTokoFoodOrderTrackingComponentStub.graphqlRepository() as GraphqlRepositoryStub
        userSessionStub =
            getTokoFoodOrderTrackingComponentStub.userSessionInterface() as UserSessionStub
        getDriverPhoneNumberUseCaseStub =
            getTokoFoodOrderTrackingComponentStub.getDriverPhoneNumberUseCaseStub() as GetDriverPhoneNumberUseCaseStub
        getTokoFoodOrderDetailUseCaseStub =
            getTokoFoodOrderTrackingComponentStub.getTokoFoodOrderDetailUseCaseStub() as GetTokoFoodOrderDetailUseCaseStub
        getTokoFoodOrderStatusUseCaseStub =
            getTokoFoodOrderTrackingComponentStub.getTokoFoodOrderStatusUseCaseStub() as GetTokoFoodOrderStatusUseCaseStub
        getUnreadChatCountUseCaseStub =
            getTokoFoodOrderTrackingComponentStub.getUnreadChatCountUseCaseStub() as GetUnreadChatCountUseCaseStub
        tokoChatConfigGroupBookingUseCaseStub =
            getTokoFoodOrderTrackingComponentStub.getTokoChatConfigGroupBookingUseCaseStub() as TokoChatConfigGroupBookingUseCaseStub
        driverPhoneNumberMapperStub =
            getTokoFoodOrderTrackingComponentStub.driverPhoneNumberMapperStub() as DriverPhoneNumberMapperStub
        tokoFoodOrderDetailMapperStub =
            getTokoFoodOrderTrackingComponentStub.tokoFoodOrderDetailMapperStub() as TokoFoodOrderDetailMapperStub
        tokofoodOrderStatusMapperStub =
            getTokoFoodOrderTrackingComponentStub.tokoFoodOrderStatusMapperStub() as TokoFoodOrderStatusMapperStub
        remoteConfig = getTokoFoodOrderTrackingComponentStub.getRemoteConfig()
    }

    @After
    open fun finish() {
        graphqlRepositoryStub.clearMocks()
    }

    protected fun getTokoFoodOrderTrackingActivityIntent(): Intent {
        return TokoFoodOrderTrackingActivityStub.createIntent(targetContext)
    }

    protected fun launchActivity() {
        activityRule.launchActivity(getTokoFoodOrderTrackingActivityIntent())
    }

    protected fun closeBottomSheet() {
        onIdView(com.tokopedia.unifycomponents.R.id.bottom_sheet_close).onClick()
    }

    protected fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    protected fun dismissPage() {
        activityRule.activity.finish()
    }
}
