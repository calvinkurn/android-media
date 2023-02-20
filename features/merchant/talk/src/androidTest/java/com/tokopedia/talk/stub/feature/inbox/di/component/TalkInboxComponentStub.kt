package com.tokopedia.talk.stub.feature.inbox.di.component

import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxModule
import com.tokopedia.talk.feature.inbox.di.TalkInboxScope
import com.tokopedia.talk.feature.inbox.di.TalkInboxViewModelModule
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Component

@Component(
    modules = [TalkInboxViewModelModule::class, TalkInboxModule::class],
    dependencies = [TalkComponentStub::class]
)
@TalkInboxScope
interface TalkInboxComponentStub : TalkInboxComponent {
    fun trackingQueue(): TrackingQueue
}
