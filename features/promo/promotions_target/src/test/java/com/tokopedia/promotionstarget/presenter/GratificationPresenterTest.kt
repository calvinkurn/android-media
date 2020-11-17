package com.tokopedia.promotionstarget.presenter

import android.app.Activity
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.tokopedia.notifications.inApp.CmDialogVisibilityContract
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.CmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.components.DaggerTestCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.components.componentProvider.CmGratifiPresenterComponentProvider
import com.tokopedia.promotionstarget.data.di.modules.TestAppModule
import com.tokopedia.promotionstarget.data.gql.GraphqlHelper
import com.tokopedia.promotionstarget.data.notification.GratifNotificationResponse
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.promotionstarget.domain.usecase.NotificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.TokopointsCouponDetailUseCase
import io.mockk.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.json.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.File
import java.lang.ref.WeakReference

/*
* Scenarios/Flows to test
* 1. When gratif api is down    (type = ORGANIC and PUSH)   DONE
* 2. When coupon api is down    (type = ORGANIC and PUSH)   DONE
* 3. When a pop-up is already visible   (type = ORGANIC and PUSH)
* 4. When a coupon Code is empty        (type = ORGANIC and PUSH)
* 5. When api is taking more time than 1 second (only for ThankYou page)
* 6. Happy flow = Gratif Api is up, Coupon Api is up and no dialog is visible   (type = ORGANIC and PUSH)
* 7. Happy flow = Gratif Api is up, Coupon Api is up, no dialog is visible and api is taking less than 1 second (only for ThankYou page)
* */
class GratificationPresenterTest {

    val context: Context = mockk()
    val activity: Activity = mockk()
    val dispatcher = TestCoroutineDispatcher()
    val notificationUseCase: NotificationUseCase = mockk()
    val tpCouponDetailUseCase: TokopointsCouponDetailUseCase = mockk()
    lateinit var notificationResponse: GratifNotificationResponse
    lateinit var couponResponse: TokopointsCouponDetailResponse
    val gson = Gson()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_gratif_notification) } returns ""
        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_hachiko_coupon) } returns ""
    }

    @Test(expected = Exception::class)
    fun testWhenGetNotificationIsDownTypePush() {
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH, true, false)

        coVerifyOrder {

            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            gratificationPresenter.notificationUseCase.getQueryParams(any(), any(), any())
            gratificationPresenter.notificationUseCase.getResponse(any())
            gratificationPresenter.dialogVisibilityContract?.onDialogDismiss(activity)
            gratificationPresenter.exceptionCallback?.onError(any())
        }
    }

    @Test
    fun testWhenGetNotificationIsDownTypeOrganic() {
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, true, false)

        coVerifyOrder {

            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.notificationUseCase.getQueryParams(any(), any(), any())
            gratificationPresenter.notificationUseCase.getResponse(any())
        }
        verify(exactly = 0) { gratificationPresenter.dialogVisibilityContract?.onDialogDismiss(activity) }
    }

    @Test(expected = Exception::class)
    fun testWhenGetHachikoCouponIsDownTypePush() {
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH, false, true)

        coVerify {

            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            gratificationPresenter.notificationUseCase.getQueryParams(any(), any(), any())
            gratificationPresenter.notificationUseCase.getResponse(any())
            gratificationPresenter.tpCouponDetailUseCase.getQueryParams(any())
            gratificationPresenter.tpCouponDetailUseCase.getResponse(any())
            gratificationPresenter.dialogVisibilityContract?.onDialogDismiss(activity)
            gratificationPresenter.exceptionCallback?.onError(any())
        }
    }

    @Test
    fun testWhenGetHachikoCouponIsDownTypeOrganic() {
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, false, true)

        coVerify {
            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.notificationUseCase.getQueryParams(any(), any(), any())
            gratificationPresenter.notificationUseCase.getResponse(any())
            gratificationPresenter.tpCouponDetailUseCase.getQueryParams(any())
            gratificationPresenter.tpCouponDetailUseCase.getResponse(any())
        }
        verify(exactly = 0) { gratificationPresenter.dialogVisibilityContract?.onDialogDismiss(activity) }
    }

    @Test
    fun testShowGratificationInAppTypeOrganic() {
        val bottomSheetDialog: BottomSheetDialog = mockk()
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, false, false, bottomSheetDialog)

        verify {

            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any())
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            bottomSheetDialog.setOnDismissListener(any())
            bottomSheetDialog.setOnCancelListener(any())
        }
    }

    @Test
    fun testShowGratificationInAppTypePush() {
        val bottomSheetDialog: BottomSheetDialog = mockk()
        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH, false, false, bottomSheetDialog)

        verify {

            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any())
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            bottomSheetDialog.setOnDismissListener(any())
            bottomSheetDialog.setOnCancelListener(any())
        }
    }

    @Test
    fun testForDialogIsAlreadyVisibleTypeOrganic() {
        val gratifCallback: GratificationPresenter.GratifPopupCallback = spyk()
        val visibilityContract: CmDialogVisibilityContract = spyk()
        every { visibilityContract.isDialogVisible(activity) } returns true

        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC,
                false,
                false,
                gratifCallback = gratifCallback,
                visibilityContract = visibilityContract)

        verifyOrder {
            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratifCallback.onIgnored(GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE)
        }
    }

    @Test
    fun testForDialogIsAlreadyVisibleTypePush() {
        val bottomSheetDialog: BottomSheetDialog = mockk()
        val gratifCallback: GratificationPresenter.GratifPopupCallback = spyk()
        val visibilityContract: CmDialogVisibilityContract = spyk()
        every { visibilityContract.isDialogVisible(activity) } returns true

        val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH,
                false,
                false,
                bottomSheetDialog = bottomSheetDialog,
                gratifCallback = gratifCallback,
                visibilityContract = visibilityContract)

        coVerify {
            gratificationPresenter.worker = dispatcher
            gratificationPresenter.uiWorker = dispatcher
            gratificationPresenter.notificationUseCase = notificationUseCase
            gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

            gratificationPresenter.initSafeJob()
            gratificationPresenter.initSafeScope()
            gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            gratificationPresenter.notificationUseCase.getQueryParams(any(), any(), any())
            gratificationPresenter.notificationUseCase.getResponse(any())
            gratificationPresenter.tpCouponDetailUseCase.getQueryParams(any())
            gratificationPresenter.tpCouponDetailUseCase.getResponse(any())
            gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any())
            gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
            bottomSheetDialog.setOnDismissListener(any())
            bottomSheetDialog.setOnCancelListener(any())
        }
    }


    private fun setupApiResponse(isNotificationApiDown: Boolean = false, isCouponApiDown: Boolean = false) {

        val requestParams: HashMap<String, Any> = mockk()
        every { notificationUseCase.getQueryParams(any(), any(), any()) } returns requestParams
        notificationResponse = getMockResponse("t_promo_get_notification", GratifNotificationResponse::class.java)
        if (isNotificationApiDown) {
            coEvery { notificationUseCase.getResponse(requestParams) } throws Exception("Api is down")
        } else {
            coEvery { notificationUseCase.getResponse(requestParams) } returns notificationResponse
        }

        every { tpCouponDetailUseCase.getQueryParams(any()) } returns requestParams
        couponResponse = getMockResponse("t_promo_get_hachiko_coupon", TokopointsCouponDetailResponse::class.java)
        if (isCouponApiDown) {
            coEvery { tpCouponDetailUseCase.getResponse(requestParams) } throws Exception("Api is down")
        } else {
            coEvery { tpCouponDetailUseCase.getResponse(requestParams) } returns couponResponse
        }

    }

    private fun buildGratifPresenter(visibilityContract: CmDialogVisibilityContract): GratificationPresenter {
        val componentProvider = object : CmGratifiPresenterComponentProvider() {
            override fun getComponent(context: Context): CmGratificationPresenterComponent {
                return DaggerTestCmGratificationPresenterComponent.builder()
                        .testAppModule(TestAppModule(context))
                        .build()
            }
        }
        val gratificationPresenter: GratificationPresenter = GratificationPresenter(context, componentProvider)
        gratificationPresenter.dialogVisibilityContract = visibilityContract
        gratificationPresenter.worker = dispatcher
        gratificationPresenter.uiWorker = dispatcher
        gratificationPresenter.notificationUseCase = notificationUseCase
        gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase
        val exceptionCallback: GratificationPresenter.ExceptionCallback = object : GratificationPresenter.ExceptionCallback {
            override fun onError(th: Throwable?) {
                //Do nothing
            }
        }
        gratificationPresenter.exceptionCallback = exceptionCallback
        return gratificationPresenter
    }

    private fun preparePresenterForApiDown(@NotificationEntryType notificationEntryType: Int,
                                           isNotificationApiDown: Boolean,
                                           isCouponApiDown: Boolean,
                                           bottomSheetDialog: BottomSheetDialog = mockk(),
                                           gratifCallback: GratificationPresenter.GratifPopupCallback = spyk(),
                                           visibilityContract: CmDialogVisibilityContract = spyk {
                                               every { isDialogVisible(any()) } returns false
                                           }): GratificationPresenter {
        setupApiResponse(isNotificationApiDown, isCouponApiDown)

        val gratificationPresenter = buildGratifPresenter(visibilityContract)

        val weakActivity: WeakReference<Activity>? = WeakReference(activity)

        val gratificationId = "1"
        val screenName = javaClass.name
        val paymentID = 0L
        val timeout = 0L
        val closeCurrentActivity = true

        every {
            gratificationPresenter.dialogCreator.createGratifDialog(any(),
                    notificationResponse.response!!, couponResponse,
                    notificationEntryType, gratifCallback, screenName, closeCurrentActivity)
        } returns bottomSheetDialog

        every { bottomSheetDialog.setOnDismissListener(any()) } just runs
        every { bottomSheetDialog.setOnCancelListener(any()) } just runs

        gratificationPresenter.showGratificationInApp(weakActivity,
                gratificationId,
                notificationEntryType,
                gratifCallback,
                screenName,
                paymentID,
                timeout,
                closeCurrentActivity)
        return gratificationPresenter
    }

    private fun <T> getMockResponse(fileName: String, clazz: Class<T>): T {
        val jsonFile = File(javaClass.getResource("/assets/$fileName.json")!!.path)
        return gson.fromJson(JSONObject(jsonFile.readText()).getJSONObject("data").toString(), clazz)
    }
}