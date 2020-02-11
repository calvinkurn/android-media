package com.tokopedia.brandlist.brandlist_page.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
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
        private val getBrandlistAllBrandUseCase: GetBrandlistAllBrandUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val getFeaturedBrandResult = MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    val getPopularBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val getNewBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val getAllBrandResult = MutableLiveData<Result<OfficialStoreAllBrands>>()

    var newOffset = INITIAL_OFFSET

    fun loadInitialData(category: Category?, userId: String?) {
        launchCatchError(block = {
            getFeaturedBrandResult.postValue(Success(getFeaturedBrandsAsync(category?.categoryId).await()))
            getPopularBrandResult.postValue(Success(getPopularBrandsAsync(userId, category?.categoryId).await()))
            getNewBrandResult.postValue(Success(getNewBrandsAsync(userId, category?.categoryId).await()))
            getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    FIRST_LETTER).await()))
        }, onError = {})
    }

    fun loadMoreAllBrands(category: Category?) {
        launchCatchError(block = {
            newOffset += ALL_BRANDS_REQUEST_SIZE
            getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    newOffset,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    FIRST_LETTER).await()))
        }, onError = {})
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

    private fun getAllBrandAsync(categoryId: String?,
                                 offset: Int,
                                 query: String,
                                 brandSize: Int,
                                 sortType: Int,
                                 firstLetter: String): Deferred<OfficialStoreAllBrands> {
        return async(Dispatchers.IO) {
            var allBrand = OfficialStoreAllBrands()
            try {
                getBrandlistAllBrandUseCase.params = GetBrandlistAllBrandUseCase
                        .createParams(categoryId?.toIntOrNull()
                                ?: 0, offset, query, brandSize, sortType, firstLetter)
                allBrand = getBrandlistAllBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                getAllBrandResult.value = Fail(t)
            }
            allBrand
        }
    }

    companion object {
        private const val INITIAL_OFFSET = 0
        private const val ALL_BRANDS_QUERY = ""
        private const val ALL_BRANDS_REQUEST_SIZE = 9
        private const val ALPHABETIC_ASC_SORT = 3
        private const val FIRST_LETTER = "a"
    }
}