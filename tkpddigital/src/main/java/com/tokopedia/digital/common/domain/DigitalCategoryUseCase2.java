package com.tokopedia.digital.common.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.data.repository.IDigitalRepository;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.widget.model.DigitalNumberList;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Rizky on 19/01/18.
 */

public class DigitalCategoryUseCase2 extends UseCase<ProductDigitalData> {

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_SORT = "sort";

    private Context context;
    private IDigitalRepository digitalRepository;

    public DigitalCategoryUseCase2(Context context, IDigitalRepository digitalRepository) {
        this.context = context;
        this.digitalRepository = digitalRepository;
    }

    @Override
    public Observable<ProductDigitalData> createObservable(RequestParams requestParams) {
        String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        String sort = requestParams.getString(PARAM_SORT, "");

        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();

        TKPDMapParam<String, String> paramQueryFavoriteList = new TKPDMapParam<>();
        paramQueryFavoriteList.put(PARAM_CATEGORY_ID, categoryId);
        paramQueryFavoriteList.put(PARAM_SORT, sort);

        return Observable.zip(
                digitalRepository.getCategory(categoryId, getGeneratedAuthParamNetwork(paramQueryCategory)),
                getFavoriteList(getGeneratedAuthParamNetwork(paramQueryFavoriteList)),
                getZipFunctionProductDigitalData());
    }

    private Observable<DigitalNumberList> getFavoriteList
            (TKPDMapParam<String, String> paramQueryLastNumber) {
        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
            return digitalRepository.getFavoriteList(paramQueryLastNumber);
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

    public RequestParams createRequestParam(String categoryId, String sort) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_SORT, sort);
        return requestParams;
    }

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

}
