package com.tokopedia.digital.product.interactor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.domain.ILastOrderNumberRepository;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalInteractor implements IProductDigitalInteractor {
    private final CompositeSubscription compositeSubscription;
    private final IDigitalCategoryRepository categoryRepository;
    private final ILastOrderNumberRepository lastOrderNumberRepository;
    private final LocalCacheHandler localCacheHandler;

    public ProductDigitalInteractor(CompositeSubscription compositeSubscription,
                                    IDigitalCategoryRepository categoryRepository,
                                    ILastOrderNumberRepository lastOrderNumberRepository,
                                    LocalCacheHandler localCacheHandler) {
        this.compositeSubscription = compositeSubscription;
        this.categoryRepository = categoryRepository;
        this.lastOrderNumberRepository = lastOrderNumberRepository;
        this.localCacheHandler = localCacheHandler;
    }

    @Override
    public void getCategoryAndBanner(
            final String pathCategoryId,
            TKPDMapParam<String, String> paramQueryCategory,
            TKPDMapParam<String, String> paramQueryBanner,
            TKPDMapParam<String, String> paramQueryLastNumber,
            TKPDMapParam<String, String> paramQueryLastOrder,
            Subscriber<ProductDigitalData> subscriber) {
        compositeSubscription.add(
                Observable.zip(
                        categoryRepository.getCategory(pathCategoryId, paramQueryCategory),
                        /*categoryRepository.getBanner(paramQueryBanner)
                                .onErrorReturn(new Func1<Throwable, List<BannerData>>() {
                                    @Override
                                    public List<BannerData> call(Throwable throwable) {
                                        return new ArrayList<>();
                                    }
                                }),*/
                        Observable.just(new ArrayList<BannerData>()),
                        getObservableRecentNumberOrderList(paramQueryLastNumber)
                                .flatMap(getFunctionFilterRecentNumberByCategory(pathCategoryId))
                                .onErrorReturn(getResumeFunctionOnErrorReturnRecentNumber()),
                        getObservableLastOrder(paramQueryLastOrder)
                                .map(getFunctionCheckCategoryIdMatcher(pathCategoryId))
                                .onErrorReturn(
                                        getResumeFunctionOnErrorReturnLastOrder(pathCategoryId)
                                ),
                        getZipFunctionProductDigitalData())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    private Observable<OrderClientNumber> getObservableLastOrder(
            TKPDMapParam<String, String> paramQueryLastOrder
    ) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            return lastOrderNumberRepository.getLastOrder(paramQueryLastOrder);
        } else {
            return Observable.just(new OrderClientNumber.Builder().build());
        }
    }

    private Observable<List<OrderClientNumber>> getObservableRecentNumberOrderList
            (TKPDMapParam<String, String> paramQueryLastNumber) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            return lastOrderNumberRepository.getRecentNumberOrderList(paramQueryLastNumber);
        } else {
            List<OrderClientNumber> emptyList = new ArrayList<>();
            return Observable.just(emptyList);
        }
    }

    @NonNull
    private Func1<Throwable, List<OrderClientNumber>> getResumeFunctionOnErrorReturnRecentNumber() {
        return new Func1<Throwable, List<OrderClientNumber>>() {
            @Override
            public List<OrderClientNumber> call(Throwable throwable) {
                throwable.printStackTrace();
                return new ArrayList<>();
            }
        };
    }

    @NonNull
    private Func1<Throwable, OrderClientNumber>
    getResumeFunctionOnErrorReturnLastOrder(final String pathCategoryId) {
        return new Func1<Throwable, OrderClientNumber>() {
            @Override
            public OrderClientNumber call(Throwable throwable) {
                throwable.printStackTrace();
                OrderClientNumber orderClientNumber = new OrderClientNumber();
                orderClientNumber.setCategoryId(pathCategoryId);

                String lastClientNumberOrder = localCacheHandler.getString(
                        TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + pathCategoryId, ""
                );
                String lastOperatorSelected = localCacheHandler.getString(
                        TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + pathCategoryId, ""
                );
                String lastProductSelected = localCacheHandler.getString(
                        TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + pathCategoryId, ""
                );
                orderClientNumber.setOperatorId(lastOperatorSelected);
                orderClientNumber.setProductId(lastProductSelected);
                if (!lastClientNumberOrder.isEmpty()) {
                    orderClientNumber.setClientNumber(lastClientNumberOrder);
                } else {
                    if (pathCategoryId.equalsIgnoreCase("1")
                            || pathCategoryId.equalsIgnoreCase("2")) {
                        orderClientNumber.setClientNumber(SessionHandler.getPhoneNumber());
                    } else {
                        orderClientNumber.setClientNumber("");
                    }
                }
                return orderClientNumber;
            }
        };
    }

    @NonNull
    private Func1<OrderClientNumber, OrderClientNumber> getFunctionCheckCategoryIdMatcher(
            final String pathCategoryId
    ) {
        return new Func1<OrderClientNumber, OrderClientNumber>() {
            @Override
            public OrderClientNumber call(OrderClientNumber orderClientNumber) {
                if (orderClientNumber != null
                        && !TextUtils.isEmpty(orderClientNumber.getCategoryId())
                        && orderClientNumber.getCategoryId().equalsIgnoreCase(pathCategoryId)) {
                    return orderClientNumber;
                } else {
                    throw new RuntimeException(
                            "last order not match with category id or null cause user are not login!!"
                    );
                }
            }
        };
    }

    @NonNull
    private Func1<List<OrderClientNumber>, Observable<List<OrderClientNumber>>>
    getFunctionFilterRecentNumberByCategory(final String pathCategoryId) {
        return new Func1<List<OrderClientNumber>, Observable<List<OrderClientNumber>>>() {
            @Override
            public Observable<List<OrderClientNumber>>
            call(List<OrderClientNumber> orderClientNumbers) {
                return Observable.from(orderClientNumbers)
                        .filter(new Func1<OrderClientNumber, Boolean>() {
                            @Override
                            public Boolean call(OrderClientNumber orderClientNumber) {
                                return (orderClientNumber.getCategoryId()
                                        .equalsIgnoreCase(pathCategoryId));
                            }
                        }).toList();
            }
        };
    }

    @NonNull
    private Func4<CategoryData, List<BannerData>, List<OrderClientNumber>,
            OrderClientNumber, ProductDigitalData> getZipFunctionProductDigitalData() {
        return new Func4<CategoryData, List<BannerData>, List<OrderClientNumber>,
                OrderClientNumber, ProductDigitalData>() {
            @Override
            public ProductDigitalData call(
                    CategoryData categoryData, List<BannerData> bannerDatas,
                    List<OrderClientNumber> orderClientNumbers, OrderClientNumber orderClientNumber
            ) {
                List<BannerData> bannerDataList = new ArrayList<>();
                for (BannerData bannerData : categoryData.getBannerDataListIncluded()) {
                    bannerDataList.add(bannerData);
                }
                return new ProductDigitalData.Builder()
                        .historyClientNumber(new HistoryClientNumber.Builder()
                                .lastOrderClientNumber(orderClientNumber)
                                .recentClientNumberList(orderClientNumbers)
                                .build())
                        .categoryData(categoryData)
                        .bannerDataList(bannerDataList)
                        .build();
            }
        };
    }
}
