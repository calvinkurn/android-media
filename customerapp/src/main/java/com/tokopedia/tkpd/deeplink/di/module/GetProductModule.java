package com.tokopedia.tkpd.deeplink.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.detail.common.data.model.ProductInfo;
import com.tokopedia.tkpd.R;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public class GetProductModule{

    @Provides
    GraphqlRepository provideGraphqlRepository(@ApplicationContext Context context){
        GraphqlClient.init(context);
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    @Named("productUseCase")
    GraphqlUseCase<ProductInfo.Response> getProductUseCase(@ApplicationContext Context context, GraphqlRepository graphqlRepository){
        GraphqlUseCase<ProductInfo.Response> useCase = new GraphqlUseCase<>(graphqlRepository);
        useCase.setTypeClass(ProductInfo.Response.class);
        useCase.setGraphqlQuery(GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_get_product_info));
        return useCase;
    }
}