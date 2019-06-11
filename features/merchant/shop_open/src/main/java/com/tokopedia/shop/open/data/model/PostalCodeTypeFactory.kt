package com.tokopedia.shop.open.data.model

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder


interface PostalCodeTypeFactory : AdapterTypeFactory {

    fun type(postalCodeViewModel: PostalCodeViewModel): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}