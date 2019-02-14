package com.tokopedia.groupchat.room.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.groupchat.room.di.PlayModule
import com.tokopedia.groupchat.room.di.PlayScope
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.groupchat.room.view.fragment.PlayWebviewFragment
import dagger.Component

/**
 * @author by nisie on 12/12/18.
 */
@PlayScope
@Component(modules = arrayOf(PlayModule::class), dependencies = [(BaseAppComponent::class)])
interface PlayComponent {

    fun inject(playFragment: PlayFragment)

    fun inject(fragment: PlayWebviewFragment)
}