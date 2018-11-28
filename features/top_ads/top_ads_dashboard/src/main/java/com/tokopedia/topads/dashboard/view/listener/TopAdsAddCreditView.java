package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.dashboard.data.model.DataCredit;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsAddCreditView extends CustomerView {

    void onCreditListLoaded(@NonNull List<DataCredit> creditList);

    void onLoadCreditListError();
}
