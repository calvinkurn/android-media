package com.tokopedia.talk.stub.feature.reading.di

import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingScope
import com.tokopedia.talk.feature.reading.di.TalkReadingViewModelModule
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import dagger.Component

@Component(modules = [TalkReadingViewModelModule::class], dependencies = [TalkComponentStub::class])
@TalkReadingScope
interface TalkReadingComponentStub : TalkReadingComponent
