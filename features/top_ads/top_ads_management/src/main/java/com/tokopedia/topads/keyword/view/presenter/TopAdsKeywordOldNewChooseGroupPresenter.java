package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView;

/**
 * Created by zulfikarrahman on 5/23/17.
 */

public abstract class TopAdsKeywordOldNewChooseGroupPresenter<T extends TopAdsKeywordNewChooseGroupView> extends BaseDaggerPresenter<T> {
    public abstract void searchGroupName(String keyword);
}