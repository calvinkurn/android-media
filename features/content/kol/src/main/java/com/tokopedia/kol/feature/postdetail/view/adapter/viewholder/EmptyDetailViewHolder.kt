package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract
import com.tokopedia.kol.feature.postdetail.view.viewmodel.EmptyDetailViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_empty_detail.view.*

/**
 * @author by yfsx on 12/10/18.
 */
class EmptyDetailViewHolder(itemView: View,
                            private val mainView: KolPostDetailContract.View)
    : AbstractViewHolder<EmptyDetailViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_empty_detail
        const val EMPTY_IMG_FORMAT = "https://ecs7.tokopedia.net/img/android/toped_logo_crying/%s/toped_logo_crying.png"

    }

    override fun bind(element: EmptyDetailViewModel) {
        initView(element)
        initViewListener()
    }

    private fun initView(element: EmptyDetailViewModel) {
        val screenDensity = DisplayMetricUtils.getScreenDensity(itemView.context)
        itemView.image.loadImage(String.format(EMPTY_IMG_FORMAT, screenDensity))
    }

    private fun initViewListener() {
        itemView.btn_back.setOnClickListener { mainView.onEmptyDetailClicked() }
    }
}
