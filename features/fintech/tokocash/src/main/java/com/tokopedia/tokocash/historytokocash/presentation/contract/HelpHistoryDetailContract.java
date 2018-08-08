package com.tokopedia.tokocash.historytokocash.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;

import java.util.List;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public interface HelpHistoryDetailContract {

    interface View extends CustomerView {
        void loadHelpHistoryData(List<HelpHistoryTokoCash> helpHistoryTokoCashes);

        void successSubmitHelpHistory();

        void showErrorHelpHistory(Throwable throwable);

        void showProgressLoading();

        void hideProgressLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHelpCategoryHistory();

        void submitHelpHistory(String subject, String message, String category, String transactionId);

        void destroyView();
    }
}
