package com.tokopedia.reksadana.view.presenter;

import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.view.data.common.Result;
import com.tokopedia.reksadana.view.data.initdata.Data;
import com.tokopedia.reksadana.view.data.initdata.FieldData;
import com.tokopedia.reksadana.view.data.initdata.Register;
import com.tokopedia.reksadana.view.data.signimageurl.GetSignUrl;
import com.tokopedia.reksadana.view.data.signimageurl.ImageDetails;
import com.tokopedia.reksadana.view.data.submit.UserDetails;
import com.tokopedia.reksadana.domain.UploadImageUseCase;
import com.tokopedia.usecase.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class ReksaDanaPresenter extends BaseDaggerPresenter<ReksaDanaContract.View> implements ReksaDanaContract.Presenter {

    private static final String UPLOADING_IMAGE = "Uploading image";
    private static final String UPLOADING_DATA = "Uploading data";
    private static final String USER_DETAILS = "userDetails";
    private static final String IMAGE_DETAILS = "imageDetails";
    private static final String EMAIL = "email";
    private static final String VALIDATING_EMAIL = "Validating Email";
    private GraphqlUseCase initDataUseCase;
    private GraphqlUseCase signImageUrlUseCase;
    private GraphqlUseCase emailValidateUseCase;
    private GraphqlUseCase submitUseCase;
    private UploadImageUseCase uploadUseCase;
    private String mSignedUrl;
    private String mPublicUrl;

    @Inject
    public ReksaDanaPresenter(GraphqlUseCase useCase, GraphqlUseCase signImageUrlUseCase, GraphqlUseCase emailValidateUseCase, GraphqlUseCase submitUseCase) {
        this.initDataUseCase = useCase;
        this.signImageUrlUseCase = signImageUrlUseCase;
        this.emailValidateUseCase = emailValidateUseCase;
        this.submitUseCase = submitUseCase;
    }

    @Override
    public void fetchData() {
        getView().setProgressVisility();
        initDataUseCase.setRequest(new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.register_form_query), Data.class));
        initDataUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
                getView().disableProgressVisibility();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                Data data = graphqlResponse.getData(Data.class);
                Register register = data.register();
                List<FieldData> fieldDataList = register.fieldData();
                Result result = register.result();

                if (result.success()) {
                    getView().setOccupation(fieldDataList.get(1));
                    getView().setEducation(fieldDataList.get(2));
                    getView().setIncomeSource(fieldDataList.get(3));
                    getView().setIncome(fieldDataList.get(4));
                    getView().setInvestment(fieldDataList.get(5));
                }
                getView().disableProgressVisibility();
            }
        });
    }

    @Override
    public void submitData() {
        getView().setProgressVisility();
        getView().setProgressText(VALIDATING_EMAIL);

        Map<String, Object> variables = new HashMap<>();
        variables.put(EMAIL, getView().getEmail());
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.validate_email), com.tokopedia.reksadana.view.data.emailvalidate.Data.class, variables);
        emailValidateUseCase.setRequest(graphqlRequest);
        emailValidateUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("sandeep", "error in email validation =" + e.toString());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                com.tokopedia.reksadana.view.data.emailvalidate.Data data = graphqlResponse.getData(com.tokopedia.reksadana.view.data.emailvalidate.Data.class);
                if (data.mf_get_sign_url().result().success()) {
                    if (data.mf_get_sign_url().valid()) {
                        getView().setProgressText(UPLOADING_IMAGE);
                        Log.e("sandeep", "response = " + data);

                        uploadUseCase = new UploadImageUseCase(getView().getAppContext(), mSignedUrl);

                        RequestParams params = RequestParams.create();
                        params.putString(UploadImageUseCase.KEY_FILE_NAME, getView().getFileLoc());
                        Log.e("sandeep", getView().getFileLoc());
                        uploadUseCase.execute(params, new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("sandeep", "erro r = " + e.toString());
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                Log.e("sandeep","response of upload ="+responseBody.toString());
                                getView().setProgressText(UPLOADING_DATA);
                                if (responseBody != null) {
                                    CommonUtils.dumper(responseBody.toString());
                                }
                                Map<String, Object> variables = new HashMap<>();
                                getView().saveSignature();
                                UserDetails details = getView().getRegistrationData(mPublicUrl);
                                variables.put(USER_DETAILS, details);
                                Log.e("sandeep", details.toString());
                                GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                                        R.raw.submit_registration_data), com.tokopedia.reksadana.view.data.submit.Data.class, variables);
                                submitUseCase.setRequest(graphqlRequest);
                                submitUseCase.execute(new Subscriber<GraphqlResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("sandeep", "error in submit =" + e.toString());
                                    }

                                    @Override
                                    public void onNext(GraphqlResponse graphqlResponse) {
                                        getView().disableProgressVisibility();
                                        Log.e("sandeep", "response = " + graphqlResponse.toString());
                                    }
                                });
                            }
                        });
                    } else {
                        getView().disableProgressVisibility();
                        Log.e("sandeep", "invalid email entered");
                    }
                } else {
                    getView().disableProgressVisibility();
                    Log.e("sandeep", "failed due to : " + data.mf_get_sign_url().message());
                }

            }
        });
    }

    public void getSignImageUrl(ImageDetails imageDetails) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(IMAGE_DETAILS, imageDetails);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.get_sign_image_url), com.tokopedia.reksadana.view.data.signimageurl.Data.class, variables);
        signImageUrlUseCase.setRequest(graphqlRequest);
        signImageUrlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                com.tokopedia.reksadana.view.data.signimageurl.Data data = graphqlResponse.getData(com.tokopedia.reksadana.view.data.signimageurl.Data.class);
                GetSignUrl getSignUrl = data.mf_get_sign_url();
                Log.e("sandeep", getSignUrl.publicURL());
                try {
                    Log.e("sandeep", URLDecoder.decode(getSignUrl.signedURL(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                mSignedUrl = getSignUrl.signedURL();
                mPublicUrl = getSignUrl.publicURL();
            }
        });
    }
}
