package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.view.PlayPrepareBroadcastActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalaseDetailFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPrepareBroadcastFragment
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(modules = [PlayBroadcasterViewModelModule::class, PlayBroadcasterModule::class, PlayBroadcasterFragmentModule::class])
@PlayBroadcasterScope
interface PlayBroadcasterComponent {

    fun inject(prepareBroadcastActivity: PlayPrepareBroadcastActivity)
}