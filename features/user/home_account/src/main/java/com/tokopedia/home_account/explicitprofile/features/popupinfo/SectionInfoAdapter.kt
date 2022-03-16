package com.tokopedia.home_account.explicitprofile.features.popupinfo

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel

class SectionInfoAdapter : BaseAdapter<QuestionDataModel>() {

    init {
        delegatesManager.addDelegate(SectionInfoAdapterDelegate())
    }
}