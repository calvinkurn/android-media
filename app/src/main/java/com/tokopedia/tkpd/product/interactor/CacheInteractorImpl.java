package com.tokopedia.tkpd.product.interactor;


import android.util.Log;

import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.database.manager.ProductDetailCacheManager;
import com.tokopedia.tkpd.database.manager.ProductOtherCacheManager;
import com.tokopedia.tkpd.product.listener.ReportProductDialogView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.product.model.productother.ProductOther;
import com.tokopedia.tkpd.var.TkpdCache;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by ricoharisin on 12/3/15.
 */
public class CacheInteractorImpl implements CacheInteractor {
    private static String TAG = "CacheProductDetail";

    @Override
    public void getProductDetailCache(final String productID, final GetProductDetailCacheListener listener) {
        Observable.just(productID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, ProductDetailData>() {
                    @Override
                    public ProductDetailData call(String s) {
                        ProductDetailCacheManager cache = new ProductDetailCacheManager();
                        return cache.getConvertObjData(productID, ProductDetailData.class);
                    }
                })
                .subscribe(new Subscriber<ProductDetailData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e(TAG, e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ProductDetailData productDetailData) {
                        Log.i(TAG, "Get The Cache!! " + productDetailData.toString());
                        listener.onSuccess(productDetailData);
                    }
                });
    }

    @Override
    public void getOtherProductCache(String productId, final ProductOtherCacheListener listener) {
        Observable.just(productId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<ProductOther>>() {
                    @Override
                    public List<ProductOther> call(String s) {
                        ProductOtherCacheManager manager = new ProductOtherCacheManager();
                        return manager.getConvertObjDataList(s, ProductOther[].class);
                    }
                })
                .subscribe(new Subscriber<List<ProductOther>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(List<ProductOther> productOtherList) {
                        listener.onSuccess(productOtherList);
                    }
                });
    }

    @Override
    public void storeOtherProductCache(final String productId, final List<ProductOther> productOthers) {
        Observable.just(productId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        new ProductOtherCacheManager().setProductID(s)
                                .setData(productOthers)
                                .setCacheDuration(300)
                                .store();
                    }
                });


    }


    @Override
    public void loadReportTypeFromCache(final ReportProductDialogView viewListener) {
        final String TAG ="steven load cache reportType";
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        Log.i(TAG, "Start to getting the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();

                        Log.i(TAG, "End of getting the cache.....");

                        return cache.getValueString(TkpdCache.LIST_REPORT_TYPE);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        viewListener.downloadReportType();
                    }

                    @Override
                    public void onNext(String data) {
                        viewListener.setReportAdapterFromCache(data);
                    }
                });
    }

    @Override
    public void saveReportTypeToCache(String data) {
        final String TAG ="steven caching reportType";
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String data) {
                        Log.i(TAG, "Start to storing the cache.....");

                        GlobalCacheManager cache = new GlobalCacheManager();
                        cache.setCacheDuration(3600);
                        cache.setKey(TkpdCache.LIST_REPORT_TYPE);
                        cache.setValue(data);
                        cache.store();

                        Log.i(TAG, "End of storing the cache.....");

                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public void storeProductDetailCache(String productID, ProductDetailData data) {
        ProductDetailCacheManager cache = new ProductDetailCacheManager();
        cache.setProductID(productID);
        cache.setProductData(data);
        cache.setCacheDuration(300);
        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ProductDetailCacheManager, ProductDetailCacheManager>() {
                    @Override
                    public ProductDetailCacheManager call(ProductDetailCacheManager productDetailCacheManager) {
                        productDetailCacheManager.store();
                        return productDetailCacheManager;
                    }
                })
                .subscribe(new Subscriber<ProductDetailCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProductDetailCacheManager productDetailCacheManager) {

                    }
                });
    }

    @Override
    public void deleteProductDetail(Integer productId) {
        ProductDetailCacheManager cache = new ProductDetailCacheManager();
        cache.delete(productId + "");
    }

}
