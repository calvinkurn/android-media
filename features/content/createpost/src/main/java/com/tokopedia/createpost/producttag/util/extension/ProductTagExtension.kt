package com.tokopedia.createpost.producttag.util.extension

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
val Set<ProductTagSource>.currentSource: ProductTagSource
    get() = lastOrNull() ?: ProductTagSource.Unknown