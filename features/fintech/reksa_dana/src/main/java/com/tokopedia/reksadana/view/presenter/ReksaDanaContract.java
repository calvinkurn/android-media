package com.tokopedia.reksadana.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.reksadana.view.data.initdata.FieldData;
import com.tokopedia.reksadana.view.data.signimageurl.ImageDetails;
import com.tokopedia.reksadana.view.data.submit.UserDetails;

public interface ReksaDanaContract {
    interface Presenter extends CustomerPresenter<View> {
        void fetchData();
        void submitData(ImageDetails imageDetails);
        void getSignImageUrl(ImageDetails imageDetails);
    }

    interface View extends CustomerView {
        Context getAppContext();

        void disableProgressVisibility();

        void setProgressVisility();

        UserDetails getRegistrationData(String publicUrl);

        void setProgressText(String text);

        void saveSignature();

        void setEducation(FieldData educationData);

        void setIncome(FieldData incomeData);

        void setIncomeSource(FieldData incomeSourceData);

        void setInvestment(FieldData investmentData);

        void setOccupation(FieldData occupationData);

        String getFileLoc();

        String getEmail();

        void onRegistrationComplete();
    }
}
