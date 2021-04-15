package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.hasQuestion
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.matchers.withTotalItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class TopchatRoomBuyerAttachProductTest : BaseBuyerTopchatRoomTest() {

    lateinit var productPreview: ProductPreview

    @ExperimentalCoroutinesApi
    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
                "1111",
                ProductPreviewAttribute.productThumbnail,
                ProductPreviewAttribute.productName,
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
                .check(matches(withTotalItem(1)))
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
                .check(matches(withTotalItem(1)))
    }

    @Test
    fun user_preview_product_from_attach_product_page() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(1)
                        )
                )

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(matches(withTotalItem(1)))
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
                .check(matches(withTotalItem(3)))
    }

    @Test
    fun user_remove_preview_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        inflateTestFragment()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(1)
                        )
                )

        // When
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
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
                .respondWith(
                        Instrumentation.ActivityResult(
                                Activity.RESULT_OK, getAttachProductData(3)
                        )
                )

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()
        clickCloseAttachmentPreview(0)
        clickCloseAttachmentPreview(1)

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
                .check(matches(withTotalItem(1)))
    }

    @Test
    fun srw_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()

        // Then
        assertSrwContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(1)
    }

    @Test
    fun srw_displayed_if_buyer_attach_from_attach_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwContentIsVisible()
        assertSrwTitle(chatSrwResponse.chatSmartReplyQuestion.title)
        assertSrwTotalQuestion(1)
    }

    @Test
    fun srw_no_question_not_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        inflateTestFragment()

        // Then
        assertTemplateChatVisibility(isDisplayed())
        assertSrwContentIsHidden()
    }

    @Test
    fun srw_no_question_not_displayed_if_buyer_attach_from_attach_product() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse.hasQuestion(false)
        inflateTestFragment()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertTemplateChatVisibility(isDisplayed())
        assertSrwContentIsHidden()
    }

    @Test
    fun srw_loading_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.setResponseWithDelay(
                chatSrwResponse, 1500
        )
        inflateTestFragment()

        // Then
        assertTemplateChatVisibility(not(isDisplayed()))
        assertSrwContentIsLoading()
    }

    @Test
    fun srw_error_state_displayed_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        inflateTestFragment()

        // Then
        assertSrwContentIsError()
    }

    @Test
    fun load_srw_from_error_state_if_buyer_attach_from_start_intent() {
        // Given
        setupChatRoomActivity {
            putProductAttachmentIntent(it)
        }
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.isError = true
        inflateTestFragment()

        // When
        chatSrwUseCase.isError = false
        chatSrwUseCase.response = chatSrwResponse
        onView(withId(com.tokopedia.unifycomponents.R.id.refreshID))
                .perform(click())

        // Then
        assertSrwContentIsVisible()
    }

    // TODO: test srw interaction expand and collapse
    // TODO: test srw interaction click and send msg

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

}
