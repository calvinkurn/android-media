package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistCollectionHostBottomSheetViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase
) : BaseViewModel(dispatcher.main) {

    private val _addWishlistCollectionItem = MutableLiveData<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>()
    val addWishlistCollectionItem: LiveData<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>
        get() = _addWishlistCollectionItem

    fun saveToWishlistCollection(collectionName: String, productIds: List<String>) {
        addWishlistCollectionItemsUseCase.setParams(collectionName, productIds)
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