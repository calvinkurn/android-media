package com.tokopedia.digital.categorylist.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.categorylist.data.cloud.DigitalCategoryListApi;
import com.tokopedia.digital.categorylist.data.cloud.entity.HomeCategoryMenuItem;
import com.tokopedia.digital.categorylist.data.mapper.ICategoryDigitalListDataMapper;
import com.tokopedia.digital.categorylist.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.user.session.UserSession;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListRepository implements IDigitalCategoryListRepository {
    public static final String DIGITAL_CATEGORY_ITEM_LIST = "DIGITAL_CATEGORY_ITEM_LIST";

    private DigitalCategoryListApi digitalApi;
    private final CacheManager globalCacheManager;
    private final ICategoryDigitalListDataMapper digitalListDataMapper;
    private final UserSession sessionHandler;

    public DigitalCategoryListRepository(DigitalCategoryListApi digitalApi,
                                         CacheManager globalCacheManager,
                                         ICategoryDigitalListDataMapper digitalListDataMapper,
                                         UserSession sessionHandler) {
        this.digitalApi = digitalApi;
        this.globalCacheManager = globalCacheManager;
        this.digitalListDataMapper = digitalListDataMapper;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList(String deviceVersion) {
        return Observable.just(
                globalCacheManager.get(DIGITAL_CATEGORY_ITEM_LIST)
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
                        .fromJson(globalCacheManager.get(
                                DIGITAL_CATEGORY_ITEM_LIST),
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
                        sessionHandler.getUserId(),
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
                        globalCacheManager.save(DIGITAL_CATEGORY_ITEM_LIST, stringResponse.body(), 0);
                    }
                    return digitalListDataMapper.transformDigitalCategoryItemDataList(
                            homeCategoryMenuItem
                    );
                }
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        };
    }
}
