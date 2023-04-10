package com.tokopedia.logisticseller.di.findingnewdriver

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.findingnewdriver.activity.FindingNewDriverActivity
import com.tokopedia.logisticseller.ui.findingnewdriver.bottomsheet.FindingNewDriverBottomSheet
import com.tokopedia.logisticseller.ui.findingnewdriver.fragment.FindingNewDriverFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [FindingNewDriverModule::class, FindingNewDriverViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface FindingNewDriverComponent {
    fun inject(findingNewDriverActivity: FindingNewDriverActivity)

    fun inject(findingNewDriverFragment: FindingNewDriverFragment)

    fun inject(findingNewDriverBottomSheet: FindingNewDriverBottomSheet)
}
