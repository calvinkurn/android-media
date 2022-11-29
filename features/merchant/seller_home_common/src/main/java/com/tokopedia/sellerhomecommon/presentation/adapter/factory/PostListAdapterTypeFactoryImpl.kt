package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import android.view.View
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PostImageEmphasizedViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PostTextEmphasizedViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PostTimerDismissalViewHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostListAdapterTypeFactoryImpl(
    private val listener: PostListPagerAdapter.Listener,
    private val isCheckingMode: Boolean = false
) : BaseAdapterTypeFactory(), PostListAdapterTypeFactory {

    override fun type(post: PostItemUiModel.PostImageEmphasizedUiModel): Int {
        return PostImageEmphasizedViewHolder.RES_LAYOUT
    }

    override fun type(post: PostItemUiModel.PostTextEmphasizedUiModel): Int {
        return PostTextEmphasizedViewHolder.RES_LAYOUT
    }

    override fun type(model: PostItemUiModel.PostTimerDismissalUiModel): Int {
        return PostTimerDismissalViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {

        return when (type) {
            PostImageEmphasizedViewHolder.RES_LAYOUT -> PostImageEmphasizedViewHolder(parent, listener, isCheckingMode)
            PostTextEmphasizedViewHolder.RES_LAYOUT -> PostTextEmphasizedViewHolder(parent, listener, isCheckingMode)
            PostTimerDismissalViewHolder.RES_LAYOUT -> PostTimerDismissalViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}