package com.tokopedia.digital.product.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ProductDigitalData;

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

    public ProductDigitalInteractor(CompositeSubscription compositeSubscription,
                                    IDigitalCategoryRepository categoryRepository) {
        this.compositeSubscription = compositeSubscription;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void getCategoryAndBanner(
            final String pathCategoryId,
            TKPDMapParam<String, String> paramQueryCategory,
            final TKPDMapParam<String, String> paramQueryBanner,
            Subscriber<ProductDigitalData> subscriber) {
        compositeSubscription.add(
                Observable.zip(
                        categoryRepository.getCategory(pathCategoryId, paramQueryCategory),
                        categoryRepository.getBanner(paramQueryBanner)
                                .onErrorResumeNext(Observable.just(new ArrayList<BannerData>())),
                        new Func2<CategoryData, List<BannerData>, ProductDigitalData>() {
                            @Override
                            public ProductDigitalData call(CategoryData categoryData, List<BannerData> bannerDatas) {
                                return new ProductDigitalData.Builder()
                                        .categoryData(categoryData)
                                        .bannerDataList(bannerDatas)
                                        .build();
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
