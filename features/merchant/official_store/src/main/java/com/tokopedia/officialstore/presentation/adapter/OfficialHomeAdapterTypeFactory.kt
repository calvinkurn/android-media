package com.tokopedia.officialstore.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.presentation.adapter.viewholder.BrandPopulerViewHolder
import com.tokopedia.officialstore.presentation.adapter.viewholder.CategoryViewHolder
import com.tokopedia.officialstore.presentation.adapter.viewholder.ExclusiveBrandViewHolder
import com.tokopedia.officialstore.presentation.adapter.viewmodel.BrandPopulerViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.CategoryViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.ExclusiveBrandViewModel

class OfficialHomeAdapterTypeFactory : BaseAdapterTypeFactory(), OfficialHomeTypeFactory {

    override fun type(brandPopulerViewModel: BrandPopulerViewModel): Int {
        return BrandPopulerViewHolder.LAYOUT
    }

    override fun type(categoryViewModel: CategoryViewModel): Int {
        return CategoryViewHolder.LAYOUT
    }

    override fun type(exclusiveBrandViewModel: ExclusiveBrandViewModel): Int {
        return ExclusiveBrandViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if (type == BrandPopulerViewHolder.LAYOUT) {
            return BrandPopulerViewHolder(parent)
        } else if (type == CategoryViewHolder.LAYOUT) {
            return CategoryViewHolder(parent)
        } else if (type == ExclusiveBrandViewHolder.LAYOUT) {
            return ExclusiveBrandViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}