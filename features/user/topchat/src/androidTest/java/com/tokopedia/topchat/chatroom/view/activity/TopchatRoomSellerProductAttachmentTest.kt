package com.tokopedia.topchat.chatroom.view.activity

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.matchers.withLinearLayoutGravity
import com.tokopedia.topchat.matchers.withRecyclerView
import org.junit.Test

class TopchatRoomSellerProductAttachmentTest : BaseSellerTopchatRoomTest() {

    @Test
    fun assert_product_card_gravity() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        inflateTestFragment()

        // Then
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.containerProductAttachment
                )
        ).check(matches(withLinearLayoutGravity(Gravity.END)))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        3, R.id.containerProductAttachment
                )
        ).check(matches(withLinearLayoutGravity(Gravity.START)))
    }
}