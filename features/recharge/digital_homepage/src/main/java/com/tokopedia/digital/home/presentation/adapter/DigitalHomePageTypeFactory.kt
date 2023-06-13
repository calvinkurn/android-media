package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageCategoryViewHolder
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel

class DigitalHomePageTypeFactory(
    val onItemBindListener: OnItemBindListener
) : BaseAdapterTypeFactory() {


    fun type(digitalHomePageCategoryModel: DigitalHomePageCategoryModel): Int {
        return DigitalHomePageCategoryViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageCategoryViewHolder.LAYOUT -> return DigitalHomePageCategoryViewHolder(
                parent,
                onItemBindListener
            )
        }
        return super.createViewHolder(parent, type)
    }

}