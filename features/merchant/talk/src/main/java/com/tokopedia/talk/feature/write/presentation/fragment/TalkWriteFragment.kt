package com.tokopedia.talk.feature.write.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.di.DaggerTalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.fragment_talk_write.*

class TalkWriteFragment : BaseDaggerFragment(), HasComponent<TalkWriteComponent> {

    companion object {

        @JvmStatic
        fun createNewInstance(): TalkWriteFragment = TalkWriteFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_write, container, false)
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    private fun initView() {
        writeTNC.setOnClickListener {
            goToTermsAndConditionsPage()
        }
    }

    private fun initQuestionTextField() {

    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }
}