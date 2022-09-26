package com.tokopedia.tokochat.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokochat.di.DaggerTokoChatComponent
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatContextModule
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatListFragmentExp
import com.tokopedia.tokochat.view.fragment.factory.TokoChatFragmentFactory
import com.tokopedia.tokochat_common.view.activity.BaseTokoChatActivity

class TokoChatListActivity: BaseTokoChatActivity<TokoChatComponent>() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactory()
    }

    private fun initializeChatServiceComponent(): TokoChatComponent {
        return DaggerTokoChatComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoChatContextModule(TokoChatContextModule(this))
            .build().also {
                tokoChatComponent = it
            }
    }

    override fun getComponent(): TokoChatComponent {
        return tokoChatComponent?: initializeChatServiceComponent()
    }

    override fun getNewFragment(): Fragment {
        return TokoChatListFragmentExp.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }
}
