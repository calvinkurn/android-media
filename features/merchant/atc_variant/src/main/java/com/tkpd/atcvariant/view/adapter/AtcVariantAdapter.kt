package com.tkpd.atcvariant.view.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.view.viewholder.AtcVariantQuantityViewHolder
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

    fun getHeaderDataModel(): VariantHeaderDataModel? {
        return (currentList.firstOrNull {
            it is VariantHeaderDataModel
        } as? VariantHeaderDataModel)
    }
}