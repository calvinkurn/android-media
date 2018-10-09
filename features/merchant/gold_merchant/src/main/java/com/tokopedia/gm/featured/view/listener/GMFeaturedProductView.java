package com.tokopedia.gm.featured.view.listener;

import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;

/**
 * Created by normansyahputa on 9/7/17.
 */

public interface GMFeaturedProductView extends BaseListViewListener<GMFeaturedProductModel> {

    void onSubmitSuccess();

    void onSubmitError(Throwable t);
}
