package com.tokopedia.product.manage.list.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.product.manage.list.view.listener.ProductDraftListCountView;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListCountPresenter extends BaseDaggerPresenter<ProductDraftListCountView> {
    public abstract void fetchAllDraftCountWithUpdateUploading();
    public abstract void fetchAllDraftCount();
    public abstract void clearAllDraft();
}
