package com.tokopedia.challenges.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.contractor.SubmissionAdapterContract;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class SubmissionAdapterPresenter extends BaseDaggerPresenter<SubmissionAdapterContract.View>
        implements SubmissionAdapterContract.Presenter {

    private PostSubmissionLikeUseCase postSubmissionLikeUseCase;

    @Inject
    public SubmissionAdapterPresenter(PostSubmissionLikeUseCase postSubmissionLikeUseCase) {
        this.postSubmissionLikeUseCase = postSubmissionLikeUseCase;
    }

    public void initialize() {

    }

    @Override
    public void onDestroy() {
        postSubmissionLikeUseCase.unsubscribe();
    }

    @Override
    public void setSubmissionLike(final SubmissionResult result, final int position) {

        RequestParams requestParams = RequestParams.create();
        if (result.getMe() != null)
            requestParams.putBoolean(PostSubmissionLikeUseCase.IS_LIKED, !result.getMe().isLiked());
        if (!TextUtils.isEmpty(result.getId()))
            requestParams.putString(Utils.QUERY_PARAM_SUBMISSION_ID, result.getId());
        postSubmissionLikeUseCase.execute(requestParams, new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });

    }
}
