package com.tokopedia.play.broadcaster.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.play.broadcaster.di.key.FragmentKey
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.factory.PlayBroadcastFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 27/05/20
 */
@Module
abstract class PlayBroadcastFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: PlayBroadcastFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastPreparationFragment::class)
    abstract fun getPlayBroadcastPreparationFragment(fragment: PlayBroadcastPreparationFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastSummaryFragment::class)
    abstract fun getPlayBroadcastSummaryFragment(fragment: PlayBroadcastSummaryFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayBroadcastUserInteractionFragment::class)
    abstract fun getPlayLiveBroadcastFragment(fragment: PlayBroadcastUserInteractionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayPermissionFragment::class)
    abstract fun getPermissionFragment(fragment: PlayPermissionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayInteractiveLeaderBoardBottomSheet::class)
    abstract fun getInteractiveLeaderBoardBottomSheet(fragment: PlayInteractiveLeaderBoardBottomSheet): Fragment

    /**
     * Revamp
     */
    @Binds
    @IntoMap
    @FragmentKey(ProductSetupFragment::class)
    abstract fun getPlayBroProductSetupFragment(fragment: ProductSetupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(EtalaseListBottomSheet::class)
    abstract fun getPlayBroEtalaseAndCampaignListBottomSheet(fragment: EtalaseListBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductChooserBottomSheet::class)
    abstract fun getPlayBroProductChooserBottomSheet(fragment: ProductChooserBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductSortBottomSheet::class)
    abstract fun getPlayBroProductSortBottomSheet(fragment: ProductSortBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductSummaryBottomSheet::class)
    abstract fun getPlayBroProductSummaryBottomSheet(fragment: ProductSummaryBottomSheet): Fragment
}