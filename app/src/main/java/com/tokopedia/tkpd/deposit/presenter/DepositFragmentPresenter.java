package com.tokopedia.tkpd.deposit.presenter;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.tkpd.customadapter.RetryDataBinder;
import com.tokopedia.tkpd.util.RefreshHandler;

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
