package com.tokopedia.common.travel.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.data.TravelPassengerRepository;
import com.tokopedia.common.travel.data.entity.ResponseTravelEditPassenger;
import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 23/11/18.
 */
public class EditTravelPassengerUseCase extends BaseTravelPassengerUseCase<Boolean> {

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private String idPassengerPrevious;
    private ITravelPassengerRepository repository;

    @Inject
    public EditTravelPassengerUseCase(GraphqlUseCase graphqlUseCase,
                                      @ApplicationContext Context context,
                                      TravelPassengerRepository repository) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
        this.repository = repository;
    }

    public void setIdPassengerPrevious(String idPassengerPrevious) {
        this.idPassengerPrevious = idPassengerPrevious;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_travel_edit_passenger);
                        Map<String, Object> variableGql = requestParams.getParameters();

                        if (!TextUtils.isEmpty(query)) {
                            GraphqlRequest request = new GraphqlRequest(query, ResponseTravelEditPassenger.class,
                                    variableGql);
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(request);
                            return graphqlUseCase.createObservable(null);
                        }

                        return Observable.error(new Exception("Query and/or variable are empty."));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseTravelEditPassenger>() {
                    @Override
                    public ResponseTravelEditPassenger call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseTravelEditPassenger.class);
                    }
                })
                .map(new Func1<ResponseTravelEditPassenger, TravelPassengerEntity>() {
                    @Override
                    public TravelPassengerEntity call(ResponseTravelEditPassenger responseTravelEditPassenger) {
                        return responseTravelEditPassenger.getTravelPassengerEntity();
                    }
                })
                .flatMap(new Func1<TravelPassengerEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(TravelPassengerEntity travelPassengerEntity) {
                        return repository.updateDataTravelPassenger(idPassengerPrevious, travelPassengerEntity);
                    }
                });
    }
}
