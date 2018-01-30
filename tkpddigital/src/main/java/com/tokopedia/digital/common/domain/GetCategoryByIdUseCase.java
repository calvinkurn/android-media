package com.tokopedia.digital.common.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.data.repository.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class GetCategoryByIdUseCase extends UseCase<ProductDigitalData> {

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";
    private final String PARAM_PRODUCT_ID = "product_id";
    private final String PARAM_CLIENT_NUMBER = "client_number";
    private final String PARAM_SORT = "sort";

    private final String PARAM_IS_RESELLER = "is_reseller";
    private final String PARAM_VALUE_IS_RESELLER = "1";

    private Context context;
    private IDigitalCategoryRepository digitalCategoryRepository;

    public GetCategoryByIdUseCase(Context context, IDigitalCategoryRepository digitalCategoryRepository) {
        this.context = context;
        this.digitalCategoryRepository = digitalCategoryRepository;
    }

    @Override
    public Observable<ProductDigitalData> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");
        String productId = requestParams.getString(PARAM_PRODUCT_ID, "");
        String clientNumber = requestParams.getString(PARAM_CLIENT_NUMBER, "");
        String sort = requestParams.getString(PARAM_SORT, "");

        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, PARAM_VALUE_IS_RESELLER);
        }

        TKPDMapParam<String, String> paramQueryFavoriteList = new TKPDMapParam<>();
        paramQueryFavoriteList.put(PARAM_CATEGORY_ID, categoryId);
        if (!TextUtils.isEmpty(operatorId)) {
            paramQueryFavoriteList.put(PARAM_OPERATOR_ID, operatorId);
        }
        if (!TextUtils.isEmpty(productId)) {
            paramQueryFavoriteList.put(PARAM_PRODUCT_ID, productId);
        }
        if (!TextUtils.isEmpty(clientNumber)) {
            paramQueryFavoriteList.put(PARAM_CLIENT_NUMBER, clientNumber);
        }
        paramQueryFavoriteList.put(PARAM_SORT, sort);

        return Observable.zip(
                digitalCategoryRepository.getCategory(categoryId, getGeneratedAuthParamNetwork(paramQueryCategory)),
                getFavoriteList(getGeneratedAuthParamNetwork(paramQueryFavoriteList)),
                getZipFunctionProductDigitalData());
    }

    private Observable<DigitalNumberList> getFavoriteList
            (TKPDMapParam<String, String> paramQueryLastNumber) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            return digitalCategoryRepository.getFavoriteList(paramQueryLastNumber);
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

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

    public RequestParams createRequestParam(String categoryId, String sort) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_SORT, sort);
        return requestParams;
    }

    public RequestParams createRequestParam(String categoryId, String operatorId, String productId,
                                            String clientNumber, String sort) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_SORT, sort);
        requestParams.putString(PARAM_OPERATOR_ID, operatorId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_CLIENT_NUMBER, clientNumber);
        return requestParams;
    }

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

}
