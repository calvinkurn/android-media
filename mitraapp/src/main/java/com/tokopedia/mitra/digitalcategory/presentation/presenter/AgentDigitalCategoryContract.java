package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.common_digital.product.presentation.model.RenderOperatorModel;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;

import java.util.List;

/**
 * Created by Rizky on 30/08/18.
 */
public class AgentDigitalCategoryContract {

    public interface View extends CustomerView {

        Resources getResources();

        void renderWidgetView(RenderOperatorModel renderOperatorModel);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getCategory(int categoryId);

    }

}
