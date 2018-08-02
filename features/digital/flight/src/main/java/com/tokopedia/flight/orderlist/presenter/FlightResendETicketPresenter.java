package com.tokopedia.flight.orderlist.presenter;

import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.orderlist.contract.FlightResendETicketContract;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.FlightSendEmailUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 08/02/18.
 */

public class FlightResendETicketPresenter extends BaseDaggerPresenter<FlightResendETicketContract.View>
        implements FlightResendETicketContract.Presenter {

    private FlightSendEmailUseCase flightSendEmailUseCase;

    @Inject
    public FlightResendETicketPresenter(FlightSendEmailUseCase flightSendEmailUseCase) {
        this.flightSendEmailUseCase = flightSendEmailUseCase;
    }

    @Override
    public void onSendButtonClicked() {
        if (validateInput(getView().getEmail())) {
            sendETicket();
        }
    }

    private boolean validateInput(String email) {
        boolean isValid = true;

        if (email == null || email.isEmpty()) {
            isValid = false;
            getView().showEmailEmptyError(R.string.flight_resend_eticket_dialog_email_empty_error);
        } else if (!isValidEmail(email)) {
            isValid = false;
            getView().showEmailInvalidError(R.string.flight_resend_eticket_dialog_email_invalid_error);
        } else if (!isEmailWithoutProhibitSymbol(email)) {
            isValid = false;
            getView().showEmailInvalidSymbolError(R.string.flight_resend_eticket_dialog_email_invalid_symbol_error);
        }

        return isValid;
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                !contactEmail.contains(".@") &&
                !contactEmail.contains("@.");
    }

    private boolean isEmailWithoutProhibitSymbol(String contactEmail) {
        return !contactEmail.contains("+");
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSendEmailUseCase.unsubscribe();
    }

    private void sendETicket() {
        flightSendEmailUseCase.execute(
                flightSendEmailUseCase.createRequestParams(
                        getView().getInvoiceId(),
                        getView().getUserId(),
                        getView().getEmail()
                ),
                new Subscriber<SendEmailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (throwable instanceof FlightException) {
                            getView().onResendETicketError(
                                    FlightErrorUtil.getMessageFromException(
                                            getView().getActivity(),
                                            throwable
                                    )
                            );
                        } else {
                            getView().onResendETicketError(throwable.getMessage());
                        }
                    }

                    @Override
                    public void onNext(SendEmailEntity sendEmailEntity) {
                        getView().onResendETicketSuccess();
                    }
                });
    }
}
