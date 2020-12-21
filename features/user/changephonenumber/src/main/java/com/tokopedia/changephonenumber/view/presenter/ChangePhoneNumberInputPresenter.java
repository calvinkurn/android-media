package com.tokopedia.changephonenumber.view.presenter;

import android.text.Editable;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.changephonenumber.view.subscriber.SubmitNumberSubscriber;
import com.tokopedia.changephonenumber.view.subscriber.ValidateNumberSubscriber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase
        .getSubmitNumberParam;
import static com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase
        .getValidateNumberParam;


/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberInputFragmentListener.View>
        implements ChangePhoneNumberInputFragmentListener.Presenter {
    private static final int MINIMUM_NUMBER_LENGTH = 8;
    private static final int MAXIMUM_NUMBER_LENGTH = 15;
    private static final String REGEX_CLEAN_PHONE_NUMBER = "(?!^)\\+|[^+0-9\\n]+";

    private final ValidateNumberUseCase validateNumberUseCase;
    private ChangePhoneNumberInputFragmentListener.View view;

    @Inject
    public ChangePhoneNumberInputPresenter(ValidateNumberUseCase validateNumberUseCase) {
        this.validateNumberUseCase = validateNumberUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void onNewNumberTextChanged(String phoneNumber, int selection) {
        final Pattern pattern = Pattern.compile(REGEX_CLEAN_PHONE_NUMBER);
        String newNumber = phoneNumber;
        final Matcher matcher = pattern.matcher(newNumber);
        newNumber = matcher.replaceAll("");

        if (isNumberLengthValid(newNumber)) {
            view.enableNextButton();
        } else {
            view.disableNextButton();
        }

        if (phoneNumber.length() != newNumber.length()) {
            int lengthDifference = newNumber.length() - phoneNumber.length();
            if (selection + lengthDifference < 0)
                view.correctPhoneNumber(newNumber, 0);
            else if (selection > newNumber.length())
                view.correctPhoneNumber(newNumber, newNumber.length());
            else
                view.correctPhoneNumber(newNumber, selection + lengthDifference);

        }
    }

    private boolean isNumberLengthValid(String newNumber) {
        newNumber = newNumber.replace("+", "");
        return (newNumber.length() >= MINIMUM_NUMBER_LENGTH && newNumber.length() <=
                MAXIMUM_NUMBER_LENGTH);
    }

    @Override
    public void validateNumber(String newPhoneNumber) {
        view.showLoading();
        validateNumberUseCase.execute(getValidateNumberParam(newPhoneNumber),
                new ValidateNumberSubscriber(view));
    }

    @Override
    public void submitNumber(String newPhoneNumber) {
        view.showLoading();
        validateNumberUseCase.execute(getSubmitNumberParam(newPhoneNumber),
                new SubmitNumberSubscriber(view));
    }

    @Override
    public void detachView() {
        super.detachView();
        validateNumberUseCase.unsubscribe();
    }
}