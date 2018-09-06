package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.mitra.digitalcategory.presentation.model.DigitalCategoryModel;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryContract {

    public interface View extends CustomerView {

        Resources getResources();

        void renderWidgetView(DigitalCategoryModel digitalCategoryModel, String defaultId);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getCategory(int categoryId);

    }

}
