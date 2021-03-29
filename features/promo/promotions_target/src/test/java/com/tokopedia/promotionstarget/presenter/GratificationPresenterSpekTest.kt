package com.tokopedia.promotionstarget.presenter

import android.app.Activity
import android.content.Context
import android.os.Handler
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.json.JSONObject
import org.junit.Assert
import org.spekframework.spek2.Spek
import java.io.File
import java.lang.ref.WeakReference
import kotlin.test.assertFailsWith


/*
* Scenarios/Flows to test
* 1. When gratif api is down    (type = ORGANIC and PUSH)   DONE
* 2. When coupon api is down    (type = ORGANIC and PUSH)   DONE
* 3. When a pop-up is already visible   (type = ORGANIC and PUSH)   DONE
* 4. When a coupon Code is empty        (type = ORGANIC and PUSH)
* 5. When api is taking more time than 1 second (only for ThankYou page)    DONE
* 6. Happy flow = Gratif Api is up, Coupon Api is up and no dialog is not visible   (type = ORGANIC and PUSH)   DONE
* */

@ExperimentalCoroutinesApi
class GratificationPresenterSpekTest : Spek({

    val gson = Gson()
    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var dispatcher: TestCoroutineDispatcher
    lateinit var notificationUseCase: NotificationUseCase
    lateinit var tpCouponDetailUseCase: TokopointsCouponDetailUseCase
    lateinit var notificationResponse: GratifNotificationResponse
    lateinit var couponResponse: TokopointsCouponDetailResponse
    lateinit var visibilityContract: CmDialogVisibilityContract

    beforeGroup {
        context = mockk()

        val slotRunnable = slot<Runnable>()
        mockkConstructor(Handler::class)
        every { anyConstructed<Handler>().postAtFrontOfQueue(capture(slotRunnable)) } answers {
            slotRunnable.captured.run()
            true
        }

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_gratif_notification) } returns ""
        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_hachiko_coupon) } returns ""
    }

    beforeEachTest {

        activity = mockk()
        dispatcher = TestCoroutineDispatcher()
        notificationUseCase = mockk()
        tpCouponDetailUseCase = mockk()
        visibilityContract = mockk()
    }


    fun <T> getMockResponse(fileName: String, clazz: Class<T>): T {
        val jsonFile = File(javaClass.getResource("/assets/$fileName.json")!!.path)
        return gson.fromJson(JSONObject(jsonFile.readText()).getJSONObject("data").toString(), clazz)
    }

    fun setupApiResponse(dispatcher: TestCoroutineDispatcher,
                         timeout: Long = 0L,
                         isNotificationApiDown: Boolean = false,
                         isCouponApiDown: Boolean = false,
                         isCouponCodeEmpty: Boolean = false) {

        val requestParams: HashMap<String, Any> = mockk()
        every { notificationUseCase.getQueryParams(any(), any(), any()) } returns requestParams
        if (isCouponCodeEmpty) {
            notificationResponse = getMockResponse("t_promo_get_notification_empty_promo_code", GratifNotificationResponse::class.java)
        } else {
            notificationResponse = getMockResponse("t_promo_get_notification", GratifNotificationResponse::class.java)
        }

        if (isNotificationApiDown) {
            coEvery { notificationUseCase.getResponse(requestParams) } throws Exception("Api is down")
        } else {
            coEvery { notificationUseCase.getResponse(requestParams) }.coAnswers {
                if (timeout > 0L) {
                    dispatcher.advanceTimeBy(timeout + 1000L)
                }
                notificationResponse
            }
        }

        every { tpCouponDetailUseCase.getQueryParams(any()) } returns requestParams
        couponResponse = getMockResponse("t_promo_get_hachiko_coupon", TokopointsCouponDetailResponse::class.java)
        if (isCouponApiDown) {
            coEvery { tpCouponDetailUseCase.getResponse(requestParams) } throws Exception("Api is down")
        } else {
            coEvery { tpCouponDetailUseCase.getResponse(requestParams) } returns couponResponse
        }
    }

    fun buildGratifPresenter(visibilityContract: CmDialogVisibilityContract): GratificationPresenter {
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

    fun preparePresenterForApiDown(@NotificationEntryType notificationEntryType: Int,
                                   isNotificationApiDown: Boolean,
                                   isCouponApiDown: Boolean,
                                   bottomSheetDialog: BottomSheetDialog = mockk(),
                                   gratifCallback: GratificationPresenter.GratifPopupCallback = spyk(),
                                   visibilityContract: CmDialogVisibilityContract,
                                   timeout: Long = 0L,
                                   paymentId: Long = 0L,
                                   isCouponCodeEmpty: Boolean = false): GratificationPresenter {
        val gratificationPresenter = buildGratifPresenter(visibilityContract)

        setupApiResponse(dispatcher = gratificationPresenter.worker as TestCoroutineDispatcher,
                timeout = timeout,
                isNotificationApiDown = isNotificationApiDown,
                isCouponApiDown = isCouponApiDown,
                isCouponCodeEmpty = isCouponCodeEmpty)

        val weakActivity: WeakReference<Activity>? = WeakReference(activity)

        val gratificationId = "1"
        val screenName = javaClass.name
        val closeCurrentActivity = true

        every {
            gratificationPresenter.dialogCreator.createGratifDialog(any(),
                    notificationResponse.response!!, couponResponse,
                    notificationEntryType, gratifCallback, screenName, closeCurrentActivity,any())
        } returns bottomSheetDialog

        every { bottomSheetDialog.setOnDismissListener(any()) } just runs
        every { bottomSheetDialog.setOnCancelListener(any()) } just runs

        gratificationPresenter.showGratificationInApp(weakActivity,
                gratificationId,
                notificationEntryType,
                gratifCallback,
                screenName,
                paymentId,
                timeout,
                closeCurrentActivity)
        return gratificationPresenter
    }


    group("When gratif api is down - fail") {

        test("type organic") {

            every { visibilityContract.isDialogVisible(activity) } returns false

            val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, true, false, visibilityContract = visibilityContract)

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

        test("type push") {
            assertFailsWith(Exception::class) {
                val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH, true, false, visibilityContract = visibilityContract)

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
        }
    }

    group("When coupon api is down - fail") {
        test("type push") {
            assertFailsWith(Exception::class) {
                val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH, false, true, visibilityContract = visibilityContract)

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
        }

        test("type organic") {
            every { visibilityContract.isDialogVisible(activity) } returns false

            val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, false, true, visibilityContract = visibilityContract)

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
    }

    group("show gratification dialog - success") {

        test("type push") {
            every { visibilityContract.isDialogVisible(activity) } returns false
            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

            val bottomSheetDialog: BottomSheetDialog = mockk()
            val gratificationPresenter = preparePresenterForApiDown(notificationEntryType = NotificationEntryType.PUSH,isNotificationApiDown =  false,isCouponApiDown =  false,bottomSheetDialog= bottomSheetDialog, visibilityContract = visibilityContract)

            Assert.assertEquals(gratificationPresenter.worker,dispatcher)
            Assert.assertEquals(gratificationPresenter.uiWorker,dispatcher)
            Assert.assertEquals(gratificationPresenter.notificationUseCase,notificationUseCase)
            Assert.assertEquals(gratificationPresenter.tpCouponDetailUseCase,tpCouponDetailUseCase)
            verifyOrder {
                gratificationPresenter.initSafeJob()
                gratificationPresenter.initSafeScope()
                gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
                gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
                gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any(),any())
                gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
                bottomSheetDialog.setOnDismissListener(any())
                bottomSheetDialog.setOnCancelListener(any())
            }
        }

        test("type organic") {

            every { visibilityContract.isDialogVisible(activity) } returns false
            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

            val bottomSheetDialog: BottomSheetDialog = mockk()
            val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC, false, false, bottomSheetDialog, visibilityContract = visibilityContract)

            verify {

                gratificationPresenter.worker = dispatcher
                gratificationPresenter.uiWorker = dispatcher
                gratificationPresenter.notificationUseCase = notificationUseCase
                gratificationPresenter.tpCouponDetailUseCase = tpCouponDetailUseCase

                gratificationPresenter.initSafeJob()
                gratificationPresenter.initSafeScope()
                gratificationPresenter.dialogVisibilityContract?.isDialogVisible(activity)
                gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any(),any())
                gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
                bottomSheetDialog.setOnDismissListener(any())
                bottomSheetDialog.setOnCancelListener(any())
            }
        }
    }

    group("when dialog is already visible") {

        test("type organic - fail") {
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
        test("type push - success") {

            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

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
                gratificationPresenter.dialogCreator.createGratifDialog(any(), any(), any(), any(), any(), any(), any(),any())
                gratificationPresenter.dialogVisibilityContract?.onDialogShown(activity)
                bottomSheetDialog.setOnDismissListener(any())
                bottomSheetDialog.setOnCancelListener(any())
            }
        }
    }

    group("when api is taking more than 1 second - fail") {

        test("thank you page") {
            every { visibilityContract.isDialogVisible(activity) } returns false
            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

            val gratifCallback: GratificationPresenter.GratifPopupCallback = spyk()
            val bottomSheetDialog: BottomSheetDialog = mockk()
            preparePresenterForApiDown(NotificationEntryType.ORGANIC,
                    isNotificationApiDown = false,
                    isCouponApiDown = false,
                    bottomSheetDialog = bottomSheetDialog,
                    timeout = 2000L,
                    gratifCallback = gratifCallback,
                    paymentId = 1L,
                    visibilityContract = visibilityContract)

            verify {
                gratifCallback.onIgnored(GratifPopupIngoreType.TIMEOUT)
            }
        }
    }

    group("when coupon code is empty - fail") {

        test("type push") {
            every { visibilityContract.isDialogVisible(activity) } returns false
            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

            val gratifCallback: GratificationPresenter.GratifPopupCallback = spyk()
            val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.PUSH,
                    isNotificationApiDown = false,
                    isCouponApiDown = false,
                    isCouponCodeEmpty = true,
                    gratifCallback = gratifCallback,
                    visibilityContract = visibilityContract)

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
                gratifCallback.onIgnored(GratifPopupIngoreType.COUPON_CODE_EMPTY)

            }
        }

        test("type organic") {
            every { visibilityContract.isDialogVisible(activity) } returns false
            every { visibilityContract.onDialogShown(activity) } just runs
            every { activity.isFinishing } returns false

            val gratifCallback: GratificationPresenter.GratifPopupCallback = spyk()
            val gratificationPresenter = preparePresenterForApiDown(NotificationEntryType.ORGANIC,
                    isNotificationApiDown = false,
                    isCouponApiDown = false,
                    isCouponCodeEmpty = true,
                    gratifCallback = gratifCallback,
                    visibilityContract = visibilityContract)

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
                gratifCallback.onIgnored(GratifPopupIngoreType.COUPON_CODE_EMPTY)
            }
        }
    }

    afterEachTest {
        dispatcher.cleanupTestCoroutines()
    }
})