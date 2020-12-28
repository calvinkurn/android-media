package com.tokopedia.home.explore.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.home.R;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.explore.domain.model.DataResponseModel;
import com.tokopedia.home.explore.domain.model.DynamicHomeIcon;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.domain.model.LayoutSections;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.view.adapter.datamodel.CategoryFavoriteDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.CategoryGridListDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.MyShopDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.SellDataModel;
import com.tokopedia.network.data.model.response.GraphqlResponse;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCaseRx;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.home.util.ErrorMessageUtils.getErrorMessage;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataSource {
    private Context context;
    private HomeDataApi homeDataApi;
    private CacheManager cacheManager;
    private UserSessionInterface userSessionInterface;
    private GetShopInfoUseCaseRx profileSource;
    private Gson gson;

    private static final String SHOP_ID = "SHOP_ID";
    private static final String USER_ID = "USER_ID";
    private static final String DEVICE_ID = "DEVICE_ID";

    public ExploreDataSource(Context context, HomeDataApi homeDataApi,
                             CacheManager cacheManager,
                             GetShopInfoUseCaseRx profileSource,
                             UserSessionInterface userSessionInterface,
                             Gson gson) {
        this.context = context;
        this.homeDataApi = homeDataApi;
        this.cacheManager = cacheManager;
        this.profileSource = profileSource;
        this.userSessionInterface = userSessionInterface;
        this.gson = gson;
    }

    public Observable<List<ExploreSectionDataModel>> getExploreData(String userId) {
        if (userId.isEmpty()) {
            return getData("");
        } else {
            RequestParams requestParams = new RequestParams();
            requestParams.putString(SHOP_ID, userSessionInterface.getShopId());
            requestParams.putString(USER_ID, userSessionInterface.getUserId());
            requestParams.putString(DEVICE_ID, userSessionInterface.getDeviceId());
            return profileSource.createObservable(requestParams)
                    .flatMap(shopInfo -> {
                        if (shopInfo != null) {
                            return getData(shopInfo.getShopCore().getDomain());
                        } else {
                            return getData("");
                        }
                    });
        }
    }

    @NonNull
    private Observable<List<ExploreSectionDataModel>> getData(String shopDomain) {
        return homeDataApi.getExploreData(String.format(getRequestPayload(), shopDomain))
                .doOnNext(saveToCache())
                .map(getMapper());
    }

    private Action1<Response<GraphqlResponse<DataResponseModel>>> saveToCache() {
        return new Action1<Response<GraphqlResponse<DataResponseModel>>>() {
            @Override
            public void call(Response<GraphqlResponse<DataResponseModel>> response) {
                if (response.isSuccessful()) {
                    DataResponseModel data = response.body().getData();
                    cacheManager.put(
                            ConstantKey.TkpdCache.EXPLORE_DATA_CACHE,
                            gson.toJson(data),
                            0
                    );
                }
            }
        };
    }

    @NonNull
    private Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionDataModel>> getMapper() {
        return new Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionDataModel>>() {
            @Override
            public List<ExploreSectionDataModel> call(Response<GraphqlResponse<DataResponseModel>> response) {
                if (response.isSuccessful()) {
                    UserSessionInterface userSession = new UserSession(context);
                    List<ExploreSectionDataModel> models = new ArrayList<>();

                    DynamicHomeIcon model = response.body().getData().getDynamicHomeIcon();
                    for (int i = 0; i < model.getLayoutSections().size(); i++) {
                        LayoutSections s = model.getLayoutSections().get(i);
                        ExploreSectionDataModel sectionViewModel = new ExploreSectionDataModel();
                        sectionViewModel.setUseCaseIcon(model.getUseCaseIcons().get(i));
                        sectionViewModel.setTitle(s.getTitle());
                        if (i == 0 && model.getFavCategory() != null) {
                            sectionViewModel.addVisitable(mappingFavoriteCategory(model.getFavCategory()));
                        }
                        if (i == 4) {
                            if (userSession.hasShop()) {
                                sectionViewModel.addVisitable(mappingManageShop(response.body().getData()
                                        .getShopInfo().getData()));
                            } else {
                                sectionViewModel.addVisitable(mappingOpenShop());
                            }
                        }
                        sectionViewModel.addVisitable(mappingCategory(s));
                        models.add(sectionViewModel);
                    }
                    return models;
                } else {
                    String messageError = getErrorMessage(response);
                    if (!TextUtils.isEmpty(messageError)) {
                        throw new RuntimeException(messageError);
                    } else {
                        throw new RuntimeException(String.valueOf(response.code()));
                    }
                }
            }

            private Visitable mappingOpenShop() {
                SellDataModel model = new SellDataModel();
                model.setTitle(context.getString(R.string.empty_shop_wording_title));
                model.setSubtitle(context.getString(R.string.empty_shop_wording_subtitle));
                model.setBtn_title(context.getString(R.string.buka_toko));
                return model;
            }

            private Visitable mappingManageShop(ShopData data) {
                MyShopDataModel model = new MyShopDataModel(data);
                return model;
            }

            private Visitable mappingFavoriteCategory(List<LayoutRows> favCategory) {
                CategoryFavoriteDataModel viewModel = new CategoryFavoriteDataModel();
                viewModel.setTitle(context.getString(R.string.kategori_favorit));
                viewModel.setItemList(favCategory);
                return viewModel;
            }

            private Visitable mappingCategory(LayoutSections s) {
                CategoryGridListDataModel viewModel = new CategoryGridListDataModel();
                viewModel.setSectionId(s.getId());
                viewModel.setTitle(s.getTitle());
                viewModel.setItemList(s.getLayoutRows());
                return viewModel;
            }
        };
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.explore_data_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    public Observable<List<ExploreSectionDataModel>> getDataCache() {
        return Observable.just(true).map(new Func1<Boolean, Response<GraphqlResponse<DataResponseModel>>>() {
            @Override
            public Response<GraphqlResponse<DataResponseModel>> call(Boolean aBoolean) {
                String cache = cacheManager.getString(ConstantKey.TkpdCache.EXPLORE_DATA_CACHE);
                if (cache != null) {
                    DataResponseModel data = gson.fromJson(cache, DataResponseModel.class);
                    String cachedShopDomain = data.getShopInfo().getData().getDomain();

                    SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
                    String shopDomainPreference = mSettings.getString("shopDomain","");
                    if (!shopDomainPreference.equals(cachedShopDomain)) {
                        throw new RuntimeException("Cached data shopInfo mismatch!!");
                    }
                    GraphqlResponse<DataResponseModel> graphqlResponse = new GraphqlResponse<>();
                    graphqlResponse.setData(data);
                    return Response.success(graphqlResponse);
                }
                throw new RuntimeException("Cache is empty!!");
            }
        }).map(getMapper());
    }
}
