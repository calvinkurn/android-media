package com.tokopedia.talk.stub.feature.inbox.di.component

import com.tokopedia.talk.feature.inbox.di.TalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxScope
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import dagger.Component

@Component(dependencies = [TalkComponentStub::class])
@TalkInboxScope
interface TalkInboxContainerComponentStub : TalkInboxContainerComponent
