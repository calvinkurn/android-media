package com.tokopedia.homenav.category.view.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException

/**
 * Created by Lukas on 20/10/20.
 */
class CategoryListTypeFactoryImpl : CategoryListTypeFactory{
    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        throw TypeNotSupportedException.create("Layout not supported")
    }
}