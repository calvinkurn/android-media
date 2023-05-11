package com.tokopedia.catalog_library.util

import java.lang.Exception

class CatalogLibraryResponseException(_message : String, val shimmerType : String) : Exception() {
    override val message: String = _message
}
