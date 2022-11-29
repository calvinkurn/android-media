package com.tokopedia.talk.stub.feature.reply.di.component

import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.di.TalkReplyScope
import com.tokopedia.talk.feature.reply.di.TalkReplyViewModelModule
import com.tokopedia.talk.stub.common.di.component.TalkComponentStub
import dagger.Component

@Component(modules = [TalkReplyViewModelModule::class], dependencies = [TalkComponentStub::class])
@TalkReplyScope
interface TalkReplyComponentStub : TalkReplyComponent
