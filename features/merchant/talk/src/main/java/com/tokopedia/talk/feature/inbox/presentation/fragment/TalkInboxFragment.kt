package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.net.Uri
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.talk_old.talkdetails.view.activity.TalkDetailsActivity
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<TalkInboxUiModel, TalkInboxAdapterTypeFactory>(), HasComponent<TalkInboxComponent> {

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
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(talkUiModel: TalkInboxUiModel?) {
        talkUiModel?.let {
            goToReply(it.inboxDetail.questionID, viewModel.getShopId())
        }
    }

    override fun loadData(page: Int) {
        TODO("Not yet implemented")
    }

    override fun getComponent(): TalkInboxComponent? {
        return activity?.run {
            DaggerTalkInboxComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    private fun goToReply(questionId: String, shopId: String) {
        Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId))
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, shopId)
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkDetailsActivity.SOURCE_INBOX)
                .build().toString()
    }

}