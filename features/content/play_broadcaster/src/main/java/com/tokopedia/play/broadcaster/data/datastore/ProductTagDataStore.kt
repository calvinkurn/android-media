package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import kotlinx.coroutines.flow.Flow

interface ProductTagDataStore {

    fun getObservableProductTag(): Flow<List<ProductTagSectionUiModel>>

    fun getProductTag(): List<ProductTagSectionUiModel>

    fun setProductTag(productTag: List<ProductTagSectionUiModel>)
}
