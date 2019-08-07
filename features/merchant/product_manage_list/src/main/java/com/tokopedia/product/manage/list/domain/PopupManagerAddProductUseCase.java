package com.tokopedia.product.manage.list.domain;

import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.product.manage.list.data.model.PopUpManagerViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.product.manage.list.view.presenter.ProductManagePresenterImpl.GQL_POPUP_NAME;

public class PopupManagerAddProductUseCase extends UseCase<Boolean> {

    private String popUpQuery;
    private GraphqlUseCase useCase;

    @Inject
    public PopupManagerAddProductUseCase(@Named(GQL_POPUP_NAME) String popUpQuery, GraphqlUseCase useCase) {
        this.popUpQuery = popUpQuery;
        this.useCase = useCase;
    }

    private static final String SHOP_ID = "shopID";

    public static RequestParams createRequestParams(int shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(SHOP_ID,shopId);
        return requestParams;
    }

    @Override public Observable<Boolean> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(popUpQuery, PopUpManagerViewModel.class, requestParams.getParameters());
        useCase.clearRequest();
        useCase.addRequest(graphqlRequest);
        return useCase.createObservable(requestParams).map(it -> {
            PopUpManagerViewModel data = it.getData(PopUpManagerViewModel.class);
            List<GraphqlError> error = it.getError(GraphqlError.class);

            if (data == null ){
                throw new RuntimeException();
            } else if (error != null && !error.isEmpty() && !error.get(0).getMessage().isEmpty()) {
                throw new RuntimeException();
            }
            return data.getShopManagerPopups().getPopupsData().isShowPopUp();
        }
        );
    }
}
