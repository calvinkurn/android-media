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
    val getAllBrandHeaderResult = MutableLiveData<Result<OfficialStoreAllBrands>>()
    val getAllBrandResult = MutableLiveData<Result<OfficialStoreAllBrands>>()

    private var firstLetterChanged = false
    private var totalBrandSize = 0
    private var currentOffset = INITIAL_OFFSET
    private var currentLetter = INITIAL_LETTER

    fun getCurrentOffset(): Int {
        return currentOffset
    }

    fun getCurrentLetter(): String {
        return currentLetter.toString()
    }

    fun updateTotalBrandSize(totalBrandSize: Int) {
        this.totalBrandSize = totalBrandSize
    }

    fun updateCurrentOffset(renderedBrands: Int) {
        currentOffset += renderedBrands
    }

    fun updateCurrentLetter() {
        val firstLetter = getTheFirstLetter(totalBrandSize, currentOffset)
        if (firstLetter != currentLetter) {
            currentLetter = firstLetter
            firstLetterChanged = true
        }
    }

    fun updateAllBrandRequestParameter() {
        if (firstLetterChanged) {
            totalBrandSize = 0
            currentOffset = 0
            firstLetterChanged = false
        }
    }

    fun resetAllBrandRequestParameter() {
        firstLetterChanged = false
        totalBrandSize = 0
        currentOffset = INITIAL_OFFSET
        currentLetter = INITIAL_LETTER
    }

    private fun getTheFirstLetter(totalBrandSize: Int, currentOffset: Int): Char {
        return if (totalBrandSize == currentOffset) {
            val newLetter = currentLetter + 1
            newLetter
        } else currentLetter
    }

    fun loadInitialData(category: Category?, userId: String?) {
        launchCatchError(block = {

            getFeaturedBrandResult.postValue(Success(getFeaturedBrandsAsync(category?.categoryId).await()))
            getPopularBrandResult.postValue(Success(getPopularBrandsAsync(userId, category?.categoryId).await()))
            getNewBrandResult.postValue(Success(getNewBrandsAsync(userId, category?.categoryId).await()))

            getAllBrandHeaderResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_HEADER_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    "").await()))

            getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    INITIAL_LETTER.toString()).await()))

        }, onError = {})
    }

    fun loadMoreAllBrands(category: Category?) {

        val requestSize = geRequestSize(totalBrandSize, currentOffset)

        launchCatchError(block = {
            getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    currentOffset,
                    ALL_BRANDS_QUERY,
                    requestSize,
                    ALPHABETIC_ASC_SORT,
                    currentLetter.toString()).await()))
        }, onError = {})
    }

    private fun geRequestSize(totalBrandSize: Int, renderedBrands: Int): Int {
        if (renderedBrands == 0) return ALL_BRANDS_REQUEST_SIZE
        val remainingBrands = totalBrandSize - renderedBrands
        return if (remainingBrands > ALL_BRANDS_REQUEST_SIZE) {
            ALL_BRANDS_REQUEST_SIZE
        } else {
            remainingBrands
        }
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
        private const val ALL_BRANDS_HEADER_REQUEST_SIZE = 1
        private const val ALL_BRANDS_REQUEST_SIZE = 30
        private const val ALPHABETIC_ASC_SORT = 3
        private const val INITIAL_LETTER = 'a'
    }
}