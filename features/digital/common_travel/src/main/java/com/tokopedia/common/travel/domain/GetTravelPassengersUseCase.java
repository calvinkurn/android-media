package com.tokopedia.common.travel.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.data.TravelPassengerDataStoreFactory;
import com.tokopedia.common.travel.data.TravelPassengerListSpecification;
import com.tokopedia.common.travel.data.entity.ResponseTravelPassengerList;
import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class GetTravelPassengersUseCase extends UseCase<List<TravelPassenger>> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private TravelPassengerDataStoreFactory travelPassengerDataStoreFactory;
    private boolean resetPassengerListSelected;

    public void setResetPassengerListSelected(boolean resetPassengerListSelected) {
        this.resetPassengerListSelected = resetPassengerListSelected;
    }

    @Inject
    public GetTravelPassengersUseCase(@ApplicationContext Context context,
                                      GraphqlUseCase graphqlUseCase,
                                      TravelPassengerDataStoreFactory travelPassengerDataStoreFactory) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.travelPassengerDataStoreFactory = travelPassengerDataStoreFactory;
    }

    @Override
    public Observable<List<TravelPassenger>> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_travel_get_passenger);

                        if (!TextUtils.isEmpty(query)) {
                            GraphqlRequest request = new GraphqlRequest(query, ResponseTravelPassengerList.class);
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(request);
                            return graphqlUseCase.createObservable(null);
                        }

                        return Observable.error(new Exception("Query and/or variable are empty."));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseTravelPassengerList>() {
                    @Override
                    public ResponseTravelPassengerList call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseTravelPassengerList.class);
                    }
                })
                .map(new Func1<ResponseTravelPassengerList, List<TravelPassengerEntity>>() {
                    @Override
                    public List<TravelPassengerEntity> call(ResponseTravelPassengerList responseTravelPassengerList) {
                        return responseTravelPassengerList.getTravelPassengerListEntity().getTravelPassengerEntityList();
                    }
                })
                .flatMap(new Func1<List<TravelPassengerEntity>, Observable<List<TravelPassenger>>>() {
                    @Override
                    public Observable<List<TravelPassenger>> call(List<TravelPassengerEntity> travelPassengerEntities) {
                        return travelPassengerDataStoreFactory.getPassengerListLocal(travelPassengerEntities, resetPassengerListSelected, new TravelPassengerListSpecification());
                    }
                })
                .map(new Func1<List<TravelPassenger>, List<TravelPassenger>>() {
                    @Override
                    public List<TravelPassenger> call(List<TravelPassenger> travelPassengers) {
                        return travelPassengers;
                    }
                });
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }

}
