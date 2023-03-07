package com.tokopedia.topchat.chatlist.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.withItemCount
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withTotalItem
import com.tokopedia.topchat.stub.common.UserSessionStub
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class ChatListActivityTest : ChatListTest() {

    @Test
    fun empty_chat_list_buyer_only() {
        // Given
        (userSession as UserSessionStub).hasShopStub = false
        chatListUseCase.response = exEmptyChatListPojo

        // When
        startChatListActivity()

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
        (userSession as UserSessionStub).hasShopStub = false
        chatListUseCase.response = exSize2ChatListPojo

        // When
        startChatListActivity()

        // Then
        onView(withId(R.id.recycler_view))
            .check(matches(withTotalItem(2)))
    }

    @Test
    fun empty_chat_list_seller_buyer() {
        // Given
        chatListUseCase.response = exEmptyChatListPojo
        userSession.setIsShopOwner(true)

        // When
        startChatListActivity()

        // Then
        onView(allOf(withId(R.id.thumbnail_empty_chat_list), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(allOf(withId(R.id.title_empty_chat_list), isCompletelyDisplayed()))
            .check(matches(withText("Belum ada chat, nih")))
        onView(allOf(withId(R.id.subtitle), isCompletelyDisplayed()))
            .check(matches(withText("Yuk, bikin tokomu ramai pengunjung dengan beriklan dan promosikan produk-produkmu.")))
        onView(allOf(withId(R.id.btnCta), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(allOf(withId(R.id.btnCta), isCompletelyDisplayed()))
            .check(matches(withText("Coba Iklan dan Promosi")))
    }

    @Test
    fun size_5_chat_list_seller_buyer() {
        // Given
        chatListUseCase.response = exSize5ChatListPojo
        userSession.setIsShopOwner(true)

        // When
        startChatListActivity()

        // Then
        onView(withIndex(withId(R.id.recycler_view), 0))
            .check(matches(withTotalItem(5)))
    }

    @Test
    fun should_show_default_3_filters_when_user_on_seller_tab() {
        // Given
        chatListUseCase.response = exSize5ChatListPojo
        userSession.setIsShopOwner(true)

        // When
        startChatListActivity()
        onView(withText("Toko Rifqi 123")).perform(click())
        onView(withId(R.id.menu_chat_filter)).perform(click())

        // Then
        onView(withId(R.id.rvMenu)).check(withItemCount(equalTo(3)))
    }

    @Test
    fun should_show_4_filters_when_whitelisted_user_on_seller_tab() {
        // Given
        chatListUseCase.response = exSize5ChatListPojo
        chatWhitelistFeatureUseCase.response = ChatWhitelistFeatureResponse().apply {
            this.chatWhitelistFeature.isWhitelist = true
        }
        userSession.setIsShopOwner(true)

        // When
        startChatListActivity()
        onView(withText("Toko Rifqi 123")).perform(click())
        onView(withId(R.id.menu_chat_filter)).perform(click())

        // Then
        onView(withId(R.id.rvMenu)).check(withItemCount(equalTo(4)))
    }

    @Test
    fun should_show_2_filters_when_user_on_buyer_tab() {
        // Given
        chatListUseCase.response = exSize5ChatListPojo
        userSession.setIsShopOwner(true)

        // When
        startChatListActivity()
        onView(withText("Rifqi MF 123")).perform(click())
        onView(withId(R.id.menu_chat_filter)).perform(click())

        // Then
        onView(withId(R.id.rvMenu)).check(withItemCount(equalTo(2)))
    }
}
