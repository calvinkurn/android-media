package com.tokopedia.tokopedianow.sortfilter.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowSectionHeaderTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipListViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowSectionHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowSectionHeaderViewHolder.SectionHeaderListener
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel
import com.tokopedia.tokopedianow.sortfilter.presentation.viewholder.SortFilterViewHolder
import com.tokopedia.tokopedianow.sortfilter.presentation.viewholder.SortFilterViewHolder.SortFilterViewHolderListener

class SortFilterAdapterTypeFactory(
    private val sortFilterListener: SortFilterViewHolderListener,
    private val chipListener: ChipListener,
    private val sectionHeaderListener: SectionHeaderListener? = null
) : BaseAdapterTypeFactory(), SortFilterTypeFactory, TokoNowChipListTypeFactory,
    TokoNowSectionHeaderTypeFactory {

    override fun type(uiModel: SortFilterUiModel): Int = SortFilterViewHolder.LAYOUT

    override fun type(uiModel: TokoNowChipListUiModel): Int = TokoNowChipListViewHolder.LAYOUT

    override fun type(uiModel: TokoNowSectionHeaderUiModel): Int = TokoNowSectionHeaderViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel?): Int = TokoNowLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SortFilterViewHolder.LAYOUT -> SortFilterViewHolder(view, sortFilterListener)
            TokoNowChipListViewHolder.LAYOUT -> TokoNowChipListViewHolder(view, chipListener)
            TokoNowSectionHeaderViewHolder.LAYOUT -> TokoNowSectionHeaderViewHolder(view, sectionHeaderListener)
            TokoNowLoadingMoreViewHolder.LAYOUT -> TokoNowLoadingMoreViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}