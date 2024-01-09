package com.tokopedia.tokopedianow.annotation.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.AnnotationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder

class AllAnnotationAdapterTypeFactory(
    private val annotationListener: AnnotationViewHolder.AnnotationListener
): BaseAdapterTypeFactory(), AllAnnotationTypeFactory {
    override fun type(uiModel: AnnotationUiModel): Int = AnnotationViewHolder.LAYOUT
    override fun type(viewModel: LoadingMoreModel): Int = TokoNowLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            AnnotationViewHolder.LAYOUT -> AnnotationViewHolder(
                itemView = parent,
                listener = annotationListener
            )
            TokoNowLoadingMoreViewHolder.LAYOUT -> TokoNowLoadingMoreViewHolder(
                itemView = parent
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
