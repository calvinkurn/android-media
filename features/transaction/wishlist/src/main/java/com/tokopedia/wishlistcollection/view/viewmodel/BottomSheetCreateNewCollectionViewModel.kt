package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BottomSheetCreateNewCollectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase,
    private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionNames = MutableLiveData<Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames>>()
    val collectionNames: LiveData<Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames>>
        get() = _collectionNames

    private val _addWishlistCollectionItem = MutableLiveData<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>()
    val addWishlistCollectionItem: LiveData<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>
        get() = _addWishlistCollectionItem

    fun getWishlistCollectionNames() {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) { getWishlistCollectionNamesUseCase.executeOnBackground() }
            if (result is Success) {
                _collectionNames.value = result
            } else {
                val error = (result as Fail).throwable
                _collectionNames.value = Fail(error)
            }
        }
    }

    fun saveNewWishlistCollection(collectionName: String, productIds: List<String>) {
        val addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionName = collectionName, productIds = productIds)
        addWishlistCollectionItemsUseCase.setParams(addWishlistParam)
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) { addWishlistCollectionItemsUseCase.executeOnBackground() }
            if (result is Success) {
                _addWishlistCollectionItem.value = result
            } else {
                val error = (result as Fail).throwable
                _addWishlistCollectionItem.value = Fail(error)
            }
        }
    }
}