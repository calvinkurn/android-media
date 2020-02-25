package com.tokopedia.product.manage.feature.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

class FilterAdapter(
        private val adapterTypeFactory: FilterAdapterTypeFactory
) : BaseAdapter<FilterAdapterTypeFactory>(adapterTypeFactory) {

    companion object {
        const val SORT_HEADER = "Urutkan"
        const val ETALASE_HEADER = "Etalase"
        const val CATEGORY_HEADER = "Kategori"
        const val OTHER_FILTER_HEADER = "Filter Lainnya"
    }

    fun updateSortData(sortViewModel: SortViewModel) {
        visitables.clear()
        visitables.add(HeaderViewModel(SORT_HEADER))
        visitables.add(sortViewModel)
        notifyDataSetChanged()
    }

    fun updateEtalaseData(etalaseViewModel: EtalaseViewModel) {
        visitables.add(HeaderViewModel(ETALASE_HEADER))
        visitables.add(etalaseViewModel)
        notifyDataSetChanged()
    }

    fun updateCategoryData(categoriesViewModel: CategoriesViewModel) {
        visitables.add(HeaderViewModel(CATEGORY_HEADER))
        visitables.add(categoriesViewModel)
        notifyDataSetChanged()
    }

    fun updateOtherFilterData(otherFilterViewModel: OtherFilterViewModel) {
        visitables.add(HeaderViewModel(OTHER_FILTER_HEADER))
        visitables.add(otherFilterViewModel)
        notifyDataSetChanged()
    }
}