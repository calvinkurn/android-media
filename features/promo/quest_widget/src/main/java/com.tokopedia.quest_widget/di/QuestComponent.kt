package com.tokopedia.quest_widget.di

import com.tokopedia.quest_widget.di.module.DispatcherModule
import com.tokopedia.quest_widget.di.module.ViewModelModule
import com.tokopedia.quest_widget.view.QuestActivity
import dagger.Component

@Component(modules = [DispatcherModule::class, ViewModelModule::class])
interface QuestComponent {
    fun inject(view: QuestActivity)
}