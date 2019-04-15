package com.tokopedia.product.manage.list.view.presenter;

import android.accounts.NetworkErrorException;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.list.domain.DeleteProductUseCase;
import com.tokopedia.product.manage.list.domain.EditPriceProductUseCase;
import com.tokopedia.product.manage.list.domain.MultipleDeleteProductUseCase;
import com.tokopedia.product.manage.list.domain.model.MultipleDeleteProductModel;
import com.tokopedia.product.manage.list.view.listener.ProductManageView;
import com.tokopedia.product.manage.list.view.mapper.GetProductListManageMapperView;
import com.tokopedia.product.manage.list.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManagePresenterImpl extends BaseDaggerPresenter<ProductManageView> implements ProductManagePresenter {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetProductListSellingUseCase getProductListSellingUseCase;
    private final EditPriceProductUseCase editPriceProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductListManageMapperView getProductListManageMapperView;
    private final MultipleDeleteProductUseCase multipleDeleteProductUseCase;
    private final TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;
    private final TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositGraphQLUseCase;
    private final GetFeatureProductListUseCase getFeatureProductListUseCase;
    private SetCashbackUseCase setCashbackUseCase;
    private final UserSessionInterface userSession;

    public ProductManagePresenterImpl(GetShopInfoUseCase getShopInfoUseCase,
                                      GetProductListSellingUseCase getProductListSellingUseCase,
                                      EditPriceProductUseCase editPriceProductUseCase,
                                      DeleteProductUseCase deleteProductUseCase,
                                      GetProductListManageMapperView getProductListManageMapperView,
                                      MultipleDeleteProductUseCase multipleDeleteProductUseCase,
                                      UserSessionInterface userSession,
                                      TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                      TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositGraphQLUseCase,
                                      GetFeatureProductListUseCase getFeatureProductListUseCase,
                                      SetCashbackUseCase setCashbackUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getProductListSellingUseCase = getProductListSellingUseCase;
        this.editPriceProductUseCase = editPriceProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getProductListManageMapperView = getProductListManageMapperView;
        this.multipleDeleteProductUseCase = multipleDeleteProductUseCase;
        this.userSession = userSession;
        this.topAdsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
        this.topAdsGetShopDepositGraphQLUseCase = topAdsGetShopDepositGraphQLUseCase;
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
        this.setCashbackUseCase = setCashbackUseCase;
    }

    @Override
    public void getGoldMerchantStatus() {
        getShopInfoUseCase.execute(RequestParams.EMPTY, new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopModel shopModel) {
                getView().onSuccessGetShopInfo(shopModel.getInfo().isGoldMerchant(), shopModel.getInfo().isOfficialStore(), shopModel.getInfo().shopDomain);
            }
        });
    }

    @Override
    public void setCashback(final String productId, final int cashback) {
        getView().showLoadingProgress();
        setCashbackUseCase.execute(SetCashbackUseCase.createRequestParams(productId, cashback), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorSetCashback(e, productId, cashback);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().hideLoadingProgress();
                if (isSuccess) {
                    getView().onSuccessSetCashback(productId, cashback);
                } else {
                    getView().onErrorSetCashback(new NetworkErrorException(), productId, cashback);
                }
            }
        });
    }

    @Override
    public void deleteProduct(final List<String> productIdList) {
        getView().showLoadingProgress();
        multipleDeleteProductUseCase.execute(MultipleDeleteProductUseCase.createRequestParams(productIdList), new Subscriber<MultipleDeleteProductModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorMultipleDeleteProduct(e, new ArrayList<String>(), productIdList);
                }
            }

            @Override
            public void onNext(MultipleDeleteProductModel multipleDeleteProductModel) {
                getView().hideLoadingProgress();
                if (multipleDeleteProductModel.getProductIdFailedToDeleteList().size() > 0) {
                    getView().onErrorMultipleDeleteProduct(new NetworkErrorException(),
                            multipleDeleteProductModel.getProductIdDeletedList(),
                            multipleDeleteProductModel.getProductIdFailedToDeleteList());
                } else {
                    getView().onSuccessMultipleDeleteProduct();
                }
            }
        });
    }

    @Override
    public void saveSourceTagging(boolean isSellerApp) {
        String source = isSellerApp ? TopAdsSourceOption.SA_MANAGE_LIST_PRODUCT : TopAdsSourceOption.MA_MANAGE_LIST_PRODUCT;
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }

    @Override
    public void getListFeaturedProduct() {
        getFeatureProductListUseCase.execute(GetFeatureProductListUseCase.createRequestParam(userSession.getShopId()),
                getSubscriberGetListFeaturedProduct());
    }

    @Override
    public void editPrice(final String productId, final String price, final String currencyId, final String currencyText) {
        getView().showLoadingProgress();
        editPriceProductUseCase.execute(EditPriceProductUseCase.createRequestParams(price, currencyId, productId), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorEditPrice(t, productId, price, currencyId, currencyText);
                }
            }

            @Override
            public void onNext(Boolean isSuccessEditPrice) {
                getView().hideLoadingProgress();
                if (isSuccessEditPrice) {
                    getView().onSuccessEditPrice(productId, price, currencyId, currencyText);
                } else {
                    getView().onErrorEditPrice(new NetworkErrorException(), productId, price, currencyId, currencyText);
                }
            }
        });
    }

    @Override
    public void getListProduct(int page, String keywordFilter, @CatalogProductOption String catalogOption,
                               @ConditionProductOption String conditionOption, int etalaseId,
                               @PictureStatusProductOption String pictureOption, @SortProductOption String sortOption, String categoryId) {
        getProductListSellingUseCase.execute(GetProductListSellingUseCase.createRequestParamsManageProduct(page,
                keywordFilter, catalogOption, conditionOption, categoryId, etalaseId,
                pictureOption, sortOption), getSubscriberGetListProduct());
    }

    private Subscriber<ProductListSellerModel> getSubscriberGetListProduct() {
        return new Subscriber<ProductListSellerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onLoadSearchError(e);
                }
            }

            @Override
            public void onNext(ProductListSellerModel productListSellerModel) {
                ProductListManageModelView productListManageModelView = getProductListManageMapperView.transform(productListSellerModel);
                getView().onSearchLoaded(productListManageModelView.getProductManageViewModels(),
                        productListManageModelView.getProductManageViewModels().size(),
                        productListManageModelView.isHasNextPage());
            }
        };
    }

    private Subscriber<List<GMFeaturedProduct>> getSubscriberGetListFeaturedProduct() {
        return new Subscriber<List<GMFeaturedProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<GMFeaturedProduct> gmFeaturedProducts) {
                getView().onSuccessGetFeaturedProductList(transform(gmFeaturedProducts));
            }
        };
    }

    private List<String> transform(List<GMFeaturedProduct> data) {
        List<String> productIds = new ArrayList<>();
        for (GMFeaturedProduct datum : data) {
            productIds.add(String.valueOf(datum.getParentId()));
        }
        return productIds;
    }

    @Override
    public void getFreeClaim(String graphqlQuery, String shopId) {
        topAdsGetShopDepositGraphQLUseCase.execute(TopAdsGetShopDepositGraphQLUseCase.createRequestParams(graphqlQuery, shopId),
                new Subscriber<DataDeposit>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()){
                            getView().onErrorGetFreeClaim(throwable);
                        }
                    }

                    @Override
                    public void onNext(DataDeposit dataDeposit) {
                        if (isViewAttached()){
                            getView().onSuccessGetFreeClaim(dataDeposit);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        setCashbackUseCase.unsubscribe();
        getProductListSellingUseCase.unsubscribe();
        editPriceProductUseCase.unsubscribe();
        deleteProductUseCase.unsubscribe();
        multipleDeleteProductUseCase.unsubscribe();
        topAdsAddSourceTaggingUseCase.unsubscribe();
        topAdsGetShopDepositGraphQLUseCase.unsubscribe();
    }
}
