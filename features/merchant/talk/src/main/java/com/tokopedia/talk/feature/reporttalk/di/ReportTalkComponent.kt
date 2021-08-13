package com.tokopedia.talk.feature.reporttalk.di


import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reporttalk.view.fragment.ReportTalkFragment
import dagger.Component

/**
 * @author by nisie on 8/30/18.
 */
@ReportTalkScope
@Component(modules = [ReportTalkViewModelModule::class], dependencies = [TalkComponent::class])
interface ReportTalkComponent {
    fun inject(reportTalkFragment: ReportTalkFragment)
}