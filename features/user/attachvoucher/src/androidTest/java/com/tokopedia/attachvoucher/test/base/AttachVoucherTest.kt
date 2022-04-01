package com.tokopedia.attachvoucher.test.base

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.attachvoucher.common.AndroidFileUtil
import com.tokopedia.attachvoucher.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.attachvoucher.common.di.module.FakeAppModule
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.stub.di.AttachVoucherComponentStub
import com.tokopedia.attachvoucher.stub.di.DaggerAttachVoucherComponentStub
import com.tokopedia.attachvoucher.stub.usecase.GetVoucherUseCaseStub
import com.tokopedia.attachvoucher.stub.view.AttachVoucherActivityStub
import com.tokopedia.attachvoucher.test.R
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class AttachVoucherTest {
    @get:Rule
    var activityTestRule = IntentsTestRule(
        AttachVoucherActivityStub::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext
    private lateinit var activity: AttachVoucherActivityStub

    @Inject
    lateinit var getVoucherUseCase: GetVoucherUseCaseStub

    protected var errorMessageResponse = "Oops!"
    protected var successGetMerchantPromotionGetMVListResponse = GetMerchantPromotionGetMVListResponse()

    @Before
    fun before() {
        setupDaggerComponent()
        setupDefaultResponse()
        getAllDataResponse()
    }

    private fun setupDaggerComponent() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        attachVoucherComponentStub = DaggerAttachVoucherComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .build()
        attachVoucherComponentStub!!.inject(this)
    }

    private fun setupDefaultResponse() {
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
    }

    private fun getAllDataResponse() {
        successGetMerchantPromotionGetMVListResponse = AndroidFileUtil.parse(
            "success_get_voucher_list.json", GetMerchantPromotionGetMVListResponse::class.java
        )
    }

    protected fun launchAttachVoucherActivity() {
        val intent = Intent()
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    protected fun scrollListToPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    protected fun checkViewNotDisplayed(@IdRes viewId: Int) {
        onView(withId(viewId)).check(matches(not(isDisplayed())))
    }

    protected fun checkViewWithMessageDisplayed(msg: String) {
        onView(withSubstring(msg)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @After
    open fun tearDown() {
        attachVoucherComponentStub = null
    }

    companion object {
        var attachVoucherComponentStub: AttachVoucherComponentStub? = null
    }
}