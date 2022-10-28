package com.tokopedia.topchat.stub.chatsearch.view.activity

import com.tokopedia.topchat.chatsearch.view.activity.ChatSearchActivity
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
import com.tokopedia.topchat.stub.chatsearch.usecase.GetSearchQueryUseCaseStub
import com.tokopedia.topchat.stub.chatsearch.view.fragment.ChatSearchFragmentStub

class ChatSearchActivityStub: ChatSearchActivity() {

    private lateinit var getSearchQueryUsecase: GetSearchQueryUseCaseStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    fun setupTestFragment(
            useCase: GetSearchQueryUseCaseStub
    ) {
        this.getSearchQueryUsecase = useCase
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
    }

    override fun getNewFragment(): ChatSearchFragment {
        return ChatSearchFragmentStub.createFragment(getSearchQueryUsecase)
    }
}