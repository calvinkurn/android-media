package com.tokopedia.digital.product.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.widget.domain.IDigitalWidgetRepository;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Rizky on 18/01/18.
 */

public class DigitalCategoryUseCase extends UseCase<ProductDigitalData> {

    private final String PARAM_IS_RESELLER = "is_reseller";
    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";
    private final String PARAM_CLIENT_NUMBER = "client_number";
    private final String PARAM_PRODUCT_ID = "product_id";
    private final String PARAM_SORT = "sort";

    private Context context;
    private IDigitalCategoryRepository digitalCategoryRepository;
    private IDigitalWidgetRepository digitalWidgetRepository;

    public DigitalCategoryUseCase(Context context,
                                  IDigitalCategoryRepository digitalCategoryRepository,
                                  IDigitalWidgetRepository digitalWidgetRepository) {
        this.context = context;
        this.digitalCategoryRepository = digitalCategoryRepository;
        this.digitalWidgetRepository = digitalWidgetRepository;
    }

    @Override
    public Observable<ProductDigitalData> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");
        String clientNumber = requestParams.getString(PARAM_CLIENT_NUMBER, "");
        String productId = requestParams.getString(PARAM_PRODUCT_ID, "");
        String sort = requestParams.getString(PARAM_SORT, "");
        String isReseller = requestParams.getString(PARAM_IS_RESELLER, "");

        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, isReseller);
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
                getObservableNumberList(getGeneratedAuthParamNetwork(paramQueryFavoriteList)),
                getZipFunctionProductDigitalData());
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

    public RequestParams createRequestParam(String categoryId, String operatorId, String clientNumber,
                                            String productId, String sort, String isReseller) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_OPERATOR_ID, operatorId);
        requestParams.putString(PARAM_CLIENT_NUMBER, clientNumber);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_SORT, sort);
        requestParams.putString(PARAM_IS_RESELLER, isReseller);
        return requestParams;
    }

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

}
