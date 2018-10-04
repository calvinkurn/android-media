package com.tokopedia.gm.subscribe.data.source.product;


import com.tokopedia.gm.subscribe.domain.product.model.GmAutoSubscribeDomainModel;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModelGroup;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductSelectorDataSource {

    private final GmSubscribeProductDataSource gmSubscribeProductDataSource;

    public GmSubscribeProductSelectorDataSource(GmSubscribeProductDataSource gmSubscribeProductDataSource) {
        this.gmSubscribeProductDataSource = gmSubscribeProductDataSource;
    }

    public Observable<List<GmProductDomainModel>> getCurrentProductSelection() {
        return gmSubscribeProductDataSource.getData().map(new GetCurrentProductSelection());
    }

    public Observable<List<GmProductDomainModel>> getExtendProductSelection() {
        return gmSubscribeProductDataSource.getData().map(new GetExtendProductSelection());
    }

    public Observable<GmProductDomainModel> getCurrentProductSelectedData(Integer productId) {
        return getCurrentProductSelection().map(new SelectedProductFinder(productId));
    }

    public Observable<GmAutoSubscribeDomainModel> getExtendProductSelectedData(Integer autoSubscribeProductId, Integer productId) {
        return Observable.zip(
                getExtendProductSelection().map(new SelectedProductFinder(autoSubscribeProductId)),
                getCurrentProductSelectedData(productId),
                getPaymentMethod(),
                new CombineExtendedProductSelected());
    }

    public Observable<String> getPaymentMethod() {
        return gmSubscribeProductDataSource.getData().map(new GetPaymentMethod());
    }

    public Observable<Boolean> clearGMSubscribeProductCache() {
        return gmSubscribeProductDataSource.clearData();
    }


    private class GetCurrentProductSelection implements Func1<GmProductDomainModelGroup, List<GmProductDomainModel>> {
        @Override
        public List<GmProductDomainModel> call(GmProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getCurrentProduct();
        }
    }

    private class GetExtendProductSelection implements Func1<GmProductDomainModelGroup, List<GmProductDomainModel>> {
        @Override
        public List<GmProductDomainModel> call(GmProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getExtendProduct();
        }
    }

    private class SelectedProductFinder implements Func1<List<GmProductDomainModel>, GmProductDomainModel> {
        private final Integer productId;

        public SelectedProductFinder(Integer productId) {
            this.productId = productId;
        }

        @Override
        public GmProductDomainModel call(List<GmProductDomainModel> gmProductDomainModels) {
            for (GmProductDomainModel domainModel : gmProductDomainModels) {
                if (domainModel.getProductId() == productId) {
                    return domainModel;
                }
            }
            return null;
        }
    }

    private class GetPaymentMethod implements Func1<GmProductDomainModelGroup, String> {
        @Override
        public String call(GmProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getPaymentMethod();
        }
    }

    private class CombineExtendedProductSelected implements Func3<GmProductDomainModel, GmProductDomainModel, String, GmAutoSubscribeDomainModel> {
        @Override
        public GmAutoSubscribeDomainModel call(GmProductDomainModel autoSubscribeDomainModel, GmProductDomainModel currentProductDomainModel, String paymentMethod) {
            GmAutoSubscribeDomainModel resultDomainModel = new GmAutoSubscribeDomainModel();
            resultDomainModel.setTitle(autoSubscribeDomainModel.getName());
            resultDomainModel.setPrice(autoSubscribeDomainModel.getPrice());
            resultDomainModel.setNextAutoSubscribe(currentProductDomainModel.getNextInv());
            resultDomainModel.setPaymentMethod(paymentMethod);
            return resultDomainModel;
        }
    }
}
