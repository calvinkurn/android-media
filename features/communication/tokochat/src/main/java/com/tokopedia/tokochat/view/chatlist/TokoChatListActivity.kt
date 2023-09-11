package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseActivity
import javax.inject.Inject

class TokoChatListActivity: TokoChatListBaseActivity<TokoChatComponent>() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun initializeTokoChatComponent(): TokoChatComponent {
        return TokoChatActivityComponentFactory
            .instance
            .createTokoChatComponent(application).also {
                tokoChatComponent = it
            }
    }

    override fun getComponent(): TokoChatComponent {
        return tokoChatComponent ?: initializeTokoChatComponent()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return TokoChatListFragment.getFragment(
            supportFragmentManager,
            classLoader,
            Bundle()
        )
    }
}
