package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData;
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
    static final String PARAM_TYPE = "type";
    static final String PARAM_TOKEN = "token";
    static final String PARAM_IMAGE_LIST = "image_list";
    static final String PARAM_MAIN_IMAGE_INDEX = "main_image_index";
    static final String TYPE_AFFILIATE = "affiliate";

    private static final String PARAM_AD_ID = "adID";
    private static final String PARAM_PRODUCT_ID = "productID";
    private static final String PARAM_INPUT = "input";

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
        int mainImageIndex = requestParams.getInt(PARAM_MAIN_IMAGE_INDEX, 0);
        return uploadMultipleImageUseCase
                .createObservable(
                        UploadMultipleImageUseCase.createRequestParams(
                                getMediumList(imageList, mainImageIndex)
                        )
                )
                .flatMap(submitPostToGraphql(requestParams));
    }

    private List<SubmitPostMedium> getMediumList(List<String> imageList, int mainImageIndex) {
        List<SubmitPostMedium> mediumList = new ArrayList<>();
        mediumList.add(new SubmitPostMedium(imageList.get(mainImageIndex), 0));
        for (int i = 0; i < imageList.size(); i++) {
            if (i != mainImageIndex) {
                mediumList.add(new SubmitPostMedium(imageList.get(i), mediumList.size()));
            }
        }
        return mediumList;
    }


    private Func1<List<SubmitPostMedium>, Observable<SubmitPostData>> submitPostToGraphql
            (RequestParams
                     requestParams) {
        return mediumList -> {
            String query = GraphqlHelper.loadRawString(
                    context.getResources(),
                    R.raw.mutation_af_submit_post
            );

            HashMap<String, Object> variables = new HashMap<>();
            variables.put(PARAM_INPUT, getContentSubmitInput(requestParams, mediumList));

            GraphqlRequest graphqlRequest = new GraphqlRequest(
                    query,
                    SubmitPostData.class,
                    variables, false);

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

    protected ContentSubmitInput getContentSubmitInput(RequestParams requestParams,
                                                       List<SubmitPostMedium> mediumList) {
        ContentSubmitInput input = new ContentSubmitInput();
        input.setType(requestParams.getString(PARAM_TYPE, ""));
        input.setToken(requestParams.getString(PARAM_TOKEN, ""));
        input.setAdID(requestParams.getString(PARAM_AD_ID, ""));
        input.setProductID(requestParams.getString(PARAM_PRODUCT_ID, ""));
        input.setMedia(mediumList);
        return input;
    }

    public static RequestParams createRequestParams(String productId, String adId, String token,
                                                    List<String> imageList, int mainImageIndex) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_TYPE, TYPE_AFFILIATE);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_AD_ID, adId);
        requestParams.putString(PARAM_TOKEN, token);
        requestParams.putObject(PARAM_IMAGE_LIST, imageList);
        requestParams.putInt(PARAM_MAIN_IMAGE_INDEX, mainImageIndex);
        return requestParams;
    }
}
