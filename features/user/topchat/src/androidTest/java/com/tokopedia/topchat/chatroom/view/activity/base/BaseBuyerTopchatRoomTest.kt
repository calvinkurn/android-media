package com.tokopedia.topchat.chatroom.view.activity.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.matchers.isExpanded
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not

open class BaseBuyerTopchatRoomTest : TopchatRoomTest() {

    protected var chatSrwResponseMultipleQuestion = ChatSmartReplyQuestionResponse()

    private val templateChats = listOf(
        "I am buyer", "Is this product ready?"
    )

    override fun before() {
        super.before()
        setupDefaultResponse()
    }

    override fun setupResponse() {
        super.setupResponse()
        chatSrwResponse = AndroidFileUtil.parse(
            "buyer/success_get_srw_questions.json",
            ChatSmartReplyQuestionResponse::class.java
        )
        chatSrwResponseMultipleQuestion = AndroidFileUtil.parse(
            "buyer/success_get_srw_multiple_questions.json",
            ChatSmartReplyQuestionResponse::class.java
        )
    }

    private fun setupDefaultResponse() {
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(
            templates = templateChats
        )
    }

    protected fun assertSrwPreviewExpanded() {
        onView(withId(R.id.rv_srw))
            .check(matches(isExpanded()))
    }

    protected fun assertSrwBubbleExpanded(
        position: Int
    ) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.chat_srw_bubble
            )
        ).check(matches(isExpanded()))
    }

    protected fun assertSrwPreviewCollapsed() {
        onView(withId(R.id.rv_srw))
            .check(matches(not(isExpanded())))
    }

}