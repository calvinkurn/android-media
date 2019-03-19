package com.tokopedia.tokocash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.tokocash.historytokocash.presentation.model.PeriodRangeModelData;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

import java.util.List;

import okhttp3.Interceptor;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/5/17.
 */

public interface TokoCashRouter {

    Intent goToDatePicker(Activity activity, List<PeriodRangeModelData> periodRangeModelData, long startDate, long endDate,
                          int datePickerSelection, int datePickerType);

    String getRangeDateFormatted(Context context, long startDate, long endDate);

    WalletUserSession getTokoCashSession();

    Intent getWebviewActivityWithIntent(Context context, String url, String title);

    String getUserEmailProfil();

    Fragment getTopupTokoCashFragment();

    Interceptor getChuckInterceptor();

    Observable<PendingCashback> getPendingCashbackUseCase();

    Intent getLoginIntent();

    String getStringRemoteConfig(String key);

    long getLongConfig(String key);

    Intent getWebviewActivityWithIntent(Context context, String url);

    String getExtraBroadcastReceiverWallet();

    AnalyticTracker getAnalyticTracker();

    Observable<InfoQrTokoCash> getInfoQrTokoCashUseCase(RequestParams requestParams);

    Observable<BalanceTokoCash> getBalanceTokoCash();

    Intent getNominalActivityIntent(Context context, String qrCode, InfoQrTokoCash infoQrTokoCash);

    Intent getOvoActivityIntent(Context applicationContext);
}
