package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionByIdUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcollection.domain.UpdateWishlistCollectionUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

class WishlistCollectionEditViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase,
    private val updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase,
    private val getWishlistCollectionByIdUseCase: GetWishlistCollectionByIdUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionNames = MutableLiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>()
    val collectionNames: LiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>
        get() = _collectionNames

    private val _updateWishlistCollectionResult = MutableLiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>()
    val updateWishlistCollectionResult: LiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>
        get() = _updateWishlistCollectionResult

    private val _getWishlistCollectionByIdResult = MutableLiveData<Result<GetWishlistCollectionByIdResponse.GetWishlistCollectionById>>()
    val getWishlistCollectionByIdResult: LiveData<Result<GetWishlistCollectionByIdResponse.GetWishlistCollectionById>>
        get() = _getWishlistCollectionByIdResult

    private val _deleteCollectionResult =
        MutableLiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    fun getWishlistCollectionNames() {
        launchCatchError(block = {
            val result = getWishlistCollectionNamesUseCase(Unit)
            if (result.getWishlistCollectionNames.status == WishlistV2CommonConsts.OK && result.getWishlistCollectionNames.errorMessage.isEmpty()) {
                _collectionNames.value = Success(result.getWishlistCollectionNames)
            } else {
                _collectionNames.value = Fail(Throwable())
            }
        }, onError = {
            _collectionNames.value = Fail(it)
        })
    }

    fun updateAccessWishlistCollection(updateWishlistCollectionParams: UpdateWishlistCollectionParams) {
        launchCatchError(block = {
            val result = updateWishlistCollectionUseCase(updateWishlistCollectionParams)
            if (result.updateWishlistCollection.status == WishlistV2CommonConsts.OK && result.updateWishlistCollection.data.success) {
                _updateWishlistCollectionResult.value = Success(result.updateWishlistCollection)
            } else {
                _updateWishlistCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
            _updateWishlistCollectionResult.value = Fail(it)
        })
    }

    fun getWishlistCollectionById(collectonId: String) {
        launchCatchError(block = {
            val result = getWishlistCollectionByIdUseCase(collectonId)
            if (result.getWishlistCollectionById.status == WishlistV2CommonConsts.OK) {
                _getWishlistCollectionByIdResult.value = Success(result.getWishlistCollectionById)
            } else {
                _getWishlistCollectionByIdResult.value = Fail(Throwable())
            }
        }, onError = {
            _getWishlistCollectionByIdResult.value = Fail(it)
        })
    }

    fun deleteWishlistCollection(collectionId: String) {
        launchCatchError(block = {
            val result = deleteWishlistCollectionUseCase(collectionId)
            if (result.deleteWishlistCollection.status == WishlistV2CommonConsts.OK && result.deleteWishlistCollection.errorMessage.isEmpty()) {
                _deleteCollectionResult.value = Success(result.deleteWishlistCollection)
            } else {
                _deleteCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
            _deleteCollectionResult.value = Fail(it)
        })
    }
}
