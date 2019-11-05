package com.tokopedia.imagesearch.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.helper.UrlParamUtils;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class RefreshImageSearchUseCase extends UseCase<SearchResultModel> {

    private static final String VAR_IMAGE = "image";
    private static final String VAR_PARAMS = "params";

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PAGE_SIZE = "page_size";
    private static final String PARAM_DEVICE = "device";
    private static final String PARAM_SOURCE = "source";
    private static final String PARAM_TOKEN = "token";

    private final static String DEFAULT_PAGE_SIZE = "100";
    private final static String DEFAULT_PAGE = "0";

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
    public Observable<SearchResultModel> createObservable(RequestParams params) {

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
        params.putString(PARAM_PAGE, DEFAULT_PAGE);
        params.putString(PARAM_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        params.putString(PARAM_DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        params.putString(PARAM_SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        params.putString(PARAM_TOKEN, token);
        params.putAll(searchParameter.getSearchParameterMap());

        return params;
    }
}
