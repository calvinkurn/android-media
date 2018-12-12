package com.tokopedia.core.tracking.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.tracking.adapter.TrackingHistoryAdapter;
import com.tokopedia.core.tracking.listener.TrackingFragmentView;
import com.tokopedia.core.tracking.model.tracking.TrackingResponse;
import com.tokopedia.core.tracking.presenter.TrackingFragmentPresenter;
import com.tokopedia.core.tracking.presenter.TrackingFragmentPresenterImpl;
import com.tokopedia.core.util.MethodChecker;

import butterknife.BindView;

/**
 * @author Alifa on 10/12/2016.
 */

public class TrackingFragment extends BasePresenterFragment<TrackingFragmentPresenter> implements
        TrackingFragmentView {

    @BindView(R2.id.mainView)
    View mainView;

    @BindView(R2.id.loadingStatus)
    ProgressBar loadingStatus;

    //HEADER
    @BindView(R2.id.tracking_status)
    TextView sendingStatus;
    @BindView(R2.id.view_status)
    View statusView;

    //VIEW1
    @BindView(R2.id.ref_num)
    TextView refNumber;
    @BindView(R2.id.receiver_name_non_jne)
    TextView receiverName;
    @BindView(R2.id.date)
    TextView sendingDate;
    @BindView(R2.id.tracking_code)
    TextView serviceCode;
    @BindView(R2.id.view_receiver_non_jne)
    View viewReceiver;
    @BindView(R2.id.view_receiver_jne)
    View viewReceiverJNE;

    //VIEW DETAIL
    @BindView(R2.id.view_footer_tracking)
    View footerTracking;
    @BindView(R2.id.view_detail_shipping)
    View detailShipping;
    @BindView(R2.id.sender_name)
    TextView sellerName;
    @BindView(R2.id.sender_city)
    TextView sellerCity;
    @BindView(R2.id.receiver_name)
    TextView buyerName;
    @BindView(R2.id.receiver_city)
    TextView buyerCity;
    @BindView(R2.id.tracking_result)
    RecyclerView addressRV;


    TrackingHistoryAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    public static final int ORDER_SHIPPING = 500;
    public static final int ORDER_DELIVERED = 600;
    public static final int ORDER_SHIPPING_REF_NUM_EDITED = 505;
    public static final int ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY = 501;
    public static final int ORDER_DELIVERED_DUE_DATE = 699;
    public static final int ORDER_INVALID = 520;

    public static TrackingFragment createInstance(String orderId) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle bundle = new Bundle();
        Log.d("alifa", "createInstance: " + orderId);
        bundle.putString("order_id", orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
        Log.d("alifa", "onFirstTimeLaunched: " + getArguments().getString("order_id"));
        presenter.loadTrackingData(getArguments().getString("order_id"));
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
        presenter = new TrackingFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_tracking;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        adapter = TrackingHistoryAdapter.createInstance(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addressRV.setLayoutManager(linearLayoutManager);
        addressRV.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        if (loadingStatus != null)
            loadingStatus.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }


    @Override
    public TrackingHistoryAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        mainView.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(MethodChecker.fromHtml(getString(R.string.error_520_tracking)));
        //TODO after WS Fixed their error message notification
/*        if (errorMessage.isEmpty())
            builder.setMessage(MethodChecker.fromHtml(getString(R.string.error_520_tracking)));
        else builder.setMessage(errorMessage);*/
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void removeError() {

    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {

    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    @Override
    public void refresh() {

    }


    @Override
    public void showEmptyState() {

    }

    @Override
    public void setRetry() {

    }

    @Override
    public void showEmptyState(String error) {

    }

    @Override
    public void setRetry(String error) {

    }

    @Override
    public void updateHeaderView(TrackingResponse response) {
        Log.d("alifa", "updateHeaderView: ");
        this.mainView.setVisibility(View.VISIBLE);
        int paddingDefault = statusView.getPaddingTop();
        switch (response.getTrackOrder().getOrderStatus()) {
            case ORDER_DELIVERED:
                statusView.setBackgroundResource(R.drawable.tkpd_address_card);
                sendingStatus.setText(getString(R.string.order_delivered));
                break;
            case ORDER_SHIPPING:
                statusView.setBackgroundResource(R.drawable.bg_yellow_border_yelow);
                sendingStatus.setText(getString(R.string.order_onprocess));
                break;
            case ORDER_SHIPPING_REF_NUM_EDITED:
                statusView.setBackgroundResource(R.drawable.bg_yellow_border_yelow);
                sendingStatus.setText(getString(R.string.order_shipping_refnum_edited));
                break;
            case ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY:
                statusView.setBackgroundResource(R.drawable.bg_yellow_border_yelow);
                sendingStatus.setText(getString(R.string.order_waiting_status));
                break;
            case ORDER_DELIVERED_DUE_DATE:
                statusView.setBackgroundResource(R.drawable.tkpd_address_card);
                sendingStatus.setText(getString(R.string.order_delivered_due_date));
                break;
            case ORDER_INVALID:
                mainView.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(MethodChecker.fromHtml(getString(R.string.error_520_tracking)));
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        getActivity().finish();
                    }
                });
                break;
            default:
                statusView.setBackgroundResource(R.drawable.bg_yellow_border_yelow);
                sendingStatus.setText(R.string.order_delivered_on_process);
                break;
        }
        statusView.setPadding(paddingDefault, paddingDefault, paddingDefault, paddingDefault);
    }

    @Override
    public void renderTrackingData(TrackingResponse response) {
        refNumber.setText(response.getTrackOrder().getShippingRefNum());
        if (response.getTrackOrder().getDetail() == null) {
            detailShipping.setVisibility(View.GONE);
            viewReceiver.setVisibility(View.GONE);
            viewReceiverJNE.setVisibility(View.GONE);
        } else {
            detailShipping.setVisibility(View.VISIBLE);
            viewReceiver.setVisibility(View.VISIBLE);
            viewReceiverJNE.setVisibility(View.VISIBLE);

            sendingDate.setText(response.getTrackOrder().getDetail().getSendDate());
            receiverName.setText(response.getTrackOrder().getDetail().getReceiverName());
            serviceCode.setText(response.getTrackOrder().getDetail().getServiceCode());

            buyerCity.setText(response.getTrackOrder().getDetail().getReceiverCity());
            buyerName.setText(response.getTrackOrder().getDetail().getReceiverName());
            sellerCity.setText(response.getTrackOrder().getDetail().getShipperCity());
            sellerName.setText(response.getTrackOrder().getDetail().getShipperName());
        }
        if (response.getTrackOrder().getTrackHistory().isEmpty()) {
            footerTracking.setVisibility(View.GONE);
        }
        adapter.getList().clear();
        adapter.showEmpty(false);
        Log.d("alifa", "renderTrackingData: ");
        adapter.addList(response.getTrackOrder().getTrackHistory());
        loadingStatus.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

}
