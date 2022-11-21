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
    private val getWishlistCollectionUseCase: GetWishlistCollectionUseCase,
    private val getWishlistCollectionByIdUseCase: GetWishlistCollectionByIdUseCase,
    private val updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _wishlistCollection = MutableLiveData<SharingWishlistStateResult<WishlistDataModel>>()
    val wishlistCollection: LiveData<SharingWishlistStateResult<WishlistDataModel>>
        get() = _wishlistCollection

    private val _wishlistCollectionById = MutableLiveData<PrivacyCenterStateResult<WishlistBydIdDataModel>>()
    val wishlistCollectionById: LiveData<PrivacyCenterStateResult<WishlistBydIdDataModel>>
        get() = _wishlistCollectionById

    private val _updateWishlistCollection = MutableLiveData<PrivacyCenterStateResult<UpdateWishlistDataModel>>()
    val updateWishlistCollection: LiveData<PrivacyCenterStateResult<UpdateWishlistDataModel>>
        get() = _updateWishlistCollection

    fun getWishlistCollections(collectionAccess: Int) {
        _wishlistCollection.value = SharingWishlistStateResult.Loading()
        launchCatchError(coroutineContext, {
            _wishlistCollection.value = getWishlistCollectionUseCase(collectionAccess)
        }, {
            _wishlistCollection.value = SharingWishlistStateResult.Fail(it)
        })
    }

    fun getCollectionById(collectionId: Int) {
        _wishlistCollectionById.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _wishlistCollectionById.value = getWishlistCollectionByIdUseCase(collectionId)
        }, {
            _wishlistCollectionById.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    fun updateWishlistCollection(params: WishlistCollectionByIdDataModel) {
        _updateWishlistCollection.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _updateWishlistCollection.value = updateWishlistCollectionUseCase(params)
        }, {
            _updateWishlistCollection.value = PrivacyCenterStateResult.Fail(it)
        })
    }
}
