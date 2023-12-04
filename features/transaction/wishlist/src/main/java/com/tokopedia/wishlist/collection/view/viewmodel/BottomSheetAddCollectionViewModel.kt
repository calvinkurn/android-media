package com.tokopedia.wishlist.collection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.collection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlist.collection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlist.collection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlist.collection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlist.collection.domain.GetWishlistCollectionsBottomSheetUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

class BottomSheetAddCollectionViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val getWishlistCollectionsBottomSheetUseCase: GetWishlistCollectionsBottomSheetUseCase,
        private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionsBottomSheet =
        MutableLiveData<Result<GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet>>()
    val collectionsBottomSheet: LiveData<Result<GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet>>
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
