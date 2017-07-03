package com.tokopedia.digital.widget.domain;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.SimpleDatabaseModel;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;
import com.tokopedia.digital.widget.data.mapper.ICategoryDigitalListDataMapper;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListRepository implements IDigitalCategoryListRepository {

    private final GlobalCacheManager globalCacheManager;
    private final ICategoryDigitalListDataMapper digitalListDataMapper;
    private final MojitoService mojitoService;

    public DigitalCategoryListRepository(MojitoService mojitoService,
                                         GlobalCacheManager globalCacheManager,
                                         ICategoryDigitalListDataMapper digitalListDataMapper) {
        this.globalCacheManager = globalCacheManager;
        this.digitalListDataMapper = digitalListDataMapper;
        this.mojitoService = mojitoService;
    }

    @Override
    public Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList() {
        return Observable.just(globalCacheManager.getValueString(
                TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST))

                .flatMap(new Func1<String, Observable<List<DigitalCategoryItemData>>>() {
                    @Override
                    public Observable<List<DigitalCategoryItemData>> call(String s) {
                        HomeCategoryMenuItem homeCategoryMenuItem = new Gson()
                                .fromJson(globalCacheManager.getValueString(
                                        TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST),
                                        HomeCategoryMenuItem.class
                                );

                        List<DigitalCategoryItemData> digitalCategoryItemDataList =
                                digitalListDataMapper.transformDigitalCategoryItemDataList(
                                        homeCategoryMenuItem
                                );
                        if (digitalCategoryItemDataList == null || digitalCategoryItemDataList.isEmpty()) {
                            throw new RuntimeException("Data null or empty!!");
                        }
                        return Observable.just(digitalCategoryItemDataList);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<DigitalCategoryItemData>>>() {
                    @Override
                    public Observable<? extends List<DigitalCategoryItemData>> call(Throwable throwable) {
                        return getDigitalCategoryItemDataListFromNetwork();
                    }
                });
    }

    private Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataListFromNetwork() {
        return mojitoService.getApi().getHomeCategoryMenu().map(
                new Func1<Response<String>, List<DigitalCategoryItemData>>() {
                    @Override
                    public List<DigitalCategoryItemData> call(Response<String> stringResponse) {
                        if (stringResponse.isSuccessful() && stringResponse.body() != null) {
                            HomeCategoryMenuItem homeCategoryMenuItem = new Gson()
                                    .fromJson(stringResponse.body(),
                                            HomeCategoryMenuItem.class
                                    );
                            if (homeCategoryMenuItem != null && homeCategoryMenuItem.getData() != null
                                    && !homeCategoryMenuItem.getData().getLayoutSections().isEmpty()) {
                                globalCacheManager.store(new SimpleDatabaseModel.Builder()
                                        .key(TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST)
                                        .value(stringResponse.body())
                                        .build());
                            }
                            return digitalListDataMapper.transformDigitalCategoryItemDataList(
                                    homeCategoryMenuItem
                            );
                        } else {
                            throw new RuntimeHttpErrorException(stringResponse.code());
                        }
                    }
                }
        );
    }
}
