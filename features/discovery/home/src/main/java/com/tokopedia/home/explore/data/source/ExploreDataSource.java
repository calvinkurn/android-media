package com.tokopedia.home.explore.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.source.CloudProfileSource;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.explore.domain.model.DataResponseModel;
import com.tokopedia.home.explore.domain.model.DynamicHomeIcon;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.domain.model.LayoutSections;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryFavoriteViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataSource {
    private Context context;
    private HomeDataApi homeDataApi;
    private CacheManager cacheManager;
    private CloudProfileSource profileSource;
    private Gson gson;

    private static final String ERROR_MESSAGE = "message_error";

    public ExploreDataSource(Context context, HomeDataApi homeDataApi,
                             CacheManager cacheManager,
                             CloudProfileSource profileSource,
                             Gson gson) {
        this.context = context;
        this.homeDataApi = homeDataApi;
        this.cacheManager = cacheManager;
        this.profileSource = profileSource;
        this.gson = gson;
    }

    public Observable<List<ExploreSectionViewModel>> getExploreData(String userId) {
        if (userId.isEmpty()) {
            return getData("");
        } else {
            return profileSource.getProfile(RequestParams.EMPTY.getParameters())
                    .flatMap(new Func1<ProfileModel, Observable<List<ExploreSectionViewModel>>>() {
                        @Override
                        public Observable<List<ExploreSectionViewModel>> call(ProfileModel profileModel) {
                            if (profileModel.getProfileData().getShopInfo() != null) {
                                return getData(profileModel.getProfileData().getShopInfo().getShopDomain());
                            } else {
                                return getData("");
                            }
                        }
                    });
        }
    }

    @NonNull
    private Observable<List<ExploreSectionViewModel>> getData(String shopDomain) {
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
                    cacheManager.save(
                            ConstantKey.TkpdCache.EXPLORE_DATA_CACHE,
                            gson.toJson(data),
                            0
                    );
                }
            }
        };
    }

    @NonNull
    private Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionViewModel>> getMapper() {
        return new Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionViewModel>>() {
            @Override
            public List<ExploreSectionViewModel> call(Response<GraphqlResponse<DataResponseModel>> response) {
                if (response.isSuccessful()) {
                    List<ExploreSectionViewModel> models = new ArrayList<>();

                    DynamicHomeIcon model = response.body().getData().getDynamicHomeIcon();
                    for (int i = 0; i < model.getLayoutSections().size(); i++) {
                        LayoutSections s = model.getLayoutSections().get(i);
                        ExploreSectionViewModel sectionViewModel = new ExploreSectionViewModel();
                        sectionViewModel.setUseCaseIcon(model.getUseCaseIcons().get(i));
                        sectionViewModel.setTitle(s.getTitle());
                        if (i == 0 && model.getFavCategory() != null) {
                            sectionViewModel.addVisitable(mappingFavoriteCategory(model.getFavCategory()));
                        }
                        if (i == 4) {
                            if (SessionHandler.isUserHasShop(context)) {
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
                SellViewModel model = new SellViewModel();
                model.setTitle(context.getString(R.string.empty_shop_wording_title));
                model.setSubtitle(context.getString(R.string.empty_shop_wording_subtitle));
                model.setBtn_title(context.getString(R.string.buka_toko));
                return model;
            }

            private Visitable mappingManageShop(ShopData data) {
                MyShopViewModel model = new MyShopViewModel(data);
                return model;
            }

            private Visitable mappingFavoriteCategory(List<LayoutRows> favCategory) {
                CategoryFavoriteViewModel viewModel = new CategoryFavoriteViewModel();
                viewModel.setTitle(context.getString(R.string.kategori_favorit));
                viewModel.setItemList(favCategory);
                return viewModel;
            }

            private Visitable mappingCategory(LayoutSections s) {
                CategoryGridListViewModel viewModel = new CategoryGridListViewModel();
                viewModel.setSectionId(s.getId());
                viewModel.setTitle(s.getTitle());
                viewModel.setItemList(s.getLayoutRows());
                return viewModel;
            }
        };
    }

    public static String getErrorMessage(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());

            if (hasErrorMessage(jsonObject)) {
                JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                return getErrorMessageJoined(jsonArray);
            } else {
                return "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static boolean hasErrorMessage(JSONObject jsonObject) {
        return jsonObject.has(ERROR_MESSAGE);
    }

    public static String getErrorMessageJoined(JSONArray errorMessages) {
        try {

            StringBuilder stringBuilder = new StringBuilder();
            if (errorMessages.length() != 0) {
                for (int i = 0, statusMessagesSize = errorMessages.length(); i < statusMessagesSize; i++) {
                    String string = null;
                    string = errorMessages.getString(i);
                    stringBuilder.append(string);
                    if (i != errorMessages.length() - 1
                            && !errorMessages.get(i).equals("")
                            && !errorMessages.get(i + 1).equals("")) {
                        stringBuilder.append("\n");
                    }
                }
            }
            return stringBuilder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDefaultErrorCodeMessage(int errorCode) {
        return context.getString(R.string.default_request_error_unknown)
                + " (" + errorCode + ")";
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

    public Observable<List<ExploreSectionViewModel>> getDataCache() {
        return Observable.just(true).map(new Func1<Boolean, Response<GraphqlResponse<DataResponseModel>>>() {
            @Override
            public Response<GraphqlResponse<DataResponseModel>> call(Boolean aBoolean) {
                String cache = cacheManager.get(ConstantKey.TkpdCache.EXPLORE_DATA_CACHE);
                if (cache != null) {
                    DataResponseModel data = gson.fromJson(cache, DataResponseModel.class);
                    String cachedShopDomain = data.getShopInfo().getData().getDomain();
                    if (!SessionHandler.getShopDomain(context).equals(cachedShopDomain)) {
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
