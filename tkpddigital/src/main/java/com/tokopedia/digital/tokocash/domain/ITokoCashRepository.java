package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.entity.TokoCashCashBackModel;
import com.tokopedia.digital.tokocash.model.CashBackData;

import rx.Observable;

/**
 * Created by kris on 6/16/17. Tokopedia
 */

public interface ITokoCashRepository {

    public Observable<CashBackData> getTokoCashPending();

}
