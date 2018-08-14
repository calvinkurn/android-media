package com.tokopedia.common.travel.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.entity.ResponseTravelPassengerList;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 14/08/18.
 */
public class GetTravelPassengersUseCase {

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    @Inject
    public GetTravelPassengersUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(Subscriber<GraphqlResponse> getPassengerListSubsriber) {
        graphqlUseCase.clearRequest();

        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_travel_get_passenger);
        GraphqlRequest request = new GraphqlRequest(query, ResponseTravelPassengerList.class);

        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(getPassengerListSubsriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }

}
