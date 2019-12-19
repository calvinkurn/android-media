package com.tokopedia.opportunity.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.opportunity.R;
import com.tokopedia.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.opportunity.di.component.DaggerOpportunityComponent;
import com.tokopedia.opportunity.di.component.OpportunityComponent;
import com.tokopedia.opportunity.di.module.OpportunityModule;
import com.tokopedia.opportunity.listener.OpportunityView;
import com.tokopedia.opportunity.presentation.ActionViewData;
import com.tokopedia.opportunity.presenter.OpportunityPresenter;
import com.tokopedia.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.BaseWebViewFragment;

import javax.inject.Inject;

public class OpportunityTncFragment extends BaseWebViewFragment implements OpportunityView {
    public static final String ACCEPTED_OPPORTUNITY = "ACCEPTED_OPPORTUNITY";
    private OpportunityItemViewModel opportunityItemViewModel;

    private OnOpportunityFragmentListener listener;

    TkpdProgressDialog progressDialog;
    private View btnTakeOpportunity;

    private OpportunityComponent opportunityComponent;

    @Inject
    OpportunityPresenter presenter;

    public interface OnOpportunityFragmentListener{
        OpportunityItemViewModel getItemViewModel();
    }

    public static OpportunityTncFragment newInstance() {
        return new OpportunityTncFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.opportunityItemViewModel = listener.getItemViewModel();

        opportunityComponent.inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        btnTakeOpportunity = view.findViewById(R.id.button_take_opportunity);
        btnTakeOpportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionConfirmClicked();
            }
        });
        btnTakeOpportunity.setVisibility(View.GONE);
        return view;
    }

    @Override
    public int setProgressBar() {
        return R.id.progressbar_opportunity;
    }

    @Override
    public int setWebView() {
        return R.id.webview_opportunity;
    }

    @Override
    protected void onLoadFinished() {
        super.onLoadFinished();
        if (btnTakeOpportunity!= null) {
            btnTakeOpportunity.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter == null)
            return;

        presenter.unsubscribeObservable();
        presenter.detachView();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_opportunity_tnc;
    }

    @Override
    protected String getUrl() {
        if(opportunityItemViewModel == null)
            return null;
        return opportunityItemViewModel.getReplacementTnc();
    }

    @Override
    public void onActionDeleteClicked() {
        // no delete here
    }

    public void onActionConfirmClicked() {
        UnifyTracking.eventOpportunity(
                getActivity(),
                OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_TAKE_YES,
                OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                AppEventTracking.Action.CLICK,
                OpportunityTrackingEventLabel.EventLabel.YES
        );
        presenter.acceptOpportunity();
    }

    @Override
    public void onReputationLabelClicked() {
        // have no label here
    }

    @Override
    public void onActionSeeDetailProduct(String productId) {
        // have no detail here
    }

    @Override
    public String getOpportunityId() {
        return String.valueOf(opportunityItemViewModel.getOrderReplacementId());
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void onSuccessTakeOpportunity(ActionViewData actionViewData) {
        finishLoadingProgress();
        CommonUtils.UniversalToast(getActivity(), actionViewData.getMessage());
        Intent intent = new Intent();
        intent.putExtra(ACCEPTED_OPPORTUNITY, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onSuccessNewPrice(OpportunityNewPriceData opportunityNewPriceData) {
        finishLoadingProgress();
    }

    private void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onErrorTakeOpportunity(String errorMessage) {
        finishLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorPriceInfo(String errorMessage) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        this.listener = (OnOpportunityFragmentListener) context;

        if(context != null && context instanceof BaseActivity){
            opportunityComponent = DaggerOpportunityComponent
                    .builder()
                    .opportunityModule(new OpportunityModule())
                    .appComponent(((BaseActivity)context).getApplicationComponent())
                    .build();
        }
    }
}
