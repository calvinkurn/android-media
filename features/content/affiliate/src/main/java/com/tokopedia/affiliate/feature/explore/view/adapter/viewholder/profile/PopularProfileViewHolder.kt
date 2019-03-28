package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.profile

import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel
import com.tokopedia.kotlin.extensions.view.getDimens
import kotlinx.android.synthetic.main.item_af_popular_profile.view.*

/**
 * @author by milhamj on 12/03/19.
 */
class PopularProfileViewHolder(v: View, private val mainView: ExploreContract.View)
    : AbstractViewHolder<PopularProfileViewModel>(v) {

    val adapter: PopularProfileAdapter by lazy {
        PopularProfileAdapter(mainView)
    }

    init {
        itemView.profileRv.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View,
                                        parent: RecyclerView, state: RecyclerView.State) {
                outRect.right = itemView.getDimens(R.dimen.dp_8)
            }
        })
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_popular_profile
    }

    override fun bind(element: PopularProfileViewModel?) {
        if (element == null) {
            return
        }

        adapter.list.clear()
        adapter.list.addAll(element.popularProfiles)
        adapter.notifyDataSetChanged()
        itemView.profileRv.adapter = adapter

        itemView.titleView.bind(element.titleViewModel)
    }
}