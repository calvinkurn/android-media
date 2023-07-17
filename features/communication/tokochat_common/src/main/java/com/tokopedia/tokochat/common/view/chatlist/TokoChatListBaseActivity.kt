package com.tokopedia.tokochat.common.view.chatlist

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent

abstract class TokoChatListBaseActivity<T>: BaseSimpleActivity(), HasComponent<T> {
    protected var tokoChatComponent: T? = null
}
