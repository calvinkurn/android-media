package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;

import java.util.List;

/**
 * Created by Rizky on 06/09/18.
 */
public class DigitalOperatorChooserNoStyleContract {

    public interface View extends CustomerView {

        void renderOperators(List<RenderProductModel> renderProductModels);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getOperators(int categoryId);

    }

}
