package com.tokopedia.digital.common.domain.interactor;

import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class GetDigitalCategoryByIdUseCase extends UseCase<ProductDigitalData> {

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";
    private final String PARAM_PRODUCT_ID = "product_id";
    private final String PARAM_CLIENT_NUMBER = "client_number";
    private final String PARAM_SORT = "sort";
    private final String PARAM_NEED_FAVORITE_LIST = "need_favorite_list";
    private final String DEFAULT_EMPTY_FIELD = "";

    private IDigitalCategoryRepository digitalCategoryRepository;
    private UserSessionInterface userSession;

    public GetDigitalCategoryByIdUseCase(IDigitalCategoryRepository digitalCategoryRepository, UserSessionInterface userSession) {
        this.digitalCategoryRepository = digitalCategoryRepository;
        this.userSession = userSession;
    }

    @Override
    public Observable<ProductDigitalData> createObservable(RequestParams requestParams) {
        final String categoryId = requestParams.getString(PARAM_CATEGORY_ID, "");
        final String operatorId = requestParams.getString(PARAM_OPERATOR_ID, "");
        final String productId = requestParams.getString(PARAM_PRODUCT_ID, "");
        final String clientNumber = requestParams.getString(PARAM_CLIENT_NUMBER, "");
        boolean needFavoriteList = requestParams.getBoolean(PARAM_NEED_FAVORITE_LIST, false);

        if (needFavoriteList) {
            //fetch category detail and favorit both if user is logged in
            if (userSession.isLoggedIn()) {
                return digitalCategoryRepository.getCategoryWithFavorit(categoryId, operatorId, clientNumber, productId);
            }

            //fetch category detail if user is not logged in
            return digitalCategoryRepository.getCategory(categoryId)
                    .map(productDigitalData -> {
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
                    });
        } else {
            return digitalCategoryRepository.getCategory(categoryId);
        }
    }

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
}
