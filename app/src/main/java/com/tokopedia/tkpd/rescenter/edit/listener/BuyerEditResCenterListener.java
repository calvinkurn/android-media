package com.tokopedia.tkpd.rescenter.edit.listener;

import android.content.Context;

import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.tkpd.rescenter.edit.customview.EditCategorySectionView;
import com.tokopedia.tkpd.rescenter.edit.customview.EditPackageStatusView;
import com.tokopedia.tkpd.rescenter.edit.customview.EditProductTroubleView;
import com.tokopedia.tkpd.rescenter.edit.customview.EditTroubleSectionView;
import com.tokopedia.tkpd.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.tkpd.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.tkpd.rescenter.edit.presenter.BuyerEditResCenterPresenter;

/**
 * Created on 8/24/16.
 */
public interface BuyerEditResCenterListener {

    EditPackageStatusView getEditPackageStatusView();

    EditCategorySectionView getEditCategorySectionView();

    EditProductTroubleView getEditProductTroubleView();

    void setLoading(boolean visible);

    void setMainView(boolean visible);

    void setTimeOutView(NetworkErrorHelper.RetryClickedListener rcListener);

    void setErrorView(String message);

    void renderPackageReceivedFormView();

    void renderCategoryTroubleView(EditResCenterFormData formData);

    void renderProductTroubleListView(EditResCenterFormData formData);

    void renderTroubleView(EditResCenterFormData formData);

    BuyerEditResCenterPresenter getPresenter();

    void setCategoryTroubleViewVisibility(boolean visible);

    void setProductTroubleListViewVisibility(boolean visible);

    void setTroubleViewVisibility(boolean visible);

    void setSolutionViewVisibility(boolean visible);

    String getResolutionID();

    Context getBaseContext();

    DetailResCenterData getDetailData();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

    EditTroubleSectionView getEditTroubleView();

    void setSnackBar(String string);

    void openSolutionFragment();

    void openProductDetailTroubleFragment();

    void renderShop(EditResCenterFormData formData);

    void renderInvoice(EditResCenterFormData formData);

}
