package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput;
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/19/18.
 */
public class EditPostUseCase extends SubmitPostUseCase {
    private static final String PARAM_POST_ID = "ID";
    private static final String PARAM_ACTION = "action";
    private static final String ACTION_UPDATE = "update";

    @Inject
    EditPostUseCase(@ApplicationContext Context context,
                    UploadMultipleImageUseCase uploadMultipleImageUseCase,
                    GraphqlUseCase graphqlUseCase) {
        super(context, uploadMultipleImageUseCase, graphqlUseCase);
    }

    @Override
    protected ContentSubmitInput getContentSubmitInput(RequestParams requestParams,
                                                       List<SubmitPostMedium> mediumList) {
        ContentSubmitInput input = new ContentSubmitInput();
        input.setType(requestParams.getString(Companion.getPARAM_TYPE(), ""));
        input.setToken(requestParams.getString(Companion.getPARAM_TOKEN(), ""));
        input.setActivityId(requestParams.getString(PARAM_POST_ID, ""));
        input.setAction(requestParams.getString(PARAM_ACTION, ""));
        input.setMedia(mediumList);
        return input;
    }

    public static RequestParams createRequestParams(String postId, String token,
                                                    List<String> imageList, int mainImageIndex) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Companion.getPARAM_TYPE(), Companion.getTYPE_AFFILIATE());
        requestParams.putString(PARAM_POST_ID, postId);
        requestParams.putString(Companion.getPARAM_TOKEN(), token);
        requestParams.putString(PARAM_ACTION, ACTION_UPDATE);
        requestParams.putObject(Companion.getPARAM_IMAGE_LIST(), imageList);
        requestParams.putInt(Companion.getPARAM_MAIN_IMAGE_INDEX(), mainImageIndex);
        return requestParams;
    }
}
