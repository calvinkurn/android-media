package com.example.home.play.di

import com.example.home.play.HomePresenterTest
import com.tokopedia.core.base.di.component.AppComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [HomeTestModule::class]
)
interface HomeTestComponent : AppComponent {
    fun inject(presenter: HomePresenterTest)
}