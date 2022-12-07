package com.tokopedia.data_explorer.db_explorer.presentation

object Constants {

    object Keys {
        const val DATABASE_PATH = "KEY_DATABASE_PATH"
        const val DATABASE_NAME = "KEY_DATABASE_NAME"
        const val SCHEMA_NAME = "KEY_SCHEMA_NAME"
    }

    object Urls {
        const val EMPTY_DATA_URL = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"
    }

    object ErrorMessages {
        const val NO_CONTENT = "Table Empty! No Content Found"
        const val INVALID_PAGE_REQUEST = "Invalid Page Request"
        const val COPY_ERROR = "Copy operation cannot be performed"
        const val DELETION_ERROR = "Deletion cannot be performed"
        const val DELETION_SUCCESS = "Table Deleted Successfully"
    }
}
