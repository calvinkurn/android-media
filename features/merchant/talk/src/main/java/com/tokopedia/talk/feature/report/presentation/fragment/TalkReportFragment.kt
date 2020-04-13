package com.tokopedia.talk.feature.report.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.report.di.DaggerTalkReportComponent
import com.tokopedia.talk.feature.report.di.TalkReportComponent


class TalkReportFragment : BaseDaggerFragment(), HasComponent<TalkReportComponent> {

    companion object {

        const val TALK_ID = "talk_id"
        const val COMMENT_ID = "comment_id"

        @JvmStatic
        fun createNewInstance(talkId: Int = 0, commentId: Int = 0): TalkReportFragment =
                TalkReportFragment().apply {
                    arguments?.putInt(TALK_ID, talkId)
                    arguments?.putInt(COMMENT_ID, commentId)
                }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun getComponent(): TalkReportComponent {
        return DaggerTalkReportComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }
}