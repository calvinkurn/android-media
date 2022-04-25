package com.tokopedia.createpost.producttag.view.uimodel.action

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
sealed interface ProductTagAction {
    data class SelectProductTagSource(val source: ProductTagSource): ProductTagAction
}