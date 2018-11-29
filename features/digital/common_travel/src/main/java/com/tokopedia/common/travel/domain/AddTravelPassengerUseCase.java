package com.tokopedia.common.travel.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.data.entity.ResponseTravelAddPassenger;
import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 12/11/18.
 */
public class AddTravelPassengerUseCase extends BaseTravelPassengerUseCase<TravelPassenger> {

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    @Inject
    public AddTravelPassengerUseCase(GraphqlUseCase graphqlUseCase,
                                     @ApplicationContext Context context) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
    }

    @Override
    public Observable<TravelPassenger> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_travel_add_passenger);
                        Map<String, Object> variableGql = requestParams.getParameters();

                        if (!TextUtils.isEmpty(query)) {
                            GraphqlRequest request = new GraphqlRequest(query, ResponseTravelAddPassenger.class,
                                    variableGql);
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(request);
                            return graphqlUseCase.createObservable(null);
                        }

                        return Observable.error(new Exception("Query and/or variable are empty."));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseTravelAddPassenger>() {
                    @Override
                    public ResponseTravelAddPassenger call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseTravelAddPassenger.class);
                    }
                })
                .map(new Func1<ResponseTravelAddPassenger, TravelPassengerEntity>() {
                    @Override
                    public TravelPassengerEntity call(ResponseTravelAddPassenger responseTravelAddPassenger) {
                        return responseTravelAddPassenger.getTravelPassengerEntity();
                    }
                })
                .map(new Func1<TravelPassengerEntity, TravelPassenger>() {
                    @Override
                    public TravelPassenger call(TravelPassengerEntity travelPassengerEntity) {
                        TravelPassenger travelPassenger = new TravelPassenger();
                        travelPassenger.setId(travelPassengerEntity.getId());
                        travelPassenger.setTravelId(travelPassengerEntity.getTravelId());
                        travelPassenger.setName(travelPassengerEntity.getName());
                        return travelPassenger;
                    }
                });
    }
}
