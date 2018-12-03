package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory
import com.tokopedia.kol.feature.post.view.adapter.viewholder.EmptyKolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.ExploreViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostYoutubeViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.*
import com.tokopedia.profile.view.adapter.viewholder.ProfileEmptyViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel

/**
 * @author by milhamj on 9/20/18.
 */
class ProfileTypeFactoryImpl(private val viewListener : ProfileEmptyContract.View,
                             private val kolPostViewListener : KolPostListener.View.ViewHolder?)
    : BaseAdapterTypeFactory(), ProfileTypeFactory, KolPostTypeFactory {

    override fun type(viewModel: ProfileHeaderViewModel): Int {
        return ProfileHeaderViewHolder.LAYOUT
    }

    override fun type(viewModel: ProfileEmptyViewModel): Int {
        return ProfileEmptyViewHolder.LAYOUT
    }

    override fun type(kolPostViewModel: KolPostViewModel): Int {
        return KolPostViewHolder.LAYOUT
    }

    override fun type(emptyKolPostViewModel: EmptyKolPostViewModel): Int {
        return EmptyKolPostViewHolder.LAYOUT
    }

    override fun type(kolPostYoutubeViewModel: KolPostYoutubeViewModel): Int {
        return KolPostYoutubeViewHolder.LAYOUT
    }

    override fun type(exploreViewModel: ExploreViewModel): Int {
        return ExploreViewHolder.LAYOUT
    }

    override fun type(entryPointViewModel: EntryPointViewModel?): Int {
        return 0
    }

    override fun setType(type: KolPostViewHolder.Type?) {
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            ProfileHeaderViewHolder.LAYOUT ->
                ProfileHeaderViewHolder(parent, viewListener) as AbstractViewHolder<Visitable<*>>
            ProfileEmptyViewHolder.LAYOUT ->
                ProfileEmptyViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            KolPostViewHolder.LAYOUT ->
                    KolPostViewHolder(parent,
                            kolPostViewListener,
                            KolPostViewHolder.Type.PROFILE
                    ) as AbstractViewHolder<Visitable<*>>
            KolPostYoutubeViewHolder.LAYOUT ->
                KolPostYoutubeViewHolder(parent,
                        kolPostViewListener,
                        KolPostYoutubeViewHolder.Type.PROFILE
                ) as AbstractViewHolder<Visitable<*>>
            EmptyKolPostViewHolder.LAYOUT ->
                EmptyKolPostViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            ExploreViewHolder.LAYOUT ->
                ExploreViewHolder(parent, kolPostViewListener) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)
        }
    }
}