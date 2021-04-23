package com.tokopedia.recentview.view.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author by yoasfs on 13/08/20
 */

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class RecentViewViewModel @Inject constructor(
        baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val recentViewUseCase: RecentViewUseCase
): BaseViewModel(baseDispatcher.io) {


    val recentViewDetailProductDataResp : LiveData<Result<ArrayList<RecentViewDetailProductDataModel>>>
        get() = _recentViewDetailProductDataResp
    private val _recentViewDetailProductDataResp : MutableLiveData<Result<ArrayList<RecentViewDetailProductDataModel>>> = MutableLiveData()

    val addWishlistResponse: LiveData<Result<String>> get() = _addWishlistResponse
    private val _addWishlistResponse = MutableLiveData<Result<String>>()

    val removeWishlistResponse: LiveData<Result<String>> get() = _removeWishlistResponse
    private val _removeWishlistResponse = MutableLiveData<Result<String>>()

    fun getRecentView() {
        recentViewUseCase.apply {
            getParam(userSession.userId)
        }.execute({
            _recentViewDetailProductDataResp.postValue(Success(it))
        }, {
            _recentViewDetailProductDataResp.postValue(Fail(it))
        })
    }

    fun addToWishlist(adapterPosition: Int, productId: String) {
        addWishListUseCase.createObservable(productId,
                userSession.userId, object : WishListActionListener{
            override fun onErrorAddWishList(errorMessage: String, productId: String) {
                _addWishlistResponse.postValue(Fail(Throwable(errorMessage)))
            }

            override fun onSuccessAddWishlist(productId: String) {
                _addWishlistResponse.postValue(Success(productId))
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

            override fun onSuccessRemoveWishlist(productId: String?) {}
        })
    }

    fun removeFromWishlist(adapterPosition: Int, productId: String) {
        removeWishListUseCase.createObservable(productId,
                userSession.userId,  object : WishListActionListener{
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

            override fun onSuccessAddWishlist(productId: String?) {}

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                _removeWishlistResponse.postValue(Fail(Throwable(errorMessage)))
            }

            override fun onSuccessRemoveWishlist(productId: String) {
                _removeWishlistResponse.postValue(Success(productId))
            }
        })
    }

}