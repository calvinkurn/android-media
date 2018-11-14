package com.tokopedia.useridentification.view.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.useridentification.view.domain.pojo.UploadIdentificationPojo;
import com.tokopedia.useridentification.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 13/11/18.
 */
public class UploadIdentificationUseCase {

    private static final int TYPE_KTP = 1;
    private static final int TYPE_SELFIE = 2;

    private static final String KYC_TYPE = "kyc_type";
    private static final String PIC_OBJ_KYC = "pic_obj_kyc";
    private static final String KYC_NUMBER = "kyc_number";
    private static final String RELATION_ID = "relation_id";
    private static final String AUTO_VERIFY = "auto_verify";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public UploadIdentificationUseCase(@ApplicationContext Context context,
                                       GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;

    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .mutation_upload_kyc);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                UploadIdentificationPojo.class, requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static Map<String, Object> getRequestParam(int kycType,
                                                      String picObjKyc,
                                                      String kycNumber,
                                                      String relationId,
                                                      boolean autoVerify) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(KYC_TYPE, kycType);
        requestParams.put(PIC_OBJ_KYC, picObjKyc);
        requestParams.put(KYC_NUMBER, kycNumber);
        requestParams.put(RELATION_ID, relationId);
        requestParams.put(AUTO_VERIFY, autoVerify);
        return requestParams;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
