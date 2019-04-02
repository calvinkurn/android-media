package com.tokopedia.gm.featured.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.featured.view.listener.GMFeaturedProductView;

/**
 * Created by normansyahputa on 9/7/17.
 */

public abstract class GMFeaturedProductPresenter extends BaseDaggerPresenter<GMFeaturedProductView> {
    public abstract void loadData();

    public abstract void postData(RequestParams requestParams);
}
