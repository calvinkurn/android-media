package com.tkpd.atc_variant.views.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tkpd.atc_variant.views.viewholder.AtcVariantQuantityViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil

/**
 * Created by Yehezkiel on 06/05/21
 */

class AtcVariantAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                        typeFactoryImpl: AtcVariantAdapterTypeFactoryImpl) : BaseListAdapterDiffutil<AtcVariantAdapterTypeFactoryImpl>(asyncDifferConfig, typeFactoryImpl) {

    fun removeTextWatcherQuantityViewHolder(rv: RecyclerView?) {
        val quantityViewHolderPosition = currentList.indexOfFirst {
            it is VariantQuantityDataModel
        }

        if (quantityViewHolderPosition != -1) {
            (rv?.findViewHolderForAdapterPosition(quantityViewHolderPosition) as? AtcVariantQuantityViewHolder)?.removeTextChangedListener()
        }
    }
}