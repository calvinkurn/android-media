package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.DaggerTalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.TalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel.TalkSmartReplyDetailViewModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.fragment_talk_smart_reply_detail.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_smart_reply_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDescriptionText()
        setToolbarTitle()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun setDescriptionText() {
        talkSmartReplyDetailSubtitle.text = context?.let { HtmlLinkHelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
    }

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_smart_reply_detail_page)
    }
}