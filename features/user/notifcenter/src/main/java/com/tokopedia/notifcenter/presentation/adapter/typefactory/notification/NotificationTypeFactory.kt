package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel

interface NotificationTypeFactory : AdapterTypeFactory {
    fun type(sectionTitleUiModel: SectionTitleUiModel): Int
    fun type(bigDividerUiModel: BigDividerUiModel): Int
    fun type(notificationUiModel: NotificationUiModel): Int
    fun type(loadMoreUiModel: LoadMoreUiModel): Int

    /**
     * to support 1 uiModel has several type of view
     * @return must be layout [LayoutRes]
     */
    @LayoutRes
    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    /**
     * use to pass interface implmented on adapter to viewholder
     * @param any can be used to pass several interfaces without the need
     * to call `this` multiple times
     */
    fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int, adapterListener: Any
    ): AbstractViewHolder<out Visitable<*>>
}