package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play_common.player.TokopediaPlayManager
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule {

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context): TokopediaPlayManager = TokopediaPlayManager.getInstance(ctx)
}