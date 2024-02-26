package com.tokopedia.chatbot.chatbot2.csat.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.chatbot.chatbot2.csat.view.CsatBottomsheet
import dagger.Component

@ActivityScope
@Component(modules = [CsatViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CsatComponent {
    fun inject(bottomSheet: CsatBottomsheet)
}
