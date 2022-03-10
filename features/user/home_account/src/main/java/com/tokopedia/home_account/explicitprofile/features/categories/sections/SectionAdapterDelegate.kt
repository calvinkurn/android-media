package com.tokopedia.home_account.explicitprofile.features.categories.sections

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel

class SectionAdapterDelegate(
    private val sectionListener: SectionViewHolder.SectionListener
) : TypedAdapterDelegate<SectionsDataModel, SectionsDataModel, SectionViewHolder>(
    SectionViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: SectionsDataModel, holder: SectionViewHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SectionViewHolder {
        return SectionViewHolder(basicView, sectionListener)
    }

}