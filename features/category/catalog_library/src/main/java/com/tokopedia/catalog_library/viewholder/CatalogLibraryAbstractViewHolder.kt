package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

abstract class CatalogLibraryAbstractViewHolder<T : Visitable<*>>(itemView: View?) : AbstractViewHolder<T>(itemView) {

    override fun onViewAttachedToWindow() {}

    open fun onViewDetachedFromWindow() {}

    override fun onViewDetachedFromWindow(recyclerView: RecyclerView?, visiblePercentage: Int) {}
}
