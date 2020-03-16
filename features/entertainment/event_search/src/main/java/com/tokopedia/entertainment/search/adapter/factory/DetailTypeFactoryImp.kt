package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.search.adapter.DetailEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.ResetFilterViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventGridViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.CategoryTextViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.ResetFilterViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventGridViewModel

class DetailTypeFactoryImp(val onClicked : ((Any) -> Unit) = {}) : DetailTypeFactory {
    override fun type(viewModel: SearchEventGridViewModel): Int {
        return SearchEventGridViewHolder.LAYOUT
    }

    override fun type(viewModel: CategoryTextViewModel): Int {
        return CategoryTextViewHolder.LAYOUT
    }

    override fun type(viewModel: ResetFilterViewModel): Int {
        return ResetFilterViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): DetailEventViewHolder<*> {
        val createDetailEventViewHolder : DetailEventViewHolder<*>
        createDetailEventViewHolder = if (type == CategoryTextViewHolder.LAYOUT) {
            CategoryTextViewHolder(view, onClicked)
        }else if(type == SearchEventGridViewHolder.LAYOUT){
            SearchEventGridViewHolder(view)
        }else if(type == ResetFilterViewHolder.LAYOUT){
            ResetFilterViewHolder(view, onClicked)
        } else{
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return createDetailEventViewHolder
    }
}