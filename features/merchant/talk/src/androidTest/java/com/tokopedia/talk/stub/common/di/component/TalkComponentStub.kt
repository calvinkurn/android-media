package com.tokopedia.talk.stub.common.di.component

import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.stub.common.di.module.TalkModuleStub
import dagger.Component

@Component(modules = [TalkModuleStub::class], dependencies = [BaseAppComponentStub::class])
@TalkScope
interface TalkComponentStub : TalkComponent
