package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;
import com.tokopedia.digital.tokocash.model.WithdrawSaldo;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface ITokoCashHistoryInteractor {

    void getHistoryTokoCash(Subscriber<TokoCashHistoryData> subscriber, String type,
                            String startDate, String endDate, int page);

    void getHelpListCategory(Subscriber<List<HelpHistoryTokoCash>> subscriber);

    void postHelpHistory(Subscriber<Boolean> subscriber, String subject, String message, String category, String transactionId);

    void postMoveToSaldo(Subscriber<WithdrawSaldo> subscriber, String url, ParamsActionHistory paramsActionHistory);
}
