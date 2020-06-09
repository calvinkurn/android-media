package com.tokopedia.statistic.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@StatisticScope
@Component(dependencies = [BaseAppComponent::class])
interface StatisticComponent {


}