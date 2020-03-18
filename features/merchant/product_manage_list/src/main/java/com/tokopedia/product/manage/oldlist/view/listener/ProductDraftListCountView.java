package com.tokopedia.product.manage.oldlist.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftListCountView extends CustomerView {
    void onDraftCountLoaded(long rowCount);
    void onDraftCountLoadError();
}
