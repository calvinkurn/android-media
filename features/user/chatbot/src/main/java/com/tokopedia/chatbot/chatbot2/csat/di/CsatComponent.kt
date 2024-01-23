package com.tokopedia.chatbot.chatbot2.csat.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.chatbot.chatbot2.csat.CsatBottomsheet
import dagger.Component

@ActivityScope
@Component(modules = [CsatModule::class], dependencies = [BaseAppComponent::class])
interface CsatComponent {
    fun inject(bottomSheet: CsatBottomsheet)
}
