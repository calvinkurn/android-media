package com.tokopedia.core.catalog.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.catalog.interactor.CatalogDataInteractor;
import com.tokopedia.core.catalog.interactor.ICataloDataInteractor;
import com.tokopedia.core.catalog.listener.ICatalogDetailListView;
import com.tokopedia.core.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.core.catalog.model.CatalogDetailItemShop;
import com.tokopedia.core.catalog.model.CatalogDetailListData;
import com.tokopedia.core.catalog.model.CatalogListWrapperData;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;

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
        Intent intent = new Intent(view.getActivity(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(shop.getId(), shop.getDomain()));
        view.getActivity().startActivity(intent);
    }

    @Override
    public void goToProductDetailPage(CatalogDetailItemProduct product) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(view.getActivity(), ProductInfoActivity.class);
        bundle.putString("product_id", product.getId());
        intent.putExtras(bundle);
        view.getActivity().startActivity(intent);
    }
}
