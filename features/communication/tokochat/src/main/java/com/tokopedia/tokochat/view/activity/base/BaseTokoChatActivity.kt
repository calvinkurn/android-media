package com.tokopedia.tokochat.view.activity.base

import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.di.DaggerTokoChatComponent
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatContextModule
import com.tokopedia.tokochat.view.fragment.factory.TokoChatFragmentFactory

abstract class BaseTokoChatActivity: BaseSimpleActivity(), HasComponent<TokoChatComponent> {

    private var tokoChatComponent: TokoChatComponent? = null
    protected var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactory()
        super.onCreate(savedInstanceState)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragmentContainer
    }

    override fun getLayoutRes(): Int {
        return R.layout.base_activity_toko_chat
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    protected open fun initializeChatServiceComponent(): TokoChatComponent {
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
}
