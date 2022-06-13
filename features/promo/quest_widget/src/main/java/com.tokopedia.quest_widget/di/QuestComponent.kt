package com.tokopedia.quest_widget.di

import com.tokopedia.quest_widget.di.module.DispatcherModule
import com.tokopedia.quest_widget.di.module.ViewModelModule
import com.tokopedia.quest_widget.view.QuestWidgetView
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class QuestScope

@QuestScope
@Component(modules = [DispatcherModule::class, ViewModelModule::class])
interface QuestComponent {
    fun inject(view: QuestWidgetView)
}