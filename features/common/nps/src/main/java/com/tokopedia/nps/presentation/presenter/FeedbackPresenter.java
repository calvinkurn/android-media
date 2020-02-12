package com.tokopedia.nps.presentation.presenter;

import android.os.Build;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.nps.domain.Feedback;
import com.tokopedia.nps.domain.interactor.PostFeedbackUseCase;
import com.tokopedia.nps.presentation.view.FeedbackView;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Field;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by meta on 28/06/18.
 */
public class FeedbackPresenter {

    private FeedbackView mView;

    private PostFeedbackUseCase postFeedbackUseCase;

    @Inject
    public FeedbackPresenter(PostFeedbackUseCase postFeedbackUseCase) {
        this.postFeedbackUseCase = postFeedbackUseCase;
    }

    public void setView(FeedbackView mView) {
        this.mView = mView;
    }

    public void post(String rating, String category, String comment) {
        this.mView.hideRetry();
        this.mView.showLoading();

        UserSession userSession = new UserSession(mView.context());

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("rating", rating);
        requestParams.putString("category", category);
        requestParams.putString("user_id", userSession.getUserId());
        requestParams.putString("comment", comment);
        requestParams.putString("app_version", GlobalConfig.VERSION_NAME);
        requestParams.putString("device_model", String.format("%s %s", Build.MANUFACTURER, Build.MODEL));
        requestParams.putString("os_type", "1");

        postFeedbackUseCase.execute(requestParams, new FeedbackSubscriber());
    }

    public void onDestroy() {
        this.postFeedbackUseCase.unsubscribe();
        this.mView = null;
    }


    private class FeedbackSubscriber extends Subscriber<Feedback> {

        @Override
        public void onCompleted() {
            FeedbackPresenter.this.mView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            FeedbackPresenter.this.mView.hideLoading();
            FeedbackPresenter.this.mView.showError(e.getMessage());
            FeedbackPresenter.this.mView.showRetry();
        }

        @Override
        public void onNext(Feedback feedback) {
            if (feedback.isSuccess()) {
                FeedbackPresenter.this.mView.successPostFeedback();
            }
        }
    }
}
