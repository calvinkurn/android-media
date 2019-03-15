package com.tokopedia.discovery.catalog.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.catalog.interactor.CatalogDataInteractor;
import com.tokopedia.discovery.catalog.interactor.ICataloDataInteractor;
import com.tokopedia.discovery.catalog.listener.ICatalogDetailListView;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemShop;
import com.tokopedia.discovery.catalog.model.CatalogDetailListData;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author by alvarisi on 10/17/16.
 */

public class CatalogDetailListPresenter implements ICatalogDetailListPresenter {
    private final ICatalogDetailListView view;
    private final ICataloDataInteractor interactor;

    public CatalogDetailListPresenter(ICatalogDetailListView view) {
        this.view = view;
        this.interactor = new CatalogDataInteractor();
    }

    @Override
    public void fetchCatalogDetailListData(@NonNull CatalogListWrapperData catalogListWrapperData) {
        this.interactor.getDetailCatalogListData(catalogListWrapperData,
                new Subscriber<CatalogDetailListData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            view.renderErrorGetCatalogProduct(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            view.renderErrorGetCatalogProduct(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else {
                            view.renderErrorGetCatalogProduct(
                                    ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                            );
                        }
                    }

                    @Override
                    public void onNext(CatalogDetailListData catalogDetailListData) {
                        view.renderListCatalogProduct(catalogDetailListData.getCatalogDetailItems());
                        view.renderListLocation(catalogDetailListData.getCatalogDetailListLocations());
                    }
                });
    }

    @Override
    public void fetchCatalogDetailListDataLoadMore(@NonNull CatalogListWrapperData catalogListWrapperData) {
        this.interactor.getDetailCatalogListData(catalogListWrapperData,
                new Subscriber<CatalogDetailListData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            view.renderErrorGetCatalogProductLoadMore(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            view.renderErrorGetCatalogProductLoadMore(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else {
                            view.renderErrorGetCatalogProductLoadMore(
                                    ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                            );
                        }
                    }

                    @Override
                    public void onNext(CatalogDetailListData catalogDetailListData) {
                        view.renderListCatalogProductLoadMore(catalogDetailListData.getCatalogDetailItems());
                    }
                });
    }

    @Override
    public void goToShopPage(CatalogDetailItemShop shop) {
        Intent intent = ((DiscoveryRouter) view.getActivity().getApplication()).getShopPageIntent(view.getActivity(), shop.getId());
        view.getActivity().startActivity(intent);
    }

    @Override
    public void goToProductDetailPage(CatalogDetailItemProduct product) {
        view.getActivity().startActivity(getProductIntent(product.getId())
        );
    }

    private Intent getProductIntent(String productId){
        if (view.getActivity() != null) {
            return RouteManager.getIntent(view.getActivity(),ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    private ProductPass getProductDataToPass(CatalogDetailItemProduct product) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(product.getPrice())
                .setProductId(product.getId())
                .setProductName(product.getName())
                .setProductImage(product.getImageUri())
                .build();
    }

}
