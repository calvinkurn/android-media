package com.tokopedia.filter.newdynamicfilter.helper

import com.tokopedia.filter.common.data.Sort

class SortHelper {

    companion object {
        fun getSelectedSortFromSearchParameter(
            searchParameter: Map<String, String>,
            sortList: List<Sort>
        ): Map<String, String> {
            val sortParameter = mutableMapOf<String, String>()

            for (sort in sortList) {
                if (searchParameter.containsKey(sort.key)) {
                    sortParameter[sort.key] = searchParameter.getValue(sort.key)
                }
            }

            return sortParameter
        }

        const val PALING_SESUAI = "Paling Sesuai"
        const val ULASAN = "Ulasan"
        const val TERBARU = "Terbaru"
        const val HARGA_TERTINGGI = "Harga Tertinggi"
        const val HARGA_TERENDAH = "Harga Terendah"
    }
}
