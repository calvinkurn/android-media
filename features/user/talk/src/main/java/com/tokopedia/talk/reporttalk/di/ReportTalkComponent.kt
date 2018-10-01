package com.tokopedia.talk.reporttalk.di

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.reporttalk.view.fragment.ReportTalkFragment
import dagger.Component

/**
 * @author by nisie on 8/30/18.
 */
@ReportTalkScope
@Component(modules = arrayOf(ReportTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface ReportTalkComponent {

    fun inject(reportTalkFragment: ReportTalkFragment)

}