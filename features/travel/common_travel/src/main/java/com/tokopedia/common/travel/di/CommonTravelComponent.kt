package com.tokopedia.common.travel.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@CommonTravelScope
@Component(modules = [CommonTravelModule::class],
        dependencies = [BaseAppComponent::class])
interface CommonTravelComponent