package com.tokopedia.digital.product.interactor;

import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
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
                        categoryRepository.getBanner(paramQueryBanner)
                                .onErrorResumeNext(Observable.<List<BannerData>>empty()),
                        lastOrderNumberRepository.getRecentNumberOrderList(paramQueryLastNumber)
                                .flatMap(getFunctionFilterRecentNumberByCategory(pathCategoryId))
                                .onErrorResumeNext(Observable.<List<OrderClientNumber>>empty()),
                        lastOrderNumberRepository.getLastOrder(paramQueryLastOrder)
                                .onErrorReturn(getOnErrorResumeFunctionLastOrder(pathCategoryId)),
                        getZipFunctionProductDigitalData())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
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
    private Func1<Throwable, OrderClientNumber> getOnErrorResumeFunctionLastOrder(
            final String pathCategoryId
    ) {
        return new Func1<Throwable, OrderClientNumber>() {
            @Override
            public OrderClientNumber call(Throwable throwable) {
                String lastInputNumber = localCacheHandler.getString(
                        TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER
                                + pathCategoryId
                );
                if (lastInputNumber.isEmpty() && (
                        pathCategoryId.equalsIgnoreCase("1")
                                || pathCategoryId.equalsIgnoreCase("2")
                )) {
                    lastInputNumber = SessionHandler.getPhoneNumber();
                }
                OrderClientNumber orderClientNumber = new OrderClientNumber();
                orderClientNumber.setCategoryId(pathCategoryId);
                orderClientNumber.setClientNumber(lastInputNumber);
                orderClientNumber.setOperatorId("");
                orderClientNumber.setProductId("");
                return orderClientNumber;
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
                return new ProductDigitalData.Builder()
                        .historyClientNumber(new HistoryClientNumber.Builder()
                                .lastOrderClientNumber(orderClientNumber)
                                .recentClientNumberList(orderClientNumbers)
                                .build())
                        .categoryData(categoryData)
                        .bannerDataList(bannerDatas)
                        .build();
            }
        };
    }
}
