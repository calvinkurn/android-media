package com.tokopedia.topchat.stub.chatsearch.view.fragment

import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.chatsearch.di.ChatSearchModuleTest
import com.tokopedia.topchat.stub.chatsearch.di.ChatSearchUsecaseStub
import com.tokopedia.topchat.stub.chatsearch.di.DaggerChatSearchComponentTest
import com.tokopedia.topchat.stub.chatsearch.usecase.GetSearchQueryUseCaseStub

class ChatSearchFragmentStub: ChatSearchFragment() {

    private lateinit var getSearchQueryUsecase: GetSearchQueryUseCaseStub

    override fun initInjector() {
        DaggerChatSearchComponentTest
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatSearchModuleTest(ChatSearchModuleTest())
                .chatSearchUsecaseStub(ChatSearchUsecaseStub(getSearchQueryUsecase))
                .build()
                .inject(this)
    }

    companion object {
        fun createFragment(getSearchQueryUsecase: GetSearchQueryUseCaseStub): ChatSearchFragmentStub {
            return ChatSearchFragmentStub().apply {
                this.getSearchQueryUsecase = getSearchQueryUsecase
            }
        }
    }
}