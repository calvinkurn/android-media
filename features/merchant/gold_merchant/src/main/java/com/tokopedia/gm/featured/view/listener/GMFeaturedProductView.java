package com.tokopedia.gm.featured.view.listener;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;

/**
 * Created by normansyahputa on 9/7/17.
 */

public interface GMFeaturedProductView extends BaseListViewListener<GMFeaturedProductModel> {

    void onSubmitSuccess();

    void onSubmitError(Throwable t);
}
