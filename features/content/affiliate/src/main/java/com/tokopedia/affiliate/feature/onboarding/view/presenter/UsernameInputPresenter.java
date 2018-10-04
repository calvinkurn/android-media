package com.tokopedia.affiliate.feature.onboarding.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.GetUsernameSuggestionUseCase;
import com.tokopedia.affiliate.feature.onboarding.view.contract.UsernameInputContract;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/4/18.
 */
public class UsernameInputPresenter extends BaseDaggerPresenter<UsernameInputContract.View>
        implements UsernameInputContract.Presenter {

    private GetUsernameSuggestionUseCase getUsernameSuggestionUseCase;

    @Inject
    public UsernameInputPresenter(GetUsernameSuggestionUseCase getUsernameSuggestionUseCase) {
        this.getUsernameSuggestionUseCase = getUsernameSuggestionUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        getUsernameSuggestionUseCase.unsubscribe();
    }

    @Override
    public void getUsernameSuggestion() {

    }
}
