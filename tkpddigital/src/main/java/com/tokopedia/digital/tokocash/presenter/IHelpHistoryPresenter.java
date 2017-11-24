package com.tokopedia.digital.tokocash.presenter;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public interface IHelpHistoryPresenter {

    void getHelpCategoryHistory();

    void submitHelpHistory(String subject, String message, String category, String transactionId);
}
