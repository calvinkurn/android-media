package com.tokopedia.product.addedit.imagepicker.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface ImagePickerCatalogContract {
    interface View extends BaseListViewListener<CatalogModelView>{

    }

    interface Presenter extends CustomerPresenter<View>{

        void getCatalogImage(String catalogId);

        void clearCacheCatalog();
    }
}
