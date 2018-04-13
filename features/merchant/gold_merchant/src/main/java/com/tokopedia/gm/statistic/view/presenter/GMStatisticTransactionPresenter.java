package com.tokopedia.gm.statistic.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.statistic.view.listener.GMStatisticTransactionView;

/**
 * Created by normansyahputa on 7/18/17.
 */

public abstract class GMStatisticTransactionPresenter extends BaseDaggerPresenter<GMStatisticTransactionView> {

    public abstract void loadDataWithDate(GMModuleRouter gmModuleRouter, long startDate, long endDate);

}