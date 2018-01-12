package com.tokopedia.core.product.interactor;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.ProductDetailCacheManager;
import com.tokopedia.core.database.manager.ProductOtherCacheManager;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.var.TkpdCache;

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
    private static final String PROMO_WIDGET_PDP = "PROMO_WIDGET_PDP";

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
    public void getPromoWidgetCache(final @NonNull String targetType, final @NonNull String userId,
                                    final @NonNull String shopType, @NonNull final GetPromoWidgetCacheListener listener) {

        final GlobalCacheManager cacheManager= new GlobalCacheManager();
        Observable.just(PROMO_WIDGET_PDP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, PromoAttributes>() {
                    @Override
                    public PromoAttributes call(String s) {
                        return cacheManager.getConvertObjData(s, PromoAttributes.class);
                    }
                })
                .subscribe(new Subscriber<PromoAttributes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }

                    @Override
                    public void onNext(PromoAttributes promoAttributes) {
                        if (promoAttributes!=null && targetType.equals(promoAttributes.getTargetType()) &&
                                userId.equals(promoAttributes.getUserId()) && shopType.equals(promoAttributes.getShopType())) {
                            listener.onSuccess(promoAttributes);
                        } else {
                            listener.onError();
                        }
                    }
                });
        }

    @Override
    public void storePromoWidget(String targetType, String userId, String shopType, DataPromoWidget promoWidget) {

        PromoAttributes promoAttributes = new PromoAttributes();
        if (!promoWidget.getPromoWidgetList().isEmpty()) {
            promoAttributes = promoWidget.getPromoWidgetList().get(0).getPromoAttributes();
        }
        promoAttributes.setTargetType(targetType);
        promoAttributes.setUserId(userId);
        promoAttributes.setShopType(shopType);

        final GlobalCacheManager cache = new GlobalCacheManager();
        cache.setKey(PROMO_WIDGET_PDP)
                .setCacheDuration(promoWidget.getCacheExpire())
                .setValue(CacheUtil.convertModelToString(promoAttributes, new TypeToken<PromoAttributes>() {
                }.getType()));


        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GlobalCacheManager, Boolean>() {
                    @Override
                    public Boolean call(GlobalCacheManager globalCacheManager) {
                        cache.delete(PROMO_WIDGET_PDP);
                        globalCacheManager.store();
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
