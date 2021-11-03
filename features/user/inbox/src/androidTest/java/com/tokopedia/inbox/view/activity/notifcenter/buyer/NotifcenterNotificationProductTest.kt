package com.tokopedia.inbox.view.activity.notifcenter.buyer

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.Matchers.not
import org.junit.Test

class NotifcenterNotificationProductTest : InboxNotifcenterTest() {

    @Test
    fun should_open_bottomsheet_when_click_beli_in_attached_product_variants() {
        //Given
        inboxNotifcenterDep.apply {
            val result = notifcenterDetailUseCase.productOnly
            notifcenterDetailUseCase.response = result
        }
        startInboxActivity()
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //When
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(0, R.id.btn_checkout)
        ).perform(ViewActions.click())

        //Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1988298491", "10973651", "notifcenter", "false", "") //Product from JSON
        intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_open_bottomsheet_when_click_keranjang_in_attached_product_variants() {
        //Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.productOnly
        }
        startInboxActivity()
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //When
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(0, R.id.btn_atc)
        ).perform(ViewActions.click())

        //Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1988298491", "10973651", "notifcenter", "false", "") //Product from JSON
        intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_show_toaster_when_user_click_ingatkan_saya() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.productOnly
        }
        startInboxActivity()

        //When
        scrollToProductPosition(1)
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(1, R.id.tv_add_to_wishlist)
        ).perform(ViewActions.click())

        // Then
        onView(withText(context.getString(R.string.title_success_add_to_wishlist)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_show_error_toaster_when_user_click_ingatkan_saya_but_error() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.productOnly
            addWishlistUseCase.isError = true
        }
        startInboxActivity()

        //When
        scrollToProductPosition(1)
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(1, R.id.tv_add_to_wishlist)
        ).perform(ViewActions.click())

        // Then
        onView(withSubstring("Oops!"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_open_wishlist_when_user_click_cek_wishlist() {
        //Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.productOnly
            setupTypeButton(
                productId = "1988288674",
                typeButton = ProductData.BUTTON_TYPE_WISHLIST,
                productOnly = notifcenterDetailUseCase.response
            )
        }
        startInboxActivity()
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //When
        scrollToProductPosition(2)
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(2, R.id.tv_check_wishlist)
        ).perform(ViewActions.click())

        //Then
        intended(IntentMatchers.hasData(ApplinkConst.NEW_WISHLIST))
    }

    @Test
    fun should_show_variant_labels_if_product_variants() {
        //Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response = notifcenterDetailUseCase.productOnly
        }
        startInboxActivity()
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //Then
        onView(withRecyclerView(R.id.rv_carousel_product)
            .atPositionOnView(0, R.id.pvl_variant))
            .check(matches(isDisplayed()))
    }

    @Test
    fun should_not_show_variant_labels_if_product_variants() {
        //Given
        inboxNotifcenterDep.apply {
            val dataResponse = notifcenterDetailUseCase.productOnly.apply {
                this.notifcenterDetail.newList[0].product?.variant = listOf()
            }
            notifcenterDetailUseCase.response = dataResponse
        }
        startInboxActivity()
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        //When
        scrollToProductPosition(1)

        //Then
        onView(withRecyclerView(R.id.rv_carousel_product)
            .atPositionOnView(1, R.id.pvl_variant))
            .check(matches(not(isDisplayed())))
    }

    private fun scrollToProductPosition(position: Int) {
        onView(withId(R.id.rv_carousel_product)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    private fun setupTypeButton(
        productId: String,
        typeButton: Int,
        productOnly: NotifcenterDetailResponse
    ) {
        productOnly.notifcenterDetail.newList.first().productData.forEach {
            if (it.productId == productId) {
                it.typeButton = typeButton
            }
        }
    }
}