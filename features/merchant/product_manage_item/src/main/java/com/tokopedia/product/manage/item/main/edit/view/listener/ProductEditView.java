package com.tokopedia.product.manage.item.view.presenter;

import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel;
import com.tokopedia.product.manage.item.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.product.manage.item.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.view.model.categoryrecomm.ProductCategoryPredictionViewModel;
import com.tokopedia.product.manage.item.view.model.scoringproduct.DataScoringProductView;

import java.util.List;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface ProductEditView extends ProductAddView {

    void onErrorFetchEditProduct(Throwable throwable);

    void onSuccessLoadProduct(ProductViewModel model);
}
