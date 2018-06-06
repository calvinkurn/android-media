package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.digital.tokocash.entity.OAuthInfoEntity;
import com.tokopedia.digital.tokocash.entity.ResponseHelpHistoryEntity;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.entity.WithdrawSaldoEntity;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface IHistoryTokoCashRepository {

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                             String endDate, int page);

    Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData();

    Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId);

    Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory);

    Observable<OAuthInfoEntity> getOAuthInfo();

    Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType);
}
