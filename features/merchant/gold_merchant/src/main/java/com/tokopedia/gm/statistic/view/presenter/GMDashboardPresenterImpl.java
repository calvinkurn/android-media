package com.tokopedia.gm.statistic.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.statistic.constant.GMStatConstant;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.domain.KeywordModel;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatMarketInsightUseCase;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * Created on 1/2/17.
 *
 * @author normansyahputa
 */

public class GMDashboardPresenterImpl extends GMDashboardPresenter {

    private GMStatMarketInsightUseCase marketInsightUseCase;
    private GMStatGetBuyerGraphUseCase buyerGraphUseCase;
    private GMStatGetPopularProductUseCase popularProductUseCase;
    private GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase;
    private GMStatGetProductGraphUseCase productGraphUseCase;
    private AddProductShopInfoUseCase shopInfoUseCase;

    private boolean isGoldMerchant;
    private boolean hasSuccessGetGoldMerchant;

    public GMDashboardPresenterImpl(
            GMStatMarketInsightUseCase marketInsightUseCase ,
            GMStatGetBuyerGraphUseCase buyerGraphUseCase,
            GMStatGetPopularProductUseCase popularProductUseCase,
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            GMStatGetProductGraphUseCase productGraphUseCase,
            AddProductShopInfoUseCase shopInfoUseCase) {
        this.marketInsightUseCase = marketInsightUseCase;
        this.buyerGraphUseCase = buyerGraphUseCase;
        this.popularProductUseCase = popularProductUseCase;
        this.gmStatGetTransactionGraphUseCase = gmStatGetTransactionGraphUseCase;
        this.productGraphUseCase = productGraphUseCase;
        this.shopInfoUseCase = shopInfoUseCase;
    }

    private void fetchContentData(long starDate, long endDate) {
        getProductGraph(starDate, endDate);

        RequestParams transactionParam = GMStatGetTransactionGraphUseCase.createRequestParam(starDate, endDate, "");
        gmStatGetTransactionGraphUseCase.execute(transactionParam, new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadTransactionGraph(e);
                }
            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                getView().onSuccessLoadTransactionGraph(mergeModel, isGoldMerchant);
            }
        });

        RequestParams buyerParam = GMStatGetBuyerGraphUseCase.createRequestParam(starDate, endDate);
        buyerGraphUseCase.execute(buyerParam, new Subscriber<GetBuyerGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadBuyerGraph(e);
                }
            }

            @Override
            public void onNext(GetBuyerGraph getBuyerGraph) {
                getView().onSuccessLoadBuyerGraph(getBuyerGraph);
            }
        });

        if (isGoldMerchant) {
            marketInsightUseCase.execute(RequestParams.EMPTY, new Subscriber<KeywordModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (isViewAttached()) {
                        getView().onErrorLoadMarketInsight(e);
                    }
                }

                @Override
                public void onNext(KeywordModel keywordModel) {
                    GetShopCategory getShopCategory = keywordModel.getShopCategory();
                    if (getShopCategory == null
                            || getShopCategory.getCategoryIdList() == null
                            || getShopCategory.getCategoryIdList().isEmpty()) {
                        getView().onGetShopCategoryEmpty(isGoldMerchant);
                    } else {
                        getView().onSuccessGetKeyword(keywordModel.getKeywords(), isGoldMerchant);
                    }
                    onSuccessGetCategory(keywordModel.getCategoryName());
                }
            });
        } else {
            getView().onGetShopCategoryEmpty(isGoldMerchant);
        }

        getPopularProduct();
    }

    @Override
    public void fetchData(final long startDate, final long endDate) {
        if (hasSuccessGetGoldMerchant) {
            fetchContentData(startDate, endDate);
        } else {
            shopInfoUseCase.execute(null, new Subscriber<AddProductShopInfoDomainModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().onErrorLoadBuyerGraph(e);
                    getView().onErrorLoadMarketInsight(e);
                    getView().onErrorLoadPopularProduct(e);
                    getView().onErrorLoadTransactionGraph(e);
                    getView().onErrorLoadPopularProduct(e);
                    getView().onErrorLoadShopInfo(e);
                }

                @Override
                public void onNext(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                    hasSuccessGetGoldMerchant = true;
                    isGoldMerchant = addProductShopInfoDomainModel.isGoldMerchant();
                    getView().onSuccessLoadShopInfo(isGoldMerchant);
                    fetchContentData(startDate, endDate);
                }
            });
        }
    }

    private void getProductGraph(long startDate, long endDate) {
        RequestParams productParam = GMStatGetProductGraphUseCase.createRequestParam(startDate, endDate);
        productGraphUseCase.execute(productParam, new Subscriber<GetProductGraph>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadProductGraph(e);
                }
            }

            @Override
            public void onNext(GetProductGraph getProductGraph) {
                getView().onSuccessLoadProductGraph(getProductGraph);
            }
        });
    }

    private void getPopularProduct() {
        Calendar dayOne = Calendar.getInstance();
        dayOne.add(Calendar.DATE, -GMStatConstant.POPULAR_PRODUCT_DATE_RANGE);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, GMStatConstant.MAX_DAY_FROM_CURRENT_DATE);
        RequestParams popularParam = GMStatGetPopularProductUseCase.createRequestParam(dayOne.getTimeInMillis(), yesterday.getTimeInMillis());
        popularProductUseCase.execute(popularParam, new Subscriber<GetPopularProduct>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadPopularProduct(e);
                }
            }

            @Override
            public void onNext(GetPopularProduct getPopularProduct) {
                getView().onSuccessLoadPopularProduct(getPopularProduct);
            }
        });
    }

    private void onSuccessGetCategory(List<String> categoryNameList) {
        if (categoryNameList == null || categoryNameList.size() <= 0) {
            return;
        }
        String categoryName = categoryNameList.get(0);
        getView().onSuccessGetCategory(categoryName);
    }

    @Override
    public void detachView() {
        super.detachView();
        buyerGraphUseCase.unsubscribe();
        gmStatGetTransactionGraphUseCase.unsubscribe();
        productGraphUseCase.unsubscribe();
        popularProductUseCase.unsubscribe();
        marketInsightUseCase.unsubscribe();
        shopInfoUseCase.unsubscribe();
    }
}