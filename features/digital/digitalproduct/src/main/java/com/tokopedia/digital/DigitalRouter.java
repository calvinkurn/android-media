package com.tokopedia.digital;

import android.content.Context;

import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.digital.tokocash.model.CashBackData;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public interface DigitalRouter extends IDigitalModuleRouter {

    Observable<CashBackData> getPendingCashbackUseCase(Context context);
}
