package com.tokopedia.home_account.explicitprofile.features.categories.sections.chips

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel

class ChipsAdapter(
    chipsListener: ChipsViewHolder.QuestionChipsListener
) : BaseAdapter<QuestionDataModel>() {

    init {
        delegatesManager.addDelegate(ChipsAdapterDelegate(chipsListener))
    }
}