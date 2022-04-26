package com.tokopedia.createpost.producttag.domain.repository

import com.tokopedia.createpost.producttag.view.uimodel.LastTaggedProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
interface ProductTagRepository {

    suspend fun getLastTaggedProducts(
        authorId: String,
        authorType: String,
        cursor: String,
        limit: Int,
    ) : LastTaggedProductUiModel
}