package com.tokopedia.core.inboxreputation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.adapter.InboxReputationDetailAdapter;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.HeaderReputationDataBinder;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.inboxreputation.listener.InboxReputationDetailFragmentView;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenter;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.TkpdState;

import butterknife.BindView;

/**
 * Created by Nisie on 1/25/16.
 */
public class
        InboxReputationDetailFragment extends
        BasePresenterFragment<InboxReputationDetailFragmentPresenter> implements
        InboxReputationDetailFragmentView, InboxReputationConstant {

    public interface DoActionReputationListener {
        void postReputation(Bundle param);

        void skipReview(Bundle param);

        void deleteResponse(Bundle param);

        void postReport(Bundle param);
    }

    @BindView(R2.id.product_list)
    RecyclerView listProduct;


    InboxReputationDetailFragmentPresenter presenter;
    private RefreshHandler refreshHandler;
    private InboxReputationDetailAdapter adapter;
    private TkpdProgressDialog progressDialog;
    private ShareReviewDialog dialog;
    private CallbackManager callbackManager;

    public InboxReputationDetailFragment() {
    }

    public static InboxReputationDetailFragment createInstance(Bundle data) {
        InboxReputationDetailFragment fragment = new InboxReputationDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_inbox_reputation_detail_2;
    }

    @Override
    protected void initView(View view) {
        adapter = InboxReputationDetailAdapter.createAdapter(getActivity(), presenter);
        adapter.setInboxReputation((InboxReputationItem) getArguments().getParcelable(BUNDLE_INBOX_REPUTATION));
        refreshHandler = new RefreshHandler(getActivity(), getView(), onRefresh());
        refreshHandler.setPullEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listProduct.setLayoutManager(linearLayoutManager);
        listProduct.setAdapter(adapter);
        dialog = new ShareReviewDialog(getActivity(), this);
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onFirstTimeLaunched() {
        if (adapter.getList().size() == 0) {
            showLoading();
        }
        presenter.initData();
    }

    @Override
    public void showLoading() {
        adapter.showLoading(true);

    }

    @Override
    public void removeLoading() {
        adapter.showLoading(false);

    }

    @Override
    public InboxReputationDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxReputationDetailFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW:
                    presenter.afterPostForm(bundle);
                    if(data.getStringExtra(context.getString(R.string.message)) != null){
                        SnackbarManager.make(getActivity(),data.getStringExtra(context.getString(R.string.message)), Snackbar.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showNoResult() {
        adapter.showEmpty(true);
    }

    @Override
    public void removeNoResult() {
        adapter.showEmpty(false);
    }

    @Override
    public RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshList(false);
            }
        };
    }

    @Override
    public void finishRefresh() {
        refreshHandler.finishRefresh();
    }

    @Override
    public void setPullEnabled(boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
    }

    @Override
    public void clearData() {
        adapter.clearList();
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    public void updateReview(int position, InboxReputationDetailItem singleReview) {
        adapter.getList().set(position, singleReview);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showDialogLoading() {
        if(progressDialog.isProgress())
            progressDialog.dismiss();
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public InboxReputationItem getInboxReputation() {
        return adapter.getInboxReputation();
    }

    @Override
    public void showError(String error) {
        CommonUtils.UniversalToast(context, error);
    }

    @Override
    public void onSuccessPostReputation(Bundle resultData) {
        UnifyTracking.eventReviewCompleteBuyer();
        String smiley = presenter.getSmileyString(resultData.getString(InboxReviewIntentService.EXTRA_SMILEY));

        if (adapter.getInboxReputation().getRevieweeScore().equals(HeaderReputationDataBinder.STATUS_BAD)
                || adapter.getInboxReputation().getRevieweeScore().equals(HeaderReputationDataBinder.STATUS_NEUTRAL)) {
            adapter.getInboxReputation().setIsReviewerScoreEdited(1);
        }
        adapter.getInboxReputation().setRevieweeScore(smiley);
        adapter.notifyDataSetChanged();
        setActionEnabled(true);
        dismissProgressDialog();

        presenter.refreshList(true);
        setActivityResult();
    }


    @Override
    public void onSuccessSkipReview(Bundle resultData) {
        String reputationId = resultData.getString(InboxReviewIntentService.EXTRA_REPUTATION_ID, "");
        int productPosition = resultData.getInt(InboxReviewIntentService.EXTRA_PRODUCT_POSITION, 0);

        presenter.updateCacheSkippedReview(reputationId, productPosition);
        adapter.getList().get(productPosition).setIsSkipped(1);
        adapter.notifyDataSetChanged();
        setActionEnabled(true);
        dismissProgressDialog();
        setActivityResult();

    }

    @Override
    public void onSuccessDeleteResponse(Bundle resultData) {
        String reputationId = resultData.getString(InboxReviewIntentService.EXTRA_REPUTATION_ID, "");
        int productPosition = resultData.getInt(InboxReviewIntentService.EXTRA_PRODUCT_POSITION, 0);

        presenter.updateCacheDeletedResponse(reputationId, productPosition);
        adapter.getList().get(productPosition).getReviewResponse().setResponseMessage("0");
        adapter.getList().get(productPosition).getReviewResponse().setResponseTime("0");
        adapter.getList().get(productPosition).getReviewResponse().setIsResponseRead(0);
        adapter.notifyDataSetChanged();
        setActionEnabled(true);
        dismissProgressDialog();
        setActivityResult();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onSuccessPostReport(Bundle resultData) {
        SnackbarManager.make(getActivity(), context.getString(R.string.toast_success_report),
                Snackbar.LENGTH_LONG).show();
        setActionEnabled(true);
        dismissProgressDialog();
        setActivityResult();
    }

    @Override
    public void setActivityResult() {
        Intent intent = new Intent();
        intent.putExtra(InboxReputationFragment.IS_SUCCESS, 1);
        intent.putExtra("action", InboxReputationFragment.ACTION_UPDATE_REPUTATION);
        intent.putExtra(InboxReputationFragment.PARAM_INVOICE_NUM, adapter.getInboxReputation().getInvoiceRefNum());
        intent.putExtra(InboxReputationFragment.PARAM_POSITION, getArguments().getInt(BUNDLE_POSITION, 0));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void showRefresh() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void setActionEnabled(boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
        listProduct.setEnabled(isEnabled);
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        adapter.showLoading(false);
        dismissProgressDialog();
    }

    @Override
    public void removeError() {
        adapter.showEmpty(false);
    }

    @Override
    public void showSnackbar(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener).showRetrySnackbar();
    }

    @Override
    public void showSnackbar(String error, NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, listener).showRetrySnackbar();
    }

    @Override
    public void onFailedPostReputation(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if(errorMessage.equals("")){
                NetworkErrorHelper.showSnackbar(getActivity());
            }else{
                SnackbarManager.make(getActivity(),errorMessage,Snackbar.LENGTH_LONG).show();
            }
        }
        setActionEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onFailedSkipReview(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if(errorMessage.equals("")){
                NetworkErrorHelper.showSnackbar(getActivity());
            }else{
                SnackbarManager.make(getActivity(),errorMessage,Snackbar.LENGTH_LONG).show();
            }
        }
        setActionEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onFailedDeleteResponse(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if(errorMessage.equals("")){
                NetworkErrorHelper.showSnackbar(getActivity());
            }else{
                SnackbarManager.make(getActivity(),errorMessage,Snackbar.LENGTH_LONG).show();
            }
        }
        setActionEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onFailedPostReport(final Bundle resultData) {
        if (!resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "").equals("")) {
            String errorMessage = resultData.getString(InboxReviewIntentService.EXTRA_ERROR, "");
            if(errorMessage.equals("")){
                NetworkErrorHelper.showSnackbar(getActivity());
            }else{
                SnackbarManager.make(getActivity(),errorMessage,Snackbar.LENGTH_LONG).show();
            }
        }
        setActionEnabled(true);
        dismissProgressDialog();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    public void showShareProvider(InboxReputationDetailItem inboxReputationDetailItem) {
        KeyboardHandler.DropKeyboard(getActivity(),getView());
        dialog.setContent(inboxReputationDetailItem);
        dialog.show();
    }

}
