package com.tokopedia.discovery.categoryrevamp.constants

class CategoryNavConstants {

    companion object {
        const val KEY_PARAMS = "params"
        const val START = "start"
        const val SC = "sc"
        const val CTG_ID = "ctg_id"
        const val DEVICE = "device"
        const val SOURCE = "source"
        const val UNIQUE_ID = "unique_id"
        const val ROWS = "rows"
        const val KEY_SAFE_SEARCH = "safe_search"
        const val OB = "ob"
        const val Q = "q"

        //val KEY_ROWS = "catalog_rows"
        const val KEY_SCHEME = "scheme"


        const val IDENTIFIER = "identifier"
        const val INTERMEDIARY = "intermediary"
        const val SAFESEARCH = "safeSearch"

        const val QUERY = "query"
        const val ST = "st"


    }


    interface RecyclerView {
        companion object {
            val VIEW_PRODUCT = 3
            val VIEW_PRODUCT_GRID_1 = 12
            val VIEW_PRODUCT_GRID_2 = 13
        }

        enum class GridType {
            GRID_1, GRID_2, GRID_3
        }
    }
}