package com.tokopedia.imagesearch.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.helper.UrlParamUtils;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class RefreshImageSearchUseCase extends UseCase<ProductViewModel> {

    private static final String VAR_IMAGE = "image";
    private static final String VAR_PARAMS = "params";

    private ImageProductMapper productMapper;
    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public RefreshImageSearchUseCase(Context context,
                                     GraphqlUseCase graphqlUseCase,
                                     ImageProductMapper imageProductMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.productMapper = imageProductMapper;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams params) {

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_image_search), ImageSearchProductResponse.class);

        graphqlRequest.setVariables(createParametersForQuery(params.getParameters()));

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(productMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(VAR_IMAGE, "");
        variables.put(VAR_PARAMS, UrlParamUtils.generateUrlParamString(parameters));

        return variables;
    }

    public static RequestParams generateParams(String token, SearchParameter searchParameter) {
        RequestParams params = RequestParams.create();
        params.putString(SearchApiConst.PAGE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_START);
        params.putString(SearchApiConst.PAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_PAGE_SIZE);
        params.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        params.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_IMAGE_SEARCH);
        params.putString(SearchApiConst.TOKEN, token);
        params.putAll(searchParameter.getSearchParameterMap());

        return params;
    }
}
