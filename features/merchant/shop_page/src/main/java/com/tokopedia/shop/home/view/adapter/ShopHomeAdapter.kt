package com.tokopedia.shop.home.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.shop.home.WidgetYoutubeVideo
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVideoViewHolder
import com.tokopedia.shop.home.view.fragment.IFragmentManager
import com.tokopedia.shop.home.view.model.WidgetModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        val context: Context?,
        val element: WidgetModel,
        shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory, null) {


//    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
//        super.onViewAttachedToWindow(holder)
//
//        val video = element.takeIf { it.name == WidgetYoutubeVideo }
//        when(holder) {
//            is ShopHomeVideoViewHolder -> {
//                if (context != null) {
//                    video?.binder?.bind(context, holder, fragmentManager)
//                }
//            }
//        }
//    }

//    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
//        super.onViewDetachedFromWindow(holder)
//        val video = element.takeIf { it.name == WidgetYoutubeVideo }
//        when(holder) {
//            is ShopHomeVideoViewHolder -> {
//                if (context != null) {
//                    video?.binder?.unBind(holder, fragmentManager)
//                }
//            }
//        }
//    }
}