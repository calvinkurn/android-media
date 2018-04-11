package com.tokopedia.gm.statistic.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.view.listener.GMStatisticTransactionTableView;

import java.util.Date;

/**
 * Created by normansyahputa on 7/13/17.
 */

public abstract class GMStatisticTransactionTablePresenter extends BaseDaggerPresenter<GMStatisticTransactionTableView> {
    public abstract void loadData(Date startDate, Date endDate,
                                  @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy,
                                  int page);
}
