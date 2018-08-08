package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordAddView;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 5/22/17.
 */

public abstract class TopAdsKeywordAddPresenter extends BaseDaggerPresenter<TopAdsKeywordAddView> {
    abstract public void addKeyword (String groupId, int keywordType, ArrayList<String> keywordList);
}
