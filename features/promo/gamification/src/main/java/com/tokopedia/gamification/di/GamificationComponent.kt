package com.tokopedia.gamification.di

import com.tokopedia.gamification.cracktoken.fragment.CrackEmptyTokenFragment
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment
import com.tokopedia.gamification.taptap.fragment.TapTapTokenFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 3/28/18.
 */
@GamificationScope
@Component(modules = [GamificationModule::class, ActivityContextModule::class])
interface GamificationComponent {

    fun inject(crackTokenFragment: CrackTokenFragment?)
    fun inject(crackEmptyTokenFragment: CrackEmptyTokenFragment?)
    fun inject(tapTapTokenFragment: TapTapTokenFragment?)
}