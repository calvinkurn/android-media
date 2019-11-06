package com.tokopedia.interest_pick_common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * @author by yoasfs on 2019-11-05
 */

@InterestPickCommonScope
@Component(modules = [InterestPickCommonModule::class], dependencies = [BaseAppComponent::class])
interface InterestPickCommonComponent {

}