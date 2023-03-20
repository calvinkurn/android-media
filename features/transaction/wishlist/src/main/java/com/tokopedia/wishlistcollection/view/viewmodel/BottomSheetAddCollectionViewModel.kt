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
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionsBottomSheetUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

class BottomSheetAddCollectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionsBottomSheetUseCase: GetWishlistCollectionsBottomSheetUseCase,
    private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionsBottomSheet =
        MutableLiveData<Result<com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet>>()
    val collectionsBottomSheet: LiveData<Result<com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet>>
        get() = _collectionsBottomSheet

    private val _saveItemToCollections =
        MutableLiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>()
    val saveItemToCollections: LiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>
        get() = _saveItemToCollections

    fun getWishlistCollections(param: GetWishlistCollectionsBottomSheetParams) {
        launchCatchError(block = {
            val result = getWishlistCollectionsBottomSheetUseCase(param)
            if (result.getWishlistCollectionsBottomsheet.status == WishlistV2CommonConsts.OK && result.getWishlistCollectionsBottomsheet.errorMessage.isEmpty()) {
                _collectionsBottomSheet.value = Success(result.getWishlistCollectionsBottomsheet)
            } else {
                _collectionsBottomSheet.value = Fail(Throwable())
            }
        }, onError = {
                _collectionsBottomSheet.value = Fail(it)
            })
    }

    fun saveToWishlistCollection(param: AddWishlistCollectionsHostBottomSheetParams) {
        launchCatchError(block = {
            val result = addWishlistCollectionItemsUseCase(param)
            if (result.addWishlistCollectionItems.status == WishlistV2CommonConsts.OK && result.addWishlistCollectionItems.errorMessage.isEmpty()) {
                _saveItemToCollections.value = Success(result.addWishlistCollectionItems)
            } else {
                _saveItemToCollections.value = Fail(Throwable())
            }
        }, onError = {
                _saveItemToCollections.value = Fail(it)
            })
    }
}
