package com.tokopedia.affiliate.feature.onboarding.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.GetUsernameSuggestionUseCase;
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.RegisterUsernameUseCase;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.subscriber.GetUsernameSuggestionSubscriber;
import com.tokopedia.affiliate.feature.onboarding.view.subscriber.RegisterUsernameSubscriber;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/4/18.
 */
public class UsernameInputPresenter extends BaseDaggerPresenter<UsernameInputContract.View>
        implements UsernameInputContract.Presenter {

    private final GetUsernameSuggestionUseCase getUsernameSuggestionUseCase;
    private final RegisterUsernameUseCase registerUsernameUseCase;

    @Inject
    public UsernameInputPresenter(GetUsernameSuggestionUseCase getUsernameSuggestionUseCase,
                                  RegisterUsernameUseCase registerUsernameUseCase) {
        this.getUsernameSuggestionUseCase = getUsernameSuggestionUseCase;
        this.registerUsernameUseCase = registerUsernameUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getUsernameSuggestionUseCase.unsubscribe();
        registerUsernameUseCase.unsubscribe();
    }

    @Override
    public void getUsernameSuggestion() {
        getView().showLoading();
        getUsernameSuggestionUseCase.execute(new GetUsernameSuggestionSubscriber(getView()));
    }

    @Override
    public void registerUsername(String username) {
        getView().showLoading();
        registerUsernameUseCase.execute(
                RegisterUsernameUseCase.createRequestParams(username),
                new RegisterUsernameSubscriber(getView())
        );
    }
}
