package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment

import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.DaggerTalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.TalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel.TalkSmartReplyDetailViewModel
import javax.inject.Inject

class TalkSmartReplyDetailFragment : BaseDaggerFragment(), HasComponent<TalkSmartReplyDetailComponent> {

    @Inject
    lateinit var viewModel: TalkSmartReplyDetailViewModel

    override fun getComponent(): TalkSmartReplyDetailComponent? {
        return activity?.run {
            DaggerTalkSmartReplyDetailComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }
}