package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public interface HelpHistoryListener {

    void loadHelpHistoryData(List<HelpHistoryTokoCash> helpHistoryTokoCashes);

    void successSubmitHelpHistory();

    void showErrorHelpHistory(String errorMessage);

    void showProgressLoading();

    void hideProgressLoading();
}
