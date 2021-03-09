package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.assertion.hasTotalItemOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class TopchatRoomBuyerAttachProductTest : TopchatRoomTest() {

    lateinit var productPreview: ProductPreview
    private val productName = "Testing Attach Product 1"
    private val productThumbnail = "https://ecs7-p.tokopedia.net/img/cache/350/attachment/2020/8/24/40768394/40768394_732546f9-371d-45c6-a412-451ea50aa22c.jpg.webp"

    @ExperimentalCoroutinesApi
    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
                "1111",
                productThumbnail,
                productName,
                "Rp 23.000.000",
                "",
                "",
                "",
                "",
                "",
                "tokopedia://product/1111",
                false,
                "",
                "Rp 50.000.000",
                500000,
                "50%",
                false
        )
    }

    @Test
    fun user_can_see_preview_product_before_attach_product() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(hasTotalItemOf(1))
    }

    @Test
    fun user_can_not_sent_preview_product_when_text_is_empty() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        clickSendBtn()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(hasTotalItemOf(1))
    }

    @Test
    fun user_preview_product_from_attach_product_page() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(1)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(hasTotalItemOf(1))
    }

    @Test
    fun user_reattach_product_from_preview_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(1)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(2)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(3)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()
        onView(withId(R.id.rv_attachment_preview)).perform(
                scrollToPosition<RecyclerView.ViewHolder>(2)
        )

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(hasTotalItemOf(3))
    }

    @Test
    fun user_remove_preview_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(1)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()
        clickCloseAttachmentPreview(0)

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun user_remove_two_out_of_three_preview_products() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()

        // When
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(3)
                        )
                )
        clickPlusIconMenu()
        clickAttachProductMenu()
        clickCloseAttachmentPreview(0)
        clickCloseAttachmentPreview(1)

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(hasTotalItemOf(1))
    }

    private fun clickCloseAttachmentPreview(position: Int) {
        val viewAction = RecyclerViewActions
                .actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                        position,
                        ClickChildViewWithIdAction()
                                .clickChildViewWithId(R.id.iv_close)
                )
        onView(withId(R.id.rv_attachment_preview)).perform(viewAction)
    }

    private fun getAttachProductData(totalProduct: Int): Intent {
        val products = ArrayList<ResultProduct>(totalProduct)
        for (i in 0 until totalProduct) {
            products.add(
                    ResultProduct(
                            "11111",
                            "tokopedia://product/1111",
                            productThumbnail,
                            "Rp ${i + 1}5.000.000",
                            "${i + 1} $productName"
                    )
            )
        }
        return Intent().apply {
            putParcelableArrayListExtra(
                    TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY, products
            )
        }
    }

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

}