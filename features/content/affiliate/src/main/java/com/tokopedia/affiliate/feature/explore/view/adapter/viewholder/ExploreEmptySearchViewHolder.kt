package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.EMPTY_IMG_FORMAT
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_af_explore_empty_search.view.*

/**
 * @author by yfsx on 12/10/18.
 */
class ExploreEmptySearchViewHolder(itemView: View,
                                   private val mainView: ExploreContract.View)
    : AbstractViewHolder<ExploreEmptySearchViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_af_explore_empty_search
    }

    override fun bind(element: ExploreEmptySearchViewModel) {
        initView(element)
        initViewListener()
    }

    private fun initView(element: ExploreEmptySearchViewModel) {
        val screenDensity = DisplayMetricUtils.getScreenDensity(itemView.context)
        itemView.image.loadImage(String.format(EMPTY_IMG_FORMAT, screenDensity))
        itemView.title.text = element.title
        itemView.desc.text = element.subtitle
    }

    private fun initViewListener() {
        itemView.btn_search.setOnClickListener { mainView.onButtonEmptySearchClicked() }
    }
}
