package com.tokopedia.tokocash.common.di;

import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.pendingcashback.domain.GetPendingCasbackUseCase;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
public interface TokoCashComponent {

    GetBalanceTokoCashUseCase getBalanceTokoCashUseCase();

    GetPendingCasbackUseCase getPendingCasbackUseCase();

    GetInfoQrTokoCashUseCase getInfoQrTokocashUseCase();
}
