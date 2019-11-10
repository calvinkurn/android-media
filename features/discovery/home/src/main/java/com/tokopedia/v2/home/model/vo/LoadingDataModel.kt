package com.tokopedia.v2.home.model.vo

import com.tokopedia.home.R
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType

class LoadingDataModel : ModelViewType {
    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is LoadingDataModel
    }

    override fun getPrimaryKey(): Int {
        return 0
    }
}