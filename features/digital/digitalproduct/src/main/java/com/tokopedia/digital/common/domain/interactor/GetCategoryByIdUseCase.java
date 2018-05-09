package com.tokopedia.digital.common.domain.interactor;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class GetCategoryByIdUseCase extends UseCase<ProductDigitalData> {

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";
    private final String PARAM_PRODUCT_ID = "product_id";
    private final String PARAM_CLIENT_NUMBER = "client_number";
    private final String PARAM_SORT = "sort";
    private final String DEFAULT_EMPTY_FIELD = "";

    private final String PARAM_IS_RESELLER = "is_reseller";
    private final String PARAM_VALUE_IS_RESELLER = "1";

    private final String PARAM_NEED_FAVORITE_LIST = "need_favorite_list";

    private Context context;
    private IDigitalCategoryRepository digitalCategoryRepository;

    public GetCategoryByIdUseCase(Context context, IDigitalCategoryRepository digitalCategoryRepository) {
        this.context = context;
        this.digitalCategoryRepository = digitalCategoryRepository;
    }

    @Override
    public Observable<ProductDigitalData> createObservable(RequestParams requestParams) {
        final String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        final String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");
        final String productId = requestParams.getString(PARAM_PRODUCT_ID, "");
        final String clientNumber = requestParams.getString(PARAM_CLIENT_NUMBER, "");
        String sort = requestParams.getString(PARAM_SORT, "");
        boolean needFavoriteList = requestParams.getBoolean(PARAM_NEED_FAVORITE_LIST, false);

//        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
//        if (GlobalConfig.isSellerApp()) {
//            paramQueryCategory.put(PARAM_IS_RESELLER, PARAM_VALUE_IS_RESELLER);
//        }

        if (needFavoriteList) {
            //fetch category detail and favorit both if user is not logged in
            if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
                return digitalCategoryRepository.getCategoryWithFavorit(categoryId, operatorId, clientNumber, productId);
            }

            //fetch category detail if user is not logged in
            return digitalCategoryRepository.getCategory(categoryId)
                    .map(new Func1<ProductDigitalData, ProductDigitalData>() {
                        @Override
                        public ProductDigitalData call(ProductDigitalData productDigitalData) {
                            if (productDigitalData != null) {
                                OrderClientNumber orderClientNumber = new OrderClientNumber.Builder()
                                        .categoryId(categoryId)
                                        .operatorId(operatorId)
                                        .clientNumber(clientNumber)
                                        .name(DEFAULT_EMPTY_FIELD)
                                        .productId(productId)
                                        .build();

                                HistoryClientNumber historyClientNumber = productDigitalData.getHistoryClientNumber();
                                if (historyClientNumber == null) {
                                    historyClientNumber = new HistoryClientNumber();
                                }
                                historyClientNumber.setLastOrderClientNumber(orderClientNumber);
                                productDigitalData.setHistoryClientNumber(historyClientNumber);
                            }
                            return productDigitalData;
                        }
                    });
        } else {
            return digitalCategoryRepository.getCategory(categoryId);
        }
    }

//    private Observable<DigitalNumberList> getFavoriteList
//            (TKPDMapParam<String, String> paramQueryLastNumber) {
//        if (SessionHandler.isV4Login(MainApplication.getAppContext())) {
//            return digitalCategoryRepository.getFavoriteList(paramQueryLastNumber);
//        } else {
//            List<OrderClientNumber> orderClientNumbers = new ArrayList<>();
//            DigitalNumberList digitalNumberList = new DigitalNumberList(orderClientNumbers, null);
//            if (paramQueryLastNumber.get(PARAM_CATEGORY_ID) != null
//                    && paramQueryLastNumber.get(PARAM_OPERATOR_ID) != null) {
//                String productId =
//                        paramQueryLastNumber.get(PARAM_OPERATOR_ID) == null ? DEFAULT_EMPTY_FIELD :
//                                paramQueryLastNumber.get(PARAM_OPERATOR_ID);
//                String clientNumber =
//                        paramQueryLastNumber.get(PARAM_CLIENT_NUMBER) == null ? DEFAULT_EMPTY_FIELD :
//                                paramQueryLastNumber.get(PARAM_CLIENT_NUMBER);
//                OrderClientNumber orderClientNumber = new OrderClientNumber.Builder()
//                        .categoryId(paramQueryLastNumber.get(PARAM_CATEGORY_ID))
//                        .operatorId(paramQueryLastNumber.get(PARAM_OPERATOR_ID))
//                        .clientNumber(clientNumber)
//                        .name(DEFAULT_EMPTY_FIELD)
//                        .productId(productId)
//                        .build();
//                digitalNumberList = new DigitalNumberList(orderClientNumbers, orderClientNumber);
//            }
//            return Observable.just(digitalNumberList);
//        }
//    }

    public RequestParams createRequestParam(String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        return requestParams;
    }

    public RequestParams createRequestParam(String categoryId, String sort, boolean needFavoriteList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_SORT, sort);
        requestParams.putBoolean(PARAM_NEED_FAVORITE_LIST, needFavoriteList);
        return requestParams;
    }

    public RequestParams createRequestParam(String categoryId, String operatorId, String productId,
                                            String clientNumber, String sort, boolean needFavoriteList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_SORT, sort);
        requestParams.putString(PARAM_OPERATOR_ID, operatorId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_CLIENT_NUMBER, clientNumber);
        requestParams.putBoolean(PARAM_NEED_FAVORITE_LIST, needFavoriteList);
        return requestParams;
    }

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

}
