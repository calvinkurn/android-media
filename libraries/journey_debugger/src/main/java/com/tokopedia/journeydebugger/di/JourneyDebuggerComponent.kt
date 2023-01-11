package com.tokopedia.journeydebugger.di

import NAMED_JOURNEY
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.journeydebugger.ui.fragment.JourneyDebuggerFragment
import com.tokopedia.journeydebugger.ui.presenter.JourneyDebugger
import dagger.Component
import javax.inject.Named

@Component(modules = [JourneyDebuggerModule::class], dependencies = [BaseAppComponent::class])
@JourneyDebuggerScope
interface JourneyDebuggerComponent {
    fun inject(fragment: JourneyDebuggerFragment?)

    @get:Named(NAMED_JOURNEY)
    val journeyPresenter: JourneyDebugger.Presenter
}
