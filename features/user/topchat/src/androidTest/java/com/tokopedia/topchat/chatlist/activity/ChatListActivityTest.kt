package com.tokopedia.topchat.chatlist.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.Matchers.not
import org.junit.Test

class ChatListActivityTest: ChatListTest() {

    @Test
    fun empty_chat_list_buyer_only() {
        // Given
        userSession.hasShopStub = false
        chatListUseCase.response = exEmptyChatListPojo

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)

        // Then
        onView(withId(R.id.thumbnail_empty_chat_list))
                .check(matches(isDisplayed()))
        onView(withId(R.id.title_empty_chat_list))
                .check(matches(withText("Belum ada chat, nih")))
        onView(withId(R.id.subtitle))
                .check(matches(withText("Coba ngobrol dengan teman penjual, yuk!")))
        onView(withId(R.id.btnCta))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun size_2_chat_list_buyer_only() {
        // Given
        userSession.hasShopStub = false
        chatListUseCase.response = exSize2ChatListPojo

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)

        // Then
        onView(withId(R.id.recycler_view))
                .check(matches(withTotalItem(2)))
    }

    @Test
    fun empty_chat_list_seller_buyer() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = "Toko Rifqi"
        userSession.nameStub = "Rifqi MF"
        chatListUseCase.response = exEmptyChatListPojo
        userSession.setIsShopOwner(true)

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)

        // Then
        onView(withId(R.id.thumbnail_empty_chat_list))
                .check(matches(isDisplayed()))
        onView(withId(R.id.title_empty_chat_list))
                .check(matches(withText("Belum ada chat, nih")))
        onView(withId(R.id.subtitle))
                .check(matches(withText("Yuk, bikin tokomu ramai pengunjung dengan beriklan dan promosikan produk-produkmu.")))
        onView(withId(R.id.btnCta))
                .check(matches(isDisplayed()))
        onView(withId(R.id.btnCta))
                .check(matches(withText("Coba Iklan dan Promosi")))
    }

    @Test
    fun size_5_chat_list_seller_buyer() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = "Toko Rifqi 123"
        userSession.nameStub = "Rifqi MF 123"
        chatListUseCase.response = exSize5ChatListPojo
        userSession.setIsShopOwner(true)

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)

        // Then
        onView(withIndex(withId(R.id.recycler_view), 0))
                .check(matches(withTotalItem(5)))
    }
}
