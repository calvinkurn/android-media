package com.tokopedia.useridentification.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.domain.pojo.UploadIdentificationPojo;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 13/11/18.
 */
public class UploadIdentificationUseCase {

    public static final int TYPE_KTP = 1;
    public static final int TYPE_SELFIE = 2;

    private static final String KYC_TYPE = "kycType";
    private static final String PIC_OBJ_KYC = "picObjKYC";
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

    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .mutation_upload_kyc);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                UploadIdentificationPojo.class, params.getParameters(), false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(params);
    }
    /**
     *
     * @param kycType from TYPE_KTP OR TYPE_SELFIE
     * @param picObjKyc from uploadapp
     * @return request params
     */
    public static RequestParams getRequestParam(int kycType,
                                                      String picObjKyc) {
        RequestParams param = RequestParams.create();
        param.putInt(KYC_TYPE, kycType);
        param.putString(PIC_OBJ_KYC, picObjKyc);
        return param;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
