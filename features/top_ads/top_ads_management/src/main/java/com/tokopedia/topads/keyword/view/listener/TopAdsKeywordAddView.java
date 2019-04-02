package com.tokopedia.topads.keyword.view.listener;


import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by normansyahputa on 5/22/17.
 */

public interface TopAdsKeywordAddView extends CustomerView {
    void onSuccessSaveKeyword();
    void onFailedSaveKeyword(Throwable e);
}
