package com.tokopedia.home_account.explicitprofile.features.categories.sections

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel

class SectionAdapter(
    sectionListener: SectionViewHolder.SectionListener
) : BaseAdapter<SectionsDataModel>() {

    init {
        delegatesManager.addDelegate(SectionAdapterDelegate(sectionListener))
    }
}