package com.tokopedia.digital.categorylist.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.SimpleDatabaseModel;
import com.tokopedia.core.network.entity.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.categorylist.data.cloud.DigitalCategoryListApi;
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

    private DigitalCategoryListApi digitalApi;
    private final GlobalCacheManager globalCacheManager;
    private final ICategoryDigitalListDataMapper digitalListDataMapper;
    private final SessionHandler sessionHandler;

    public DigitalCategoryListRepository(DigitalCategoryListApi digitalApi,
                                         GlobalCacheManager globalCacheManager,
                                         ICategoryDigitalListDataMapper digitalListDataMapper,
                                         SessionHandler sessionHandler) {
        this.digitalApi = digitalApi;
        this.globalCacheManager = globalCacheManager;
        this.digitalListDataMapper = digitalListDataMapper;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList(String deviceVersion) {
        return Observable.just(
                globalCacheManager.getValueString(TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST)
        ).flatMap(getFuncObservableDigitalCategoryListDataFromCache())
                .onErrorResumeNext(getResumeFunctionObservableDigitalCategoryListDataFromNetwork(deviceVersion));
    }

    @NonNull
    private Func1<Throwable, Observable<? extends List<DigitalCategoryItemData>>>
    getResumeFunctionObservableDigitalCategoryListDataFromNetwork(String deviceVersion) {
        return new Func1<Throwable, Observable<? extends List<DigitalCategoryItemData>>>() {
            @Override
            public Observable<? extends List<DigitalCategoryItemData>> call(Throwable throwable) {
                return getDigitalCategoryItemDataListFromNetwork(deviceVersion);
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
    private Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataListFromNetwork(String deviceVersion) {
        return digitalApi
                .getDigitalCategoryList(
                        sessionHandler.getLoginID(),
                        GlobalConfig.getPackageApplicationName(),
                        deviceVersion)
                .map(
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
                        globalCacheManager.setKey(TkpdCache.Key.DIGITAL_CATEGORY_ITEM_LIST);
                        globalCacheManager.setValue(stringResponse.body());
                        globalCacheManager.store();
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
