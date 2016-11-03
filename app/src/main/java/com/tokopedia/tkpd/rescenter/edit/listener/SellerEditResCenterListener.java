package com.tokopedia.tkpd.rescenter.edit.listener;

import android.content.Context;

import com.tokopedia.tkpd.database.model.AttachmentResCenterDB;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.tkpd.rescenter.edit.customview.EditAttachmentSellerView;
import com.tokopedia.tkpd.rescenter.edit.customview.EditSolutionSellerView;
import com.tokopedia.tkpd.rescenter.edit.customview.EditSummaryResCenterView;
import com.tokopedia.tkpd.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.tkpd.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.tkpd.rescenter.edit.presenter.SellerEditResCenterPresenter;

import java.util.List;

/**
 * Created on 8/29/16.
 */
public interface SellerEditResCenterListener {

    SellerEditResCenterPresenter getPresenter();

    String getResolutionID();

    Context getBaseContext();

    DetailResCenterData getDetailData();

    ActionParameterPassData getPassData();

    void setPassData(ActionParameterPassData passData);

    void setLoading(boolean visible);

    void setMainView(boolean visible);

    void setTimeOutView(NetworkErrorHelper.RetryClickedListener rcListener);

    void setErrorView(String message);

    void renderShop(EditResCenterFormData form);

    void renderInvoice(EditResCenterFormData form);

    void renderSolution(EditResCenterFormData formData);

    void renderSummary(EditResCenterFormData formData);

    void showErrorMessage(String string);

    EditSummaryResCenterView getSummaryView();

    EditSolutionSellerView getEditSolutionSellerView();

    EditAttachmentSellerView getAttachmenSectionView();

    void showLoading(boolean visible);

    void showMainView(boolean visible);

    void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener);

    void finish();

    void setActivityResult();

    List<AttachmentResCenterDB> getAttachmentData();
}
