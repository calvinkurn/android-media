package com.tokopedia.digital.categorylist.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.SimpleDatabaseModel;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.categorylist.data.mapper.ICategoryDigitalListDataMapper;
import com.tokopedia.digital.categorylist.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

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
    private final SessionHandler sessionHandler;

    public DigitalCategoryListRepository(MojitoService mojitoService,
                                         GlobalCacheManager globalCacheManager,
                                         ICategoryDigitalListDataMapper digitalListDataMapper,
                                         SessionHandler sessionHandler) {
        this.globalCacheManager = globalCacheManager;
        this.digitalListDataMapper = digitalListDataMapper;
        this.mojitoService = mojitoService;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList() {
        return Observable.just(
                globalCacheManager.getValueString(TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST)
        ).flatMap(getFuncObservableDigitalCategoryListDataFromCache())
                .onErrorResumeNext(getResumeFunctionObservableDigitalCategoryListDataFromNetwork());
    }

    @NonNull
    private Func1<Throwable, Observable<? extends List<DigitalCategoryItemData>>>
    getResumeFunctionObservableDigitalCategoryListDataFromNetwork() {
        return new Func1<Throwable, Observable<? extends List<DigitalCategoryItemData>>>() {
            @Override
            public Observable<? extends List<DigitalCategoryItemData>> call(Throwable throwable) {
                return getDigitalCategoryItemDataListFromNetwork();
            }
        };
    }

    @NonNull
    private Func1<String, Observable<List<DigitalCategoryItemData>>>
    getFuncObservableDigitalCategoryListDataFromCache() {
        return new Func1<String, Observable<List<DigitalCategoryItemData>>>() {
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
        };
    }

    @NonNull
    private Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataListFromNetwork() {
        return mojitoService.getApi().getHomeCategoryMenu(sessionHandler.getLoginID(),
                GlobalConfig.getPackageApplicationName()
        ).map(
                getFuncTransformResponseHomeCategoryToDigitalCategoryItemDataList()
        );
    }

    @NonNull
    private Func1<Response<String>, List<DigitalCategoryItemData>>
    getFuncTransformResponseHomeCategoryToDigitalCategoryItemDataList() {
        return new Func1<Response<String>, List<DigitalCategoryItemData>>() {
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
        };
    }
}
