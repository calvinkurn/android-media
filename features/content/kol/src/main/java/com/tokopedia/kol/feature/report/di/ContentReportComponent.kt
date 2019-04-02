package com.tokopedia.kol.feature.report.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.kol.feature.report.view.fragment.ContentReportFragment
import dagger.Component

/**
 * @author by milhamj on 21/11/18.
 */

@ContentReportScope
@Component(
        modules = [ContentReportModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ContentReportComponent {
    fun inject(fragment: ContentReportFragment)
}