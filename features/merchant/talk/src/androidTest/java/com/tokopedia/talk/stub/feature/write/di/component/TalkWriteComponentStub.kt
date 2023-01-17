package com.tokopedia.talk.stub.feature.write.di.component

import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteScope
import com.tokopedia.talk.feature.write.di.TalkWriteViewModelModule
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import dagger.Component

@Component(modules = [TalkWriteViewModelModule::class], dependencies = [TalkComponentStub::class])
@TalkWriteScope
interface TalkWriteComponentStub : TalkWriteComponent
