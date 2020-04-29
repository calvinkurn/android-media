package com.tokopedia.play.ui.variantsheet.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play.ui.variantsheet.adapter.delegate.VariantOptionPlaceholderAdapterDelegate
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel


/**
 * Created by jegul on 14/03/20
 */
class VariantOptionPlaceholderAdapter : BaseAdapter<VariantPlaceholderUiModel.Option>() {

    init {
        delegatesManager
                .addDelegate(VariantOptionPlaceholderAdapterDelegate())
    }
}