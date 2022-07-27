package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.WishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import javax.inject.Inject

class WishlistCollectionViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionUseCase: GetWishlistCollectionUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collections =
        MutableLiveData<Result<WishlistCollectionResponse.GetWishlistCollections>>()
    val collections: LiveData<Result<WishlistCollectionResponse.GetWishlistCollections>>
        get() = _collections

    private val _deleteCollectionResult =
        MutableLiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    fun getWishlistCollections() {
        launchCatchError(block = {
            val result = getWishlistCollectionUseCase(Unit)
            if (result.getWishlistCollections.status == OK && result.getWishlistCollections.errorMessage.isEmpty()) {
                _collections.postValue(Success(result.getWishlistCollections))
            } else {
                _collections.postValue(Fail(Throwable()))
            }
        }, onError = {
            _collections.postValue(Fail(it))
        })
    }

    fun deleteWishlistCollection(collectionId: String) {
        launchCatchError(block = {
            val result = deleteWishlistCollectionUseCase(collectionId)
            if (result.deleteWishlistCollection.status == OK && result.deleteWishlistCollection.errorMessage.isEmpty()) {
                _deleteCollectionResult.postValue(Success(result.deleteWishlistCollection))
            } else {
                _deleteCollectionResult.postValue(Fail(Throwable()))
            }
        }, onError = {
            _deleteCollectionResult.postValue(Fail(it))
        })
    }
}