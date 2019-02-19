package com.tokopedia.common.travel.ticker.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.ticker.data.response.TravelTickerAttribute;
import com.tokopedia.common.travel.ticker.data.response.TravelTickerEntity;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 19/02/19
 */
public class TravelTickerUseCase extends UseCase<TravelTickerViewModel> {

    private static final String PARAM_INSTANCE_NAME = "instanceName";
    private static final String PARAM_PAGE = "tickerPage";
    private static final String PARAM_DID = "did";
    private static final String TRAVEL_TICKER_OPERATION_NAME = "travelTicker";
    private static final String ANDROID_DEVICE_ID = "5";

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    @Inject
    public TravelTickerUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TravelTickerViewModel> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(reqParams -> {
                    HashMap<String, String> params = new HashMap<>();
                    if (reqParams != null) {
                        params.put(PARAM_DID, ANDROID_DEVICE_ID);
                        params.put(PARAM_INSTANCE_NAME, reqParams.getString(PARAM_INSTANCE_NAME, ""));
                        params.put(PARAM_PAGE, reqParams.getString(PARAM_PAGE, ""));
                    }

                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_travel_ticker);

                    if (!TextUtils.isEmpty(query)) {
                        GraphqlRequest request = new GraphqlRequest(query, TravelTickerEntity.class);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(graphqlResponse -> (TravelTickerEntity) graphqlResponse.getData(TravelTickerEntity.class))
                .map(travelTickerEntity -> travelTickerEntity.getTravelTicker())
                .flatMap(travelTickerAttribute -> Observable.just(
                        mapToTravelTickerViewModel(travelTickerAttribute)));
    }

    private TravelTickerViewModel mapToTravelTickerViewModel(TravelTickerAttribute travelTickerAttribute) {
        return new TravelTickerViewModel(
                travelTickerAttribute.getTitle(),
                travelTickerAttribute.getMessage(),
                travelTickerAttribute.getUrl(),
                travelTickerAttribute.getType(),
                travelTickerAttribute.getStatus(),
                travelTickerAttribute.getEndTime(),
                travelTickerAttribute.getStartTime(),
                travelTickerAttribute.getInstances(),
                travelTickerAttribute.getPage(),
                travelTickerAttribute.isPeriod());
    }

    public RequestParams createRequestParams(String instanceName, String tickerPage) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_INSTANCE_NAME, instanceName);
        requestParams.putString(PARAM_PAGE, tickerPage);
        return requestParams;
    }
}
