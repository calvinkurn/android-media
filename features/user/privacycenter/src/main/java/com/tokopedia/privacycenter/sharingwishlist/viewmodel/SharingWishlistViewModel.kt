package com.tokopedia.privacycenter.sharingwishlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.sharingwishlist.domain.data.UpdateWishlistDataModel
import com.tokopedia.privacycenter.sharingwishlist.domain.data.WishlistBydIdDataModel
import com.tokopedia.privacycenter.sharingwishlist.domain.data.WishlistCollectionByIdDataModel
import com.tokopedia.privacycenter.sharingwishlist.domain.data.WishlistDataModel
import com.tokopedia.privacycenter.sharingwishlist.domain.usecase.GetWishlistCollectionByIdUseCase
import com.tokopedia.privacycenter.sharingwishlist.domain.usecase.GetWishlistCollectionUseCase
import com.tokopedia.privacycenter.sharingwishlist.domain.usecase.UpdateWishlistCollectionUseCase
import com.tokopedia.privacycenter.sharingwishlist.ui.SharingWishlistStateResult
import javax.inject.Inject

class SharingWishlistViewModel @Inject constructor(
    private val getWishlistCollection: GetWishlistCollectionUseCase,
    private val getWishlistCollectionById: GetWishlistCollectionByIdUseCase,
    private val updateWishlistCollection: UpdateWishlistCollectionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _wishlistCollection = MutableLiveData<SharingWishlistStateResult<WishlistDataModel>>()
    val wishlistCollection: LiveData<SharingWishlistStateResult<WishlistDataModel>>
        get() = _wishlistCollection

    private val _wishlistCollectionById = MutableLiveData<PrivacyCenterStateResult<WishlistBydIdDataModel>>()
    val wishlistCollectionById: LiveData<PrivacyCenterStateResult<WishlistBydIdDataModel>>
        get() = _wishlistCollectionById

    private val _updateWishlist = MutableLiveData<PrivacyCenterStateResult<UpdateWishlistDataModel>>()
    val updateWishlist: LiveData<PrivacyCenterStateResult<UpdateWishlistDataModel>>
        get() = _updateWishlist

    fun getWishlistCollections(collectionAccess: Int) {
        _wishlistCollection.value = SharingWishlistStateResult.Loading()
        launchCatchError(block =  {
            _wishlistCollection.value = getWishlistCollection(collectionAccess)
        }, onError = {
            _wishlistCollection.value = SharingWishlistStateResult.Fail(it)
        })
    }

    fun getCollectionById(collectionId: Int) {
        _wishlistCollectionById.value = PrivacyCenterStateResult.Loading()
        launchCatchError(block = {
            _wishlistCollectionById.value = getWishlistCollectionById(collectionId)
        }, onError = {
            _wishlistCollectionById.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    fun updateWishlist(params: WishlistCollectionByIdDataModel) {
        _updateWishlist.value = PrivacyCenterStateResult.Loading()
        launchCatchError( block = {
            _updateWishlist.value = updateWishlistCollection(params)
        }, onError = {
            _updateWishlist.value = PrivacyCenterStateResult.Fail(it)
        })
    }
}
