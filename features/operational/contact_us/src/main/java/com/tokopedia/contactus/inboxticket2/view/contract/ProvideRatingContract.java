package com.tokopedia.contactus.inboxticket2.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface ProvideRatingContract {
    public interface ProvideRatingView extends CustomerView {
        public void setFirstOption(String option);
        public void setSecondOption(String option);
        public void setThirdOption(String option);
        public void setFourthOption(String option);

    }
    public interface ProvideRatingPresenter extends CustomerPresenter<ProvideRatingView> {

    }
}
