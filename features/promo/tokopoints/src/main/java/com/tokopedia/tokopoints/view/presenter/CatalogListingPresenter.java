package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.CatalogListingContract;
import com.tokopedia.tokopoints.view.model.CatalogBannerOuter;
import com.tokopedia.tokopoints.view.model.CatalogCategory;
import com.tokopedia.tokopoints.view.model.CatalogFilterOuter;
import com.tokopedia.tokopoints.view.model.TokenDetailOuter;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class CatalogListingPresenter extends BaseDaggerPresenter<CatalogListingContract.View>
        implements CatalogListingContract.Presenter {
    private GraphqlUseCase mGetHomePageData;
    private GraphqlUseCase mGetPointData;

    @Inject
    public CatalogListingPresenter(GraphqlUseCase getHomePageData, GraphqlUseCase getPointData) {
        this.mGetHomePageData = getHomePageData;
        this.mGetPointData = getPointData;
    }

    @Override
    public void destroyView() {
        if (mGetHomePageData != null) {
            mGetHomePageData.unsubscribe();
        }

        if (mGetPointData != null) {
            mGetPointData.unsubscribe();
        }
    }

    @Override
    public void getHomePageData() {
        if (getView() == null) {
            return;
        }

        mGetHomePageData.clearRequest();
        getView().showLoader();

        //Adding banner query
        Map<String, Object> variablesBanner = new HashMap<>();
        variablesBanner.put(CommonConstant.GraphqlVariableKeys.DEVICE, CommonConstant.DEVICE_ID_BANNER);
        GraphqlRequest graphqlRequestBanners = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_catalog_banners),
                CatalogBannerOuter.class,
                variablesBanner);
        mGetHomePageData.addRequest(graphqlRequestBanners);

        Map<String, Object> variableFilter = new HashMap<>();
        variableFilter.put(CommonConstant.GraphqlVariableKeys.SLUG, "");
        GraphqlRequest graphqlRequestFilter = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_catalog_filter),
                CatalogFilterOuter.class,
                variableFilter);
        mGetHomePageData.addRequest(graphqlRequestFilter);

        GraphqlRequest graphqlRequestEgg = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_lucky_egg_details),
                TokenDetailOuter.class);
        mGetHomePageData.addRequest(graphqlRequestEgg);

        GraphqlRequest graphqlRequestTokenDetail = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_detail),
                TokoPointDetailEntity.class);
        mGetHomePageData.addRequest(graphqlRequestTokenDetail);

        mGetHomePageData.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorFilter("null");
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //Handling the banner
                CatalogBannerOuter outer = graphqlResponse.getData(CatalogBannerOuter.class);
                if (outer == null || outer.getBannerData() == null || outer.getBannerData().getBanners() == null) {
                    getView().onErrorBanners(null);
                } else {
                    getView().onSuccessBanners(outer.getBannerData().getBanners());
                }

                //handling the catalog listing and tabs
                CatalogFilterOuter catalogFilterOuter = graphqlResponse.getData(CatalogFilterOuter.class);
                if (catalogFilterOuter == null || catalogFilterOuter.getFilter() == null) {
                    getView().onErrorFilter(null);
                } else {
                    getView().onSuccessFilter(catalogFilterOuter.getFilter());
                }

                //handling for lucky egg data
                TokenDetailOuter tokenDetail = graphqlResponse.getData(TokenDetailOuter.class);
                TokoPointDetailEntity data = graphqlResponse.getData(TokoPointDetailEntity.class);
                if (tokenDetail != null
                        && tokenDetail.getTokenDetail() != null
                        && tokenDetail.getTokenDetail().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS
                        && data != null
                        && data.getTokoPoints() != null) {
                    getView().onSuccessTokenDetail(tokenDetail.getTokenDetail(), data.getTokoPoints().getLobs());
                }
            }
        });
    }

    @Override
    public void getPointData() {
        if (getView() == null) {
            return;
        }

        mGetPointData.clearRequest();

        GraphqlRequest graphqlRequestPoints = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getResources(), R.raw.tp_gql_current_points),
                TokoPointDetailEntity.class);
        mGetPointData.addRequest(graphqlRequestPoints);

        mGetPointData.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                //Handling the point
                TokoPointDetailEntity pointDetailEntity = graphqlResponse.getData(TokoPointDetailEntity.class);
                if (pointDetailEntity == null
                        || pointDetailEntity.getTokoPoints() == null
                        || pointDetailEntity.getTokoPoints().getResultStatus() == null
                        || pointDetailEntity.getTokoPoints().getStatus() == null
                        || pointDetailEntity.getTokoPoints().getStatus().getPoints() == null) {
                    getView().onErrorPoint(null);
                } else {
                    if (pointDetailEntity.getTokoPoints().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                        getView().onSuccessPoints(pointDetailEntity.getTokoPoints().getStatus().getPoints().getRewardStr(), pointDetailEntity.getTokoPoints().getStatus().getPoints().getReward());
                    }
                }

            }
        });
    }

    @Override
    public int getSelectedCategoryId() {
        return getView().getSelectedCategoryId();
    }

    public String getCategoryName(ArrayList<CatalogCategory> catalogCategories, int selectedCategoryId) {
        for (CatalogCategory each : catalogCategories) {
            if (each == null) {
                continue;
            }

            if (selectedCategoryId == each.getId()) {
                return each.getName();
            }
        }

        return "";
    }
}
