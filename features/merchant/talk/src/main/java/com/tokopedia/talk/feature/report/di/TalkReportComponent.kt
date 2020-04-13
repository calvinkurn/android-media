package com.tokopedia.talk.feature.report.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.report.presentation.fragment.TalkReportFragment
import dagger.Component

@Component(dependencies = [TalkComponent::class])
@TalkReportScope
interface TalkReportComponent {
    fun inject(talkReportFragment: TalkReportFragment)
}