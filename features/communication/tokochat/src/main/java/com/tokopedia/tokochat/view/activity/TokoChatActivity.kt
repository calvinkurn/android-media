package com.tokopedia.tokochat.view.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokochat.di.DaggerTokoChatComponent
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatContextModule
import com.tokopedia.tokochat.view.fragment.TokoChatFragment
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatFragmentExp
import com.tokopedia.tokochat.view.fragment.factory.TokoChatFragmentFactory
import com.tokopedia.tokochat_common.view.activity.BaseTokoChatActivity

class TokoChatActivity : BaseTokoChatActivity<TokoChatComponent>() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactory()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
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
        val isExp = true
        return if (isExp) {
            TokoChatFragmentExp.getFragment(
                supportFragmentManager,
                classLoader,
                bundle ?: Bundle()
            )
        } else {
            TokoChatFragment.getFragment(
                supportFragmentManager,
                classLoader,
                bundle ?: Bundle()
            )
        }
    }
}
