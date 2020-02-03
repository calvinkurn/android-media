package com.tokopedia.events.view.contractor;

import android.widget.EditText;

import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class EventReviewTicketsContractor {

    public interface EventReviewTicketsView extends EventBaseContract.EventBaseView {
        void renderFromPackageVM(PackageViewModel packageViewModel, SelectedSeatViewModel selectedSeats, int customText1);

        void setEmailID(String emailID);

        void setPhoneNumber(String number);

        void initForms(String[] hintText, String[] regex);

        void showPromoSuccessMessage(String text, int color);

        void hideSuccessMessage();

        void showEmailTooltip();

        void showMoreinfoTooltip();

        void hideTooltip();

        boolean validateAllFields();
    }

    public interface EventReviewTicketPresenter extends EventBaseContract.EventBasePresenter {

        void proceedToPayment();

        void updatePromoCode(String code);

        boolean validateEditText(EditText view);

        void updateEmail(String email);

        void updateNumber(String number);

        void clickEmailIcon();

        void clickMoreinfoIcon();

        void clickDismissTooltip();

        void clickGoToPromo();

        String getSCREEN_NAME();

        void getProfile();
    }
}
