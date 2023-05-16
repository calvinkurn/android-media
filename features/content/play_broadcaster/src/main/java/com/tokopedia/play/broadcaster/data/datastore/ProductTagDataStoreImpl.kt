package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProductTagDataStoreImpl @Inject constructor() : ProductTagDataStore {

    private val _observableProductTag: MutableStateFlow<List<ProductTagSectionUiModel>> =
        MutableStateFlow(emptyList())

    private val mProductTag: List<ProductTagSectionUiModel>
        get() = _observableProductTag.value

    override fun getObservableProductTag(): Flow<List<ProductTagSectionUiModel>> {
        return _observableProductTag
    }

    override fun getProductTag(): List<ProductTagSectionUiModel> {
        return mProductTag
    }

    override fun setProductTag(productTag: List<ProductTagSectionUiModel>) {
        _observableProductTag.value = productTag
    }

}
