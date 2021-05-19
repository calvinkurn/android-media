package com.tkpd.atc_variant.views.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil

/**
 * Created by Yehezkiel on 06/05/21
 */

class AtcVariantAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                        typeFactoryImpl: AtcVariantAdapterTypeFactoryImpl) : BaseListAdapterDiffutil<AtcVariantAdapterTypeFactoryImpl>(asyncDifferConfig, typeFactoryImpl) {

    fun getVariantDataModel(): VariantComponentDataModel? {
        return currentList.firstOrNull { it is VariantComponentDataModel } as? VariantComponentDataModel
    }
}