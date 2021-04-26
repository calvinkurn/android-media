package com.tokopedia.topchat.chatroom.view.activity.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.matchers.isExpanded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not

open class BaseBuyerTopchatRoomTest : TopchatRoomTest() {

    protected var chatSrwResponseMultipleQuestion = ChatSmartReplyQuestionResponse()

    private val templateChats = listOf(
            "I am buyer", "Is this product ready?"
    )

    @ExperimentalCoroutinesApi
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

    protected fun assertSrwExpanded() {
        onView(withId(R.id.rv_srw))
                .check(matches(isExpanded()))
    }

    protected fun assertSrwCollapsed() {
        onView(withId(R.id.rv_srw))
                .check(matches(not(isExpanded())))
    }

}