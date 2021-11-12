package com.tokopedia.dg_transaction.testing.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.common_digital.common.di.DigitalCommonScope
import dagger.Component

@DigitalCommonScope
@Component(
    modules = [StubDigitalCommonModule::class],
    dependencies = [BaseAppComponent::class]
)
interface StubDigitalCommonComponent: DigitalCommonComponent