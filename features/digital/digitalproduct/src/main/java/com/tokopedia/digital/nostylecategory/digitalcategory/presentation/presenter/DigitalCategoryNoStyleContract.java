package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;


/**
 * Created by Rizky on 30/08/18.
 */
public class DigitalCategoryNoStyleContract {

    public interface View extends CustomerView {

        Resources getResources();

        void renderCategory(DigitalCategoryModel digitalCategoryModel);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getCategory(int categoryId);

    }

}
