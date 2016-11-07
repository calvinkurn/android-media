
package com.tokopedia.tkpd.selling.orderReject.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.expandablelayout.ExpandableLayoutListener;
import com.tkpd.library.ui.expandablelayout.ExpandableRelativeLayout;
import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.SnackbarRetry;
import com.tokopedia.tkpd.selling.SellingService;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderProduct;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.tkpd.selling.orderReject.ConfirmRejectOrderActivity;
import com.tokopedia.tkpd.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.tkpd.selling.orderReject.adapter.SimpleDividerItemDecoration;
import com.tokopedia.tkpd.selling.orderReject.model.DataResponseReject;
import com.tokopedia.tkpd.selling.orderReject.model.ModelRejectOrder;
import com.tokopedia.tkpd.selling.presenter.listener.SellingView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Erry on 6/3/2016.
 */
public class ShopClosedReasonFragment extends Fragment implements SellingView {

    @Bind(R2.id.reason)
    TextView reasonText;
    @Bind(R2.id.list)
    RecyclerView recyclerView;
    @Bind(R2.id.start_date)
    TextView startDate;
    @Bind(R2.id.end_date)
    TextView endDate;
    @Bind(R2.id.note)
    EditText noteTxt;
    @Bind(R2.id.confirm_button)
    TextView confirmButton;
    @Bind(R2.id.arrow_display)
    ImageView arrowDisplay;
    @Bind(R2.id.add_stock_empty)
    ExpandableRelativeLayout expandableRelativeLayout;
    @Bind(R2.id.set_stock_empty)
    LinearLayout setStockEmpty;
    @Bind(R2.id.pBar)
    ProgressBar progressBar;

    private ProductListAdapter listAdapter;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    OrderShippingList orderShippingList;
    List<OrderProduct> orderProducts = new ArrayList<>();
    String orderId;
    TkpdProgressDialog progressDialog;
    private int day;
    private int month;
    private int year;
    private Bundle bundle;

    public static ShopClosedReasonFragment newInstance(OrderShippingList orderShippingList, String reason, String orderId) {

        Bundle args = new Bundle();
        args.putString(ConfirmRejectOrderActivity.ORDER_ID, orderId);
        args.putString(ConfirmRejectOrderActivity.REASON, reason);
        args.putParcelable(ConfirmRejectOrderActivity.ORDERS, Parcels.wrap(orderShippingList));
        ShopClosedReasonFragment fragment = new ShopClosedReasonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

//    @OnClick(R2.id.start_date) void startDatePicker(){
//        new DatePickerUtil(getActivity()).DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
//            @Override
//            public void onDateSelected(int year, int month, int dayOfMonth) {
//                calendar.set(year, month, dayOfMonth);
//                startDate.setText(dateFormat.format(calendar.getTime()));
//            }
//        });
//    }

    @OnClick(R2.id.end_date)
    void endDatePicker() {
        calculateDate(endDate.getText().toString());
        DatePickerUtil datePickerUtil = new DatePickerUtil(getActivity());
        datePickerUtil.SetDate(day, month, year);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, 1);
        datePickerUtil.setMinDate(calendar.getTimeInMillis());
        datePickerUtil.DatePickerSpinnerShopClose(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                endDate.setText(dateFormat.format(calendar.getTime()));
            }
        });
    }

    @OnClick(R2.id.confirm_button)
    void onConfirm() {
        if (validateForm()) {
            ModelRejectOrder modelRejectOrder = new ModelRejectOrder();
            modelRejectOrder.setAction_type("reject");
            modelRejectOrder.setReason_code("4"); // 4 is shop close
            modelRejectOrder.setList_product_id(listAdapter.getStockEmptyList());
            modelRejectOrder.setOrder_id(orderId);
            modelRejectOrder.setClose_end(endDate.getText().toString());
            modelRejectOrder.setClosed_note(noteTxt.getText().toString());
            bundle = new Bundle();
            bundle.putParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_CLOSE_SHOP, bundle);
        }
    }

    private boolean validateForm() {
        if (noteTxt.getText().toString().isEmpty()) {
            noteTxt.setError(getString(R.string.note_should_not_empty));
            return false;
        } else {
            if(noteTxt.getText().toString().length() >100){
                noteTxt.setError(getString(R.string.note_should_less_100));
                return false;
            }else{
                noteTxt.setError(null);
                return true;
            }
        }
    }

    @OnClick(R2.id.set_stock_empty)
    void onClickStockEmpty() {
        expandableRelativeLayout.toggle();
        if (expandableRelativeLayout.isExpanded())
            arrowDisplay.setImageResource(R.drawable.chevron_down);
        else
            arrowDisplay.setImageResource(R.drawable.chevron_up);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_reject_shop_closed, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderId = getArguments().getString(ConfirmRejectOrderActivity.ORDER_ID);
        orderShippingList = Parcels.unwrap(getArguments().getParcelable(ConfirmRejectOrderActivity.ORDERS));
        orderProducts = orderShippingList.getOrderProducts();
        listAdapter = new ProductListAdapter(getActivity(), ProductListAdapter.Type.stock, orderProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        startDate.setEnabled(false);
        reasonText.setText(getArguments().getString(ConfirmRejectOrderActivity.REASON));
        startDate.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        endDate.setText(dateFormat.format(calendar.getTime()));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getProductDetail();
    }

    private void getProductDetail() {
        bundle = new Bundle();
        bundle.putParcelable(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY, Parcels.wrap(orderProducts));
        ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.GET_PRODUCT_FORM_EDIT_CLOSED, bundle);
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case SellingService.REJECT_ORDER_CLOSE_SHOP:
                DataResponseReject result = Parcels.unwrap(data.getParcelable(DataResponseReject.MODEL_DATA_REJECT_RESPONSE_KEY));
                if (result.getIsSuccess() == 1) {
                    progressDialog.dismiss();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                break;
            case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
                progressBar.setVisibility(View.GONE);
                expandableRelativeLayout.setVisibility(View.VISIBLE);
                orderProducts.clear();
                List<OrderProduct> orderProductList = Parcels.unwrap(data.getParcelable(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY));
                orderProducts.addAll(orderProductList);
                listAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        progressDialog.dismiss();
        String error = (String) data[0];
        switch (type) {
            case SellingService.REJECT_ORDER_CLOSE_SHOP:
                NetworkErrorHelper.showDialogCustomMSG(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_CLOSE_SHOP, bundle);
                        }
                    }
                }, error);
                break;
            case SellingService.GET_PRODUCT_FORM_EDIT_CLOSED:
                progressBar.setVisibility(View.GONE);
                SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.get_product_detail_failed), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.GET_PRODUCT_FORM_EDIT_CLOSED, bundle);
                        }
                    }
                });
                snackbarRetry.showRetrySnackbar();
                break;
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        progressDialog.dismiss();
        String error = (String) data[0];
        if (error != null) {
            showDialogError(getActivity(), error);
        }
    }

    private void calculateDate(String date) {
        String[] dates = date.split("/");
        day = Integer.parseInt(dates[0]);
        month = Integer.parseInt(dates[1]);
        year = Integer.parseInt(dates[2]);
    }

    private void showDialogError(Context context, String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        msg.setText(error);
        dialog.setView(promptsView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        expandableRelativeLayout.setVisibility(View.GONE);
    }
}
