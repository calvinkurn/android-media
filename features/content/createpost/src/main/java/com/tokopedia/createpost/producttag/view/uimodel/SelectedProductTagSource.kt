package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class SelectedProductTagSource(
    val source: ProductTagSource,
    val needAddToBackStack: Boolean,
) {
    companion object {
        val Empty = SelectedProductTagSource(
            source = ProductTagSource.Unknown,
            needAddToBackStack = false,
        )
    }
}