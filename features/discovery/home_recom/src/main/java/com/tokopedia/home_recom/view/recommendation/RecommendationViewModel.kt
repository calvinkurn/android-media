package com.tokopedia.home_recom.view.recommendation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.graphql.domain.GraphqlRepository
import com.tokopedia.home_recom.model.dataModel.ProductDataModel
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
        gqlRepository: GraphqlRepository
) : ViewModel() {

    val data = MutableLiveData<Result<ProductInfoDataModel>>().apply {
        value = Success(ProductInfoDataModel(ProductDataModel.empty()))
    }

}