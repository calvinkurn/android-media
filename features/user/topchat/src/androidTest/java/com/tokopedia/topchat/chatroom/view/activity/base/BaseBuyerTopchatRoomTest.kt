package com.tokopedia.topchat.chatroom.view.activity.base

import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi

open class BaseBuyerTopchatRoomTest : TopchatRoomTest() {

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
    }

    private fun setupDefaultResponse() {
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(
                templates = templateChats
        )
    }

}