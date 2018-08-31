package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by Rizky on 30/08/18.
 */
public class AgentDigitalCategoryContract {

    public interface View extends CustomerView {

        Resources getResources();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getCategory(int categoryId);

    }

}
