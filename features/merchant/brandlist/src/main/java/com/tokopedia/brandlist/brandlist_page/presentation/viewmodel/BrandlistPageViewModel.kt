package com.tokopedia.brandlist.brandlist_page.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistFeaturedBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject

class BrandlistPageViewModel @Inject constructor(
        private val getBrandListFeaturedBrandUseCase: GetBrandlistFeaturedBrandUseCase,
        private val getBrandListPopularBrandUseCase: GetBrandlistPopularBrandUseCase,
        private val getBrandListAllBrandUseCase: GetBrandlistAllBrandUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _getFeaturedBrandResult = MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    val getFeaturedBrandResult: LiveData<Result<OfficialStoreFeaturedShop>>
        get() = _getFeaturedBrandResult

    private val _getPopularBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val getPopularBrandResult: LiveData<Result<OfficialStoreBrandsRecommendation>>
        get() = _getPopularBrandResult

    private val _getNewBrandResult = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val getNewBrandResult: LiveData<Result<OfficialStoreBrandsRecommendation>>
        get() = _getNewBrandResult

    private val _getAllBrandHeaderResult = MutableLiveData<Result<OfficialStoreAllBrands>>()
    val getAllBrandHeaderResult: LiveData<Result<OfficialStoreAllBrands>>
        get() = _getAllBrandHeaderResult

    private val _getAllBrandResult = MutableLiveData<Result<OfficialStoreAllBrands>>()
    val getAllBrandResult: LiveData<Result<OfficialStoreAllBrands>>
        get() = _getAllBrandResult

    private var firstLetterChanged = false
    private var totalBrandSize = 0
    private var currentOffset = INITIAL_OFFSET


    fun updateTotalBrandSize(totalBrandSize: Int) {
        this.totalBrandSize = totalBrandSize
    }

    fun updateCurrentOffset(renderedBrands: Int) {
        currentOffset += renderedBrands
    }

    fun getTotalBrandSize(): Int = totalBrandSize
    fun getFirstLetterChanged(): Boolean = firstLetterChanged
    fun getCurrentOffset(): Int = currentOffset
    fun setOffset() {
        currentOffset = 100
    }

    fun resetAllBrandRequestParameter() {
        firstLetterChanged = false
        totalBrandSize = 0
        currentOffset = INITIAL_OFFSET
    }

    fun loadInitialData(category: Category?, userId: String?) {
        launchCatchError(block = {

            _getFeaturedBrandResult.postValue(Success(getFeaturedBrandsAsync(category?.categoryId).await()))
            _getPopularBrandResult.postValue(Success(getPopularBrandsAsync(userId, category?.categories).await()))
            _getNewBrandResult.postValue(Success(getNewBrandsAsync(userId, category?.categories).await()))

            _getAllBrandHeaderResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_HEADER_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    "").await()))

            _getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    INITIAL_LETTER.toString()).await()))

        }, onError = {})
    }

    fun loadBrandsPerAlphabet(category: Category?, brandFirstLetter: String) {
        launchCatchError(block = {
            _getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    brandFirstLetter).await()))
        }, onError = {})
    }

    fun loadAllBrands(category: Category?) {
        launchCatchError(block = {
            _getAllBrandHeaderResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_HEADER_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    "").await()))

            _getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    INITIAL_OFFSET,
                    ALL_BRANDS_QUERY,
                    ALL_BRANDS_REQUEST_SIZE,
                    ALPHABETIC_ASC_SORT,
                    INITIAL_LETTER.toString()).await()))
        }, onError = {})
    }

    fun loadMoreAllBrands(category: Category?, brandFirstLetter: String) {
        val requestSize = geRequestSize(totalBrandSize, currentOffset)
        launchCatchError(block = {
            _getAllBrandResult.postValue(Success(getAllBrandAsync(
                    category?.categoryId,
                    currentOffset,
                    ALL_BRANDS_QUERY,
                    requestSize,
                    ALPHABETIC_ASC_SORT,
                    brandFirstLetter).await()))
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
        return async(dispatchers.io) {
            var featuredbrand = OfficialStoreFeaturedShop()
            try {
                getBrandListFeaturedBrandUseCase.params = GetBrandlistFeaturedBrandUseCase
                        .createParams(categoryId?.toIntOrNull() ?: 0)
                featuredbrand = getBrandListFeaturedBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _getFeaturedBrandResult.value = Fail(t)
            }
            featuredbrand
        }
    }

    private fun getPopularBrandsAsync(userId: String?, categoryId: ArrayList<Int>?): Deferred<OfficialStoreBrandsRecommendation> {
        return async(dispatchers.io) {
            var popularBrand = OfficialStoreBrandsRecommendation()
            val categories = categoryId.toString().replace(" ","")
            try {
                getBrandListPopularBrandUseCase.params = GetBrandlistPopularBrandUseCase
                        .createParams(userId?.toIntOrNull() ?: 0,
                                categories.substring(1,categories.length-1),
                                GetBrandlistPopularBrandUseCase.POPULAR_WIDGET_NAME
                        )
                popularBrand = getBrandListPopularBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _getPopularBrandResult.value = Fail(t)
            }
            popularBrand
        }
    }

    private fun getNewBrandsAsync(userId: String?, categoryId: ArrayList<Int>?): Deferred<OfficialStoreBrandsRecommendation> {
        return async(dispatchers.io) {
            var newBrand = OfficialStoreBrandsRecommendation()
            val categories = categoryId.toString().replace(" ","")
            try {
                getBrandListPopularBrandUseCase.params = GetBrandlistPopularBrandUseCase
                        .createParams(
                                userId?.toIntOrNull() ?: 0,
                                categories.substring(1,categories.length-1),
                                GetBrandlistPopularBrandUseCase.NEW_WIDGET_NAME
                        )
                newBrand = getBrandListPopularBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _getNewBrandResult.value = Fail(t)
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
        return async(dispatchers.io) {
            var allBrand = OfficialStoreAllBrands()
            try {
                getBrandListAllBrandUseCase.params = GetBrandlistAllBrandUseCase
                        .createParams(categoryId?.toIntOrNull()
                                ?: 0, offset, query, brandSize, sortType, firstLetter)
                allBrand = getBrandListAllBrandUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _getAllBrandResult.value = Fail(t)
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