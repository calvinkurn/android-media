package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.data.model.data.DataCredit;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsNewPromoFragmentListener {

    void onGroupNameListLoaded(@NonNull List<DataCredit> creditList);

    void onLoadGroupNameListError();
}
