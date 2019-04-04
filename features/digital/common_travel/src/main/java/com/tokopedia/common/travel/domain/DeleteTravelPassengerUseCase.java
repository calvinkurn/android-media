package com.tokopedia.common.travel.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.data.TravelPassengerRepository;
import com.tokopedia.common.travel.data.entity.ResponseTravelDeletePassenger;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class DeleteTravelPassengerUseCase extends BaseTravelPassengerUseCase<Boolean> {

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private ITravelPassengerRepository travelPassengerRepository;
    private String idPassengerSelected;

    @Inject
    public DeleteTravelPassengerUseCase(GraphqlUseCase graphqlUseCase,
                                        @ApplicationContext Context context,
                                        TravelPassengerRepository travelPassengerRepository) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
        this.travelPassengerRepository = travelPassengerRepository;
    }

    public void setIdPassengerSelected(String idPassengerSelected) {
        this.idPassengerSelected = idPassengerSelected;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(reqParams -> {
                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_travel_delete_passenger);
                    Map<String, Object> variableGql = reqParams.getParameters();

                    if (!TextUtils.isEmpty(query)) {
                        GraphqlRequest request = new GraphqlRequest(query, ResponseTravelDeletePassenger.class,
                                variableGql, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(graphqlResponse -> (ResponseTravelDeletePassenger) graphqlResponse.getData(ResponseTravelDeletePassenger.class))
                .map(responseTravelDeletePassenger -> responseTravelDeletePassenger.getTravelPassengerEntity())
                .map(travelPassengerEntity -> {
                    TravelPassenger travelPassenger = new TravelPassenger();
                    travelPassenger.setTravelId(travelPassengerEntity.getTravelId());
                    return travelPassenger;
                })
                .flatMap(travelPassenger -> travelPassengerRepository.deletePassenger(idPassengerSelected));
    }
}
