package com.tokopedia.common.travel.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.data.TravelPassengerRepository;
import com.tokopedia.common.travel.data.entity.ResponseTravelPassengerList;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class GetTravelPassengersUseCase extends UseCase<List<TravelPassenger>> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private ITravelPassengerRepository travelPassengerRepository;
    private boolean resetPassengerListSelected;
    private String idPassengerSelected;

    public void setResetPassengerListSelected(boolean resetPassengerListSelected) {
        this.resetPassengerListSelected = resetPassengerListSelected;
    }

    public void setTravelPassengerSelected(String idPassengerSelected) {
        this.idPassengerSelected = idPassengerSelected;
    }

    @Inject
    public GetTravelPassengersUseCase(@ApplicationContext Context context,
                                      GraphqlUseCase graphqlUseCase,
                                      TravelPassengerRepository travelPassengerRepository) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.travelPassengerRepository = travelPassengerRepository;
    }

    @Override
    public Observable<List<TravelPassenger>> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(reqParams -> {
                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_travel_get_passenger);

                    if (!TextUtils.isEmpty(query)) {
                        GraphqlRequest request = new GraphqlRequest(query, ResponseTravelPassengerList.class, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(graphqlResponse -> (ResponseTravelPassengerList) graphqlResponse.getData(ResponseTravelPassengerList.class))
                .map(responseTravelPassengerList -> responseTravelPassengerList.getTravelPassengerListEntity().getTravelPassengerEntityList())
                .flatMap(travelPassengerEntities -> travelPassengerRepository.findAllTravelPassenger(travelPassengerEntities,
                        resetPassengerListSelected))
                .flatMap(travelPassengers -> {
                    return Observable.zip(
                            getPassengerNotSelected(travelPassengers),
                            getPassengerSelected(travelPassengers),
                            new Func2<List<TravelPassenger>, List<TravelPassenger>, List<TravelPassenger>>() {
                                @Override
                                public List<TravelPassenger> call(List<TravelPassenger> travelPassengerNotSelected, List<TravelPassenger> travelPassengersSelected) {
                                    List<TravelPassenger> travelPassengerList = new ArrayList<>();
                                    travelPassengerList.addAll(travelPassengerNotSelected);
                                    travelPassengerList.addAll(travelPassengersSelected);
                                    return travelPassengerList;
                                }
                            });
                });
    }

    private Observable<List<TravelPassenger>> getPassengerNotSelected(List<TravelPassenger> travelPassengers) {
        return Observable.from(travelPassengers)
                .filter(travelPassenger -> !travelPassenger.isSelected() ||
                        travelPassenger.getIdPassenger().equals(idPassengerSelected))
                .toList();
    }

    private Observable<List<TravelPassenger>> getPassengerSelected(List<TravelPassenger> travelPassengers) {
        return Observable.from(travelPassengers)
                .filter(travelPassenger -> travelPassenger.isSelected() &&
                        !travelPassenger.getIdPassenger().equals(idPassengerSelected))
                .toList();
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }

}
