package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.response.CollectionWishlistResponse
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistCollectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionUseCase: GetWishlistCollectionUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collections = MutableLiveData<Result<CollectionWishlistResponse.Data.GetWishlistCollections>>()
    val collections: LiveData<Result<CollectionWishlistResponse.Data.GetWishlistCollections>>
        get() = _collections

    private val _deleteCollectionResult = MutableLiveData<Result<DeleteWishlistCollectionResponse.Data.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.Data.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    fun getWishlistCollections() {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) { getWishlistCollectionUseCase.executeOnBackground() }
            if (result is Success) {
                _collections.value = result
            } else {
                val error = (result as Fail).throwable
                _collections.value = Fail(error)
            }
        }
    }

    fun deleteWishlistCollection(collectionId: String) {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) {
                    deleteWishlistCollectionUseCase.setParams(collectionId)
                    deleteWishlistCollectionUseCase.executeOnBackground()
                }
            if (result is Success) {
                _deleteCollectionResult.value = result
            } else {
                val error = (result as Fail).throwable
                _deleteCollectionResult.value = Fail(error)
            }
        }
    }
}