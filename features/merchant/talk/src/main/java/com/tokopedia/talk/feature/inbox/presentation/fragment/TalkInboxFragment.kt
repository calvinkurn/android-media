package com.tokopedia.talk.feature.inbox.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<TalkInboxUiModel, TalkInboxAdapterTypeFactory>() {

    companion object {
        fun createNewInstance(): TalkInboxFragment {
            return TalkInboxFragment()
        }
    }

    @Inject
    lateinit var viewModel: TalkInboxViewModel

    override fun getAdapterTypeFactory(): TalkInboxAdapterTypeFactory {
        return TalkInboxAdapterTypeFactory()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(talkUiModel: TalkInboxUiModel?) {
        talkUiModel?.let {
            goToReply(it.inboxDetail.questionID)
        }
    }

    override fun loadData(page: Int) {
        TODO("Not yet implemented")
    }

    private fun goToReply(questionId: String) {

    }

}