package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CatalogListingViewModel @Inject constructor(private val repository : CatalogListingRepository) : BaseViewModel(Dispatchers.Main), CatalogListingContract.Presenter {
    private var pointRange = 0
    private var currentCategoryId = 0
    private var currentSubCategoryId = 0

    val bannerLiveDate = MutableLiveData<Resources<CatalogBannerBase>>()
    val filterLiveData = MutableLiveData<Resources<CatalogFilterBase>>()
    val pointLiveData = MutableLiveData<Resources<TokoPointStatusEntity>>()

    override fun getHomePageData(slugCategory: String?, slugSubCategory: String?, isBannerRequire: Boolean) {
        launchCatchError(block = {
            filterLiveData.value = Loading()
            val graphqlResponse = repository.getHomePageData(slugCategory,slugSubCategory, isBannerRequire)
            val outer = graphqlResponse.getData<CatalogBannerOuter>(CatalogBannerOuter::class.java)
            if (outer == null || outer.bannerData == null || outer.bannerData.banners == null) {
                bannerLiveDate.value = ErrorMessage("")
            } else {
                bannerLiveDate.value = Success(outer.bannerData)
            }
            //handling the catalog listing and tabs
            val catalogFilterOuter = graphqlResponse.getData<CatalogFilterOuter>(CatalogFilterOuter::class.java)
            if (catalogFilterOuter == null || catalogFilterOuter.filter == null) {
                filterLiveData.value = ErrorMessage("")
            } else {
                filterLiveData.value = Success(catalogFilterOuter.filter)
            }
        }){
            filterLiveData.value = ErrorMessage("")
        }
    }

    override fun getPointData() {
        launchCatchError(block = {
           val  pointDetailEntity = repository.getPointData()
            if ( pointDetailEntity.tokoPoints == null || pointDetailEntity.tokoPoints.resultStatus == null || pointDetailEntity.tokoPoints.status == null || pointDetailEntity.tokoPoints.status.points == null) {
                pointLiveData.value = ErrorMessage("")
            } else {
                if (pointDetailEntity.tokoPoints.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    pointLiveData.value = Success(pointDetailEntity.tokoPoints.status)
                }
            }
        }){}
    }

    override fun setPointRangeId(id: Int) {
        pointRange = id
    }

    override fun getPointRangeId(): Int {
        return pointRange
    }

    override fun setCurrentCategoryId(id: Int) {
        currentCategoryId = id
    }

    override fun getCurrentCategoryId(): Int {
        return currentCategoryId
    }

    override fun setCurrentSubCategoryId(id: Int) {
        currentSubCategoryId = id
    }

    override fun getCurrentSubCategoryId(): Int {
        return currentSubCategoryId
    }

}