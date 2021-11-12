package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub
import org.junit.After
import org.junit.Test

class TopchatRoomOCCTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun should_directly_open_occ_when_click_beli_langsung_in_attached_product() {
        // Given
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT,
            "1160424090"
        )
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_show_toaster_when_click_beli_langsung_in_attached_product_and_fail() {
        // Given
        val expectedErrorMessage = "Oops OCC!"
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartOccMultiUseCase.errorMessage = listOf(expectedErrorMessage)
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        onView(withSubstring(expectedErrorMessage))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_show_toaster_when_click_beli_langsung_in_attached_product_and_error() {
        // Given
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartOccMultiUseCase.isError = true
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        onView(withSubstring("Oops!"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_not_show_button_beli_langsung_in_broadcast() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatAttachmentUseCase.response.chatAttachments.list[3].attributes =
            getModifiedChatAttributes(isVariant = true, hasDiscount = true)
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        withRecyclerView(R.id.rv_product_carousel)
            .atPosition(0)
            .matches(withText(context.getString(R.string.action_buy)))
    }

    @Test
    fun should_not_show_button_beli_langsung_in_broadcast_product_non_variant() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatAttachmentUseCase.response.chatAttachments.list[3].attributes =
            getModifiedChatAttributes(isVariant = false, hasDiscount = true)
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        withRecyclerView(R.id.rv_product_carousel)
            .atPosition(0)
            .matches(withText(context.getString(R.string.action_buy)))
    }

    @Test
    fun should_not_show_button_beli_langsung_in_broadcast_product_non_discount() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatAttachmentUseCase.response.chatAttachments.list[3].attributes =
            getModifiedChatAttributes(isVariant = true, hasDiscount = false)
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        withRecyclerView(R.id.rv_product_carousel)
            .atPosition(0)
            .matches(withText(context.getString(R.string.action_buy)))
    }

    @Test
    fun should_not_show_button_beli_langsung_in_broadcast_product_non_variant_non_discount() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatAttachmentUseCase.response.chatAttachments.list[3].attributes =
            getModifiedChatAttributes(isVariant = false, hasDiscount = false)
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        withRecyclerView(R.id.rv_product_carousel)
            .atPosition(0)
            .matches(withText(context.getString(R.string.action_buy)))
    }

    private fun getModifiedChatAttributes(
        isVariant: Boolean = false,
        hasDiscount: Boolean = false
    ): String {
        return "{\"product_id\":445955139,\"product_profile\":{\"name\":\"L\u0027Oreal Paris Mascara Waterproof Lash Paradise Black\",\"price\":\"Rp 93.000\",\"price_int\":93000,\"image_url\":\"https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/5/12/29240564/29240564_d2a1bb6c-0a2a-44a3-8aee-67ee4b90c31f_1300_1300\",\"url\":\"https://www.tokopedia.com/lorealparis/l-oreal-paris-mascara-waterproof-lash-paradise-black\",\"playstore_product_data\":{\"playstore_status\":\"NORMAL\"},\"price_before\":\"${getPriceBeforeModified(hasDiscount)}\",\"drop_percentage\":\"${getDropPercentageModified(hasDiscount)}\",\"shop_id\":5665147,\"status\":1,\"min_order\":1,\"category_id\":61,\"remaining_stock\":0,\"category_breadcrumb\":\"\",\"list_image_url\":[\"https://ecs7.tokopedia.net/img/cache/700/product-1/2020/5/12/29240564/29240564_d2a1bb6c-0a2a-44a3-8aee-67ee4b90c31f_1300_1300\",\"https://ecs7.tokopedia.net/img/cache/700/attachment/2020/6/10/32520341/32520341_637c6bb1-0422-4c05-84cf-751c50073585.jpg\",\"https://ecs7.tokopedia.net/img/cache/700/product-1/2020/8/16/29240564/29240564_b6e07130-f8d4-4220-9114-ae7bb75c3f33_1300_1300\",\"https://ecs7.tokopedia.net/img/cache/700/product-1/2020/8/16/29240564/29240564_cabc9e63-f092-4e70-8a77-4352a0646660_1300_1300\",\"https://ecs7.tokopedia.net/img/cache/700/product-1/2020/8/27/29240564/29240564_be0e7639-8800-492a-a808-62cbb6093c1f_1080_1080\"],\"variant\":${getVariantModified(isVariant)},\"wishlist\":false,\"free_ongkir\":{\"is_active\":true,\"image_url\":\"https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png\"},\"rating\":{\"rating\":5,\"rating_score\":4.9,\"count\":1944},\"campaign_id\":-10000}}"
    }

    private fun getVariantModified(isVariant: Boolean): String {
        return if (isVariant) {
            "[{\"identifier\":\"\",\"name\":\"\",\"option\":{\"id\":127786096,\"hex\":\"#FFFFFF\",\"value\":\"Putih\"},\"position\":0,\"unit_name\":\"\"},{\"identifier\":\"\",\"name\":\"\",\"option\":{\"id\":127786098,\"hex\":\"\",\"value\":\"S\"},\"position\":0,\"unit_name\":\"\"}]"
        } else "[]"
    }

    private fun getPriceBeforeModified(hasDiscount: Boolean): String {
        return if (hasDiscount) {
            "Rp 155.000"
        } else ""
    }

    private fun getDropPercentageModified(hasDiscount: Boolean): String {
        return if (hasDiscount) {
            "40"
        } else ""
    }

    @After
    fun after() {
        TopChatRoomFragmentStub.isOCCActive = false
    }

}