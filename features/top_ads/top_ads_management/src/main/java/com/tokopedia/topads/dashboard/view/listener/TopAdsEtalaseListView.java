package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;

/**
 * Created by hendry on 2/17/17.
 */
public interface TopAdsEtalaseListView extends CustomerView {

    void onLoadSuccess(@NonNull List<Etalase> etalaseList);

    void onLoadSuccessEtalaseEmpty();

    void onLoadConnectionError();

    void onLoadError(String message);

    void showLoad (boolean isShow);
}
