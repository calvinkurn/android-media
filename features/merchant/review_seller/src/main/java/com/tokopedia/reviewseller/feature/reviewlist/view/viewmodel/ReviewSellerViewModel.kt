package com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.reviewseller.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.reviewseller.feature.reviewlist.domain.GetReviewProductListUseCase
import com.tokopedia.reviewseller.feature.reviewlist.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReviewSellerViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val getProductRatingOverallUseCase: GetProductRatingOverallUseCase,
        private val getReviewProductListUseCase: GetReviewProductListUseCase
        ) : BaseViewModel(dispatcherProvider.main()) {

    private val _reviewProductList = MutableLiveData<Pair<Boolean, List<ProductReviewUiModel>>>()
    val reviewProductList: LiveData<Pair<Boolean, List<ProductReviewUiModel>>> = _reviewProductList

    fun getProductRatingData() {


    }




}