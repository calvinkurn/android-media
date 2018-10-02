package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.request.ContentSubmitInput;
import com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.request.SubmitPostMedium;
import com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.response.SubmitPostData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/1/18.
 */
public class SubmitPostUseCase extends UseCase<SubmitPostData> {
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_AD_ID = "adID";
    private static final String PARAM_PRODUCT_ID = "productID";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_IMAGE_LIST = "image_list";
    private static final String PARAM_INPUT = "input";
    private static final String TYPE_AFFILIATE = "affiliate";
    private static final String TYPE_IMAGE = "image";

    public static final int SUCCESS = 1;

    private final Context context;
    private final UploadMultipleImageUseCase uploadMultipleImageUseCase;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    SubmitPostUseCase(@ApplicationContext Context context,
                      UploadMultipleImageUseCase uploadMultipleImageUseCase,
                      GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.uploadMultipleImageUseCase = uploadMultipleImageUseCase;
        this.graphqlUseCase = graphqlUseCase;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<SubmitPostData> createObservable(RequestParams requestParams) {
        List<String> imageList = (List<String>) requestParams.getObject(PARAM_IMAGE_LIST);
        return uploadMultipleImageUseCase
                .createObservable(UploadMultipleImageUseCase.createRequestParams(imageList))
                .flatMap(submitPostToGraphql(requestParams));
    }

    private Func1<List<String>, Observable<SubmitPostData>> submitPostToGraphql(RequestParams
                                                                                 requestParams) {
        return imageUrlList -> {
            String query = GraphqlHelper.loadRawString(
                    context.getResources(),
                    R.raw.query_af_content_form
            );

            HashMap<String, Object> variables = new HashMap<>();
            variables.put(PARAM_INPUT, getContentSubmitInput(requestParams, imageUrlList));

            GraphqlRequest graphqlRequest = new GraphqlRequest(
                    query,
                    SubmitPostData.class,
                    variables);

            graphqlUseCase.clearRequest();
            graphqlUseCase.addRequest(graphqlRequest);

            return graphqlUseCase
                    .createObservable(RequestParams.create())
                    .map(mapGraphqlResponse());
        };
    }

    private Func1<GraphqlResponse, SubmitPostData> mapGraphqlResponse() {
        return graphqlResponse -> graphqlResponse.getData(SubmitPostData.class);
    }

    private ContentSubmitInput getContentSubmitInput(RequestParams requestParams,
                                                     List<String> imageUrlList) {
        ContentSubmitInput input = new ContentSubmitInput();
        input.setType(requestParams.getString(PARAM_TYPE, ""));
        input.setAdID(requestParams.getString(PARAM_AD_ID, ""));
        input.setProductID(requestParams.getString(PARAM_PRODUCT_ID, ""));
        input.setToken(requestParams.getString(PARAM_TOKEN, ""));

        List<SubmitPostMedium> mediumList = new ArrayList<>();
        for (int i = 0; i < imageUrlList.size(); i++) {
            mediumList.add(
                    new SubmitPostMedium(imageUrlList.get(i), TYPE_IMAGE, i)
            );
        }
        input.setMedia(mediumList);
        return input;
    }

    public static RequestParams createRequestParams(String adId, String productId, String token,
                                                    List<String> imageList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_TYPE, TYPE_AFFILIATE);
        requestParams.putString(PARAM_AD_ID, adId);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_TOKEN, token);
        requestParams.putObject(PARAM_IMAGE_LIST, imageList);
        return requestParams;
    }
}
