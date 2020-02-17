package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.contract.CatalogListingContract
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.NetworkDetector
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class CatalogListingPresenter @Inject constructor(private val mGetHomePageData: GraphqlUseCase?, private val mGetPointData: GraphqlUseCase?) : BaseDaggerPresenter<CatalogListingContract.View?>(), CatalogListingContract.Presenter {
    private var pointRange = 0
    private var currentCategoryId = 0
    private var currentSubCategoryId = 0
    override fun destroyView() {
        mGetHomePageData?.unsubscribe()
        mGetPointData?.unsubscribe()
    }

    override fun getHomePageData(slugCategory: String, slugSubCategory: String, isBannerRequire: Boolean) {
        if (view == null) {
            return
        }
        mGetHomePageData!!.clearRequest()
        view!!.showLoader()
        if (isBannerRequire) {
            val variablesBanner: MutableMap<String, Any> = HashMap()
            variablesBanner[CommonConstant.GraphqlVariableKeys.DEVICE] = CommonConstant.DEVICE_ID_BANNER
            val graphqlRequestBanners = GraphqlRequest(GraphqlHelper.loadRawString(view!!.resources, R.raw.tp_gql_catalog_banners),
                    CatalogBannerOuter::class.java,
                    variablesBanner, false)
            mGetHomePageData.addRequest(graphqlRequestBanners)
        }
        val graphqlRequestTokenDetail = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_tokopoint_detail),
                TokoPointDetailEntity::class.java, false)
        mGetHomePageData.addRequest(graphqlRequestTokenDetail)
        val variableFilter: MutableMap<String, Any> = HashMap()
        variableFilter[CommonConstant.GraphqlVariableKeys.SLUG_CATEGORY] = slugCategory
        variableFilter[CommonConstant.GraphqlVariableKeys.SLUG_SUB_CATEGORY] = slugSubCategory
        val graphqlRequestFilter = GraphqlRequest(GraphqlHelper.loadRawString(view!!.resources, R.raw.tp_gql_catalog_filter),
                CatalogFilterOuter::class.java,
                variableFilter, false)
        mGetHomePageData.addRequest(graphqlRequestFilter)
        val graphqlRequestEgg = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_lucky_egg_details),
                TokenDetailOuter::class.java, false)
        mGetHomePageData.addRequest(graphqlRequestEgg)
        mGetHomePageData.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                view!!.onErrorFilter("null", NetworkDetector.isConnectedToInternet(view!!.activityContext))
            }

            override fun onNext(graphqlResponse: GraphqlResponse) { //Handling the banner
                val outer = graphqlResponse.getData<CatalogBannerOuter>(CatalogBannerOuter::class.java)
                if (outer == null || outer.bannerData == null || outer.bannerData.banners == null) {
                    view!!.onErrorBanners(null)
                } else {
                    view!!.onSuccessBanners(outer.bannerData.banners)
                }
                //handling the catalog listing and tabs
                val catalogFilterOuter = graphqlResponse.getData<CatalogFilterOuter>(CatalogFilterOuter::class.java)
                if (catalogFilterOuter == null || catalogFilterOuter.filter == null) {
                    view!!.onErrorFilter(null, NetworkDetector.isConnectedToInternet(view!!.activityContext))
                } else {
                    view!!.onSuccessFilter(catalogFilterOuter.filter)
                }
            }
        })
    }

    override fun getPointData() {
        if (view == null) {
            return
        }
        mGetPointData!!.clearRequest()
        val graphqlRequestPoints = GraphqlRequest(GraphqlHelper.loadRawString(view!!.resources, R.raw.tp_gql_current_points),
                TokoPointDetailEntity::class.java, false)
        mGetPointData.addRequest(graphqlRequestPoints)
        mGetPointData.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {}
            override fun onNext(graphqlResponse: GraphqlResponse) { //Handling the point
                val pointDetailEntity = graphqlResponse.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
                if (pointDetailEntity == null || pointDetailEntity.tokoPoints == null || pointDetailEntity.tokoPoints.resultStatus == null || pointDetailEntity.tokoPoints.status == null || pointDetailEntity.tokoPoints.status.points == null) {
                    view!!.onErrorPoint(null)
                } else {
                    if (pointDetailEntity.tokoPoints.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                        view!!.onSuccessPoints(pointDetailEntity.tokoPoints.status.points.rewardStr,
                                pointDetailEntity.tokoPoints.status.points.reward,
                                pointDetailEntity.tokoPoints.status.tier.nameDesc,
                                pointDetailEntity.tokoPoints.status.tier.eggImageUrl)
                    }
                }
            }
        })
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

    fun getCategoryName(catalogCategories: List<CatalogSubCategory?>, selectedCategoryId: Int): String {
        for (each in catalogCategories) {
            if (each == null) {
                continue
            }
            if (selectedCategoryId == each.id) {
                return each.name
            }
        }
        return ""
    }

}