package com.tokopedia.talk_old.shoptalk.di

import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.shoptalk.view.fragment.ShopTalkFragment
import dagger.Component

/**
 * @author by nisie on 9/17/18.
 */
@ShopTalkScope
@Component(modules = arrayOf(ShopTalkModule::class), dependencies = arrayOf(TalkComponent::class))
interface ShopTalkComponent {

    fun inject(shopTalkFragment: ShopTalkFragment)

}