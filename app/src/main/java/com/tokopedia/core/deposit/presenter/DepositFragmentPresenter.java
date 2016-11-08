package com.tokopedia.core.deposit.presenter;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.customadapter.RetryDataBinder;

/**
 * Created by Nisie on 3/30/16.
 */
public interface DepositFragmentPresenter {
    void onDrawClicked();

    void onSearchClicked();

    void onEndDateClicked(DatePickerUtil datePicker);

    void onStartDateClicked(DatePickerUtil datePicker);

    void getSummaryDeposit();

    void onRefresh();

    void setFirstDateParameter();

    void setCache();

    void loadMore(int lastItemPosition, int visibleItem);

    RetryDataBinder.OnRetryListener onRetry();

    void onDestroyView();
}
