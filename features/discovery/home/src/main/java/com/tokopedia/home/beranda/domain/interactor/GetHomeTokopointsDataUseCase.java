package com.tokopedia.home.beranda.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData;

import javax.inject.Inject;

public class GetHomeTokopointsDataUseCase extends GraphqlUseCase {

    private final Context context;

    @Inject
    public GetHomeTokopointsDataUseCase(@ApplicationContext Context context){
        this.context=context;
    }

    public GraphqlRequest getRequest() {
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.home_gql_tokopoints_details),
                TokopointsDrawerHomeData.class
        );
    }


}
