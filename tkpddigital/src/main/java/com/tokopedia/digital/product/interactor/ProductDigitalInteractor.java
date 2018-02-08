package com.tokopedia.digital.product.interactor;

import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.product.model.PulsaBalance;
import com.tokopedia.digital.widget.domain.IDigitalWidgetRepository;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalInteractor implements IProductDigitalInteractor {
    private final CompositeSubscription compositeSubscription;
    private final IDigitalCategoryRepository categoryRepository;
    private final IUssdCheckBalanceRepository ussdCheckBalanceRepository;
    private IDigitalWidgetRepository digitalWidgetRepository;

    public ProductDigitalInteractor(CompositeSubscription compositeSubscription,
                                    IDigitalWidgetRepository digitalWidgetRepository,
                                    IDigitalCategoryRepository categoryRepository,
                                    LocalCacheHandler localCacheHandler,
                                    IUssdCheckBalanceRepository ussdCheckBalanceRepository) {
        this.compositeSubscription = compositeSubscription;
        this.digitalWidgetRepository = digitalWidgetRepository;
        this.categoryRepository = categoryRepository;
        this.ussdCheckBalanceRepository = ussdCheckBalanceRepository;
    }

    @Override
    public void getCategoryAndBanner(
            final String pathCategoryId,
            TKPDMapParam<String, String> paramQueryCategory,
            TKPDMapParam<String, String> paramQueryBanner,
            TKPDMapParam<String, String> paramQueryNumberList,
            TKPDMapParam<String, String> paramQueryLastOrder,
            Subscriber<ProductDigitalData> subscriber) {
        compositeSubscription.add(
                Observable.zip(
                        categoryRepository.getCategory(pathCategoryId, paramQueryCategory),
                        getObservableNumberList(paramQueryNumberList),
                        getZipFunctionProductDigitalData())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    private Observable<DigitalNumberList> getObservableNumberList
            (TKPDMapParam<String, String> paramQueryLastNumber) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            return digitalWidgetRepository.getObservableNumberList(paramQueryLastNumber);
        } else {
            List<OrderClientNumber> orderClientNumbers = new ArrayList<>();
            DigitalNumberList digitalNumberList = new DigitalNumberList(orderClientNumbers, null);
            return Observable.just(digitalNumberList);
        }
    }

    @NonNull
    private Func2<CategoryData, DigitalNumberList, ProductDigitalData> getZipFunctionProductDigitalData() {
        return new Func2<CategoryData, DigitalNumberList, ProductDigitalData>() {
            @Override
            public ProductDigitalData call(
                    CategoryData categoryData,
                    DigitalNumberList digitalNumberList
            ) {
                List<BannerData> bannerDataList = new ArrayList<>();
                bannerDataList.addAll(categoryData.getBannerDataListIncluded());

                List<BannerData> otherBannerDataList = new ArrayList<>();
                otherBannerDataList.addAll(categoryData.getOtherBannerDataListIncluded());

                OrderClientNumber lastOrder = null;
                if (digitalNumberList.getLastOrder() != null) {
                    lastOrder = digitalNumberList.getLastOrder();
                }
                List<OrderClientNumber> numberList = digitalNumberList.getOrderClientNumbers();
                return new ProductDigitalData.Builder()
                        .historyClientNumber(new HistoryClientNumber.Builder()
                                .lastOrderClientNumber(lastOrder)
                                .recentClientNumberList(numberList)
                                .build())
                        .categoryData(categoryData)
                        .bannerDataList(bannerDataList)
                        .otherBannerDataList(otherBannerDataList)
                        .build();
            }
        };
    }

    @Override
    public void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber) {
        ussdCheckBalanceRepository.processPulsaBalanceUssdResponse(requestBodyPulsaBalance)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

}
