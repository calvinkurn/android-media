package com.tokopedia.shop.open.view.adapter
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.open.data.model.PostalCodeTypeFactory
import com.tokopedia.shop.open.data.model.PostalCodeViewModel
import com.tokopedia.shop.open.view.holder.PostalCodeChooserViewHolder

class PostalCodeAdapterTypeFactory: BaseAdapterTypeFactory(),PostalCodeTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == PostalCodeChooserViewHolder.LAYOUT) {
            PostalCodeChooserViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(postalCodeViewModel: PostalCodeViewModel): Int {
        return PostalCodeChooserViewHolder.LAYOUT
    }

}