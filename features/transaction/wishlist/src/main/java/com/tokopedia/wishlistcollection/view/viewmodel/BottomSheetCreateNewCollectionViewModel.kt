package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.CreateWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

class BottomSheetCreateNewCollectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase,
    private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase,
    private val createWishlistCollectionUseCase: CreateWishlistCollectionUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionNames = MutableLiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>()
    val collectionNames: LiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>
        get() = _collectionNames

    private val _addWishlistCollectionItem = MutableLiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>()
    val addWishlistCollectionItem: LiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>
        get() = _addWishlistCollectionItem

    private val _createWishlistCollectionResult = MutableLiveData<Result<CreateWishlistCollectionResponse.CreateWishlistCollection>>()
    val createWishlistCollectionResult: LiveData<Result<CreateWishlistCollectionResponse.CreateWishlistCollection>>
        get() = _createWishlistCollectionResult

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

    fun saveNewWishlistCollection(addWishlistParam: AddWishlistCollectionsHostBottomSheetParams) {
        launchCatchError(block = {
            val result = addWishlistCollectionItemsUseCase(addWishlistParam)
            if (result.addWishlistCollectionItems.status == WishlistV2CommonConsts.OK && result.addWishlistCollectionItems.errorMessage.isEmpty()) {
                _addWishlistCollectionItem.value = Success(result.addWishlistCollectionItems)
            } else {
                _addWishlistCollectionItem.value = Fail(Throwable())
            }
        }, onError = {
                _addWishlistCollectionItem.value = Fail(it)
            })
    }

    fun createNewWishlistCollection(collectionName: String) {
        launchCatchError(block = {
            val result = createWishlistCollectionUseCase(collectionName)
            if (result.createWishlistCollection.status == WishlistV2CommonConsts.OK && result.createWishlistCollection.errorMessage.isEmpty()) {
                _createWishlistCollectionResult.value = Success(result.createWishlistCollection)
            } else {
                _createWishlistCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
                _createWishlistCollectionResult.value = Fail(it)
            })
    }
}
