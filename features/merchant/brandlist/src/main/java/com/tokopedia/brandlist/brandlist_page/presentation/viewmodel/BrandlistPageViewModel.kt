package com.tokopedia.brandlist.brandlist_page.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistFeaturedBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistNewBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class BrandlistPageViewModel @Inject constructor(
        private val getBrandListFeaturedBrandUseCase: GetBrandlistFeaturedBrandUseCase,
        private val getBrandListPopularBrandUseCase: GetBrandlistPopularBrandUseCase,
        private val getBrandListNewBrandUseCase: GetBrandlistNewBrandUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val getFeaturedBrandResult = MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    val getPopularBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val getNewBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()

    fun loadInitialData(category: Category?, userId: String?) {
        launchCatchError(block = {
            getFeaturedBrandResult.value = Success(getFeaturedBrandsAsync(category?.categoryId).await())
            getPopularBrandResult.value = Success(getPopularBrandsAsync(userId, category?.categoryId).await())
            getNewBrandResult.value = Success(getNewBrandsAsync(userId, category?.categoryId).await())
        }, onError = {
            getFeaturedBrandResult.value = Fail(it)
            getPopularBrandResult.value = Fail(it)
            getNewBrandResult.value = Fail(it)
        })
    }

    private fun getFeaturedBrandsAsync(categoryId: String?): Deferred<OfficialStoreFeaturedShop> {
        return async(Dispatchers.IO) {
            var featuredbrand = OfficialStoreFeaturedShop()
            try {
                getBrandListFeaturedBrandUseCase.params = GetBrandlistFeaturedBrandUseCase
                        .createParams(categoryId?.toIntOrNull() ?: 0)
                featuredbrand = getBrandListFeaturedBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                getFeaturedBrandResult.value = Fail(t)
            }
            featuredbrand
        }
    }

    private fun getPopularBrandsAsync(userId: String?, categoryId: String?): Deferred<OfficialStoreBrandsRecommendation> {
        return async(Dispatchers.IO) {
            var popularBrand = OfficialStoreBrandsRecommendation()
            try {
                getBrandListPopularBrandUseCase.params = GetBrandlistPopularBrandUseCase
                        .createParams(userId?.toIntOrNull() ?: 0, categoryId?.toIntOrNull() ?: 0)
                popularBrand = getBrandListPopularBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                getPopularBrandResult.value = Fail(t)
            }
            popularBrand
        }
    }

    private fun getNewBrandsAsync(userId: String?, categoryId: String?): Deferred<OfficialStoreBrandsRecommendation> {
        return async(Dispatchers.IO) {
            var newBrand = OfficialStoreBrandsRecommendation()
            try {
                getBrandListNewBrandUseCase.params = GetBrandlistNewBrandUseCase
                        .createParams(userId?.toIntOrNull() ?: 0, categoryId?.toIntOrNull() ?: 0)
                newBrand = getBrandListNewBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                getNewBrandResult.value = Fail(t)
            }
            newBrand
        }
    }

    private fun getAllBrandAsync() {

    }
}