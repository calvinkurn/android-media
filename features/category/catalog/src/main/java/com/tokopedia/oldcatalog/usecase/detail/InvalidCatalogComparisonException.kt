package com.tokopedia.oldcatalog.usecase.detail

import java.io.IOException

class InvalidCatalogComparisonException(
    val invalidCatalogCount: Int
): IOException()
