package com.tokopedia.tkpd.purchase.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.customView.OrderStatusView;
import com.tokopedia.tkpd.product.activity.ProductInfoActivity;
import com.tokopedia.tkpd.product.model.passdata.ProductPass;
import com.tokopedia.tkpd.purchase.adapter.TxProductListAdapter;
import com.tokopedia.tkpd.purchase.listener.TxDetailViewListener;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderData;
import com.tokopedia.tkpd.purchase.presenter.TxDetailPresenter;
import com.tokopedia.tkpd.purchase.presenter.TxDetailPresenterImpl;
import com.tokopedia.tkpd.purchase.view.NestedListView;

import java.text.MessageFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TxDetailActivity
 * Created by Angga.Prasetiyo on 28/04/2016.
 */
public class TxDetailActivity extends BasePresenterActivity<TxDetailPresenter> implements
        TxDetailViewListener, TxProductListAdapter.ActionListener {

    private static final String EXTRA_ORDER_DATA = "EXTRA_ORDER_DATA";

    @Bind(R.id.invoice_text)
    TextView tvInvoiceNumber;
    @Bind(R.id.shop_name)
    TextView tvShopName;
    @Bind(R.id.product_list)
    NestedListView lvProduct;
    @Bind(R.id.shipping_cost)
    TextView tvShippingCost;
    @Bind(R.id.additional_cost)
    TextView tvAdditionalCost;
    @Bind(R.id.destination)
    TextView tvDestination;
    @Bind(R.id.destination_detail)
    TextView tvDestinationDetail;
    @Bind(R.id.quantity)
    TextView tvQuantity;
    @Bind(R.id.grand_total)
    TextView tvGrandTotal;
    @Bind(R.id.transaction)
    TextView tvTransactionDate;
    @Bind(R.id.see_all)
    TextView btnShowMoreHistory;
    @Bind(R.id.receive_btn)
    TextView btnReceiveOrder;
    @Bind(R.id.reject_btn)
    TextView btnRejectOrder;
    @Bind(R.id.ask_seller)
    TextView btnAskSeller;
    @Bind(R.id.track_btn)
    TextView btnTrackOrder;
    @Bind(R.id.complain_but)
    TextView btnComplainOrder;
    @Bind(R.id.upload_proof)
    TextView btnUploadProof;
    @Bind(R.id.sender_name)
    TextView tvSenderName;
    @Bind(R.id.sender_phone)
    TextView tvSenderPhone;
    @Bind(R.id.sender_form)
    View holderFormSender;
    @Bind(R.id.order_status_layout)
    LinearLayout holderOrderStatus;

    private OrderData orderData;
    private TkpdProgressDialog progressDialog;
    private TxProductListAdapter productListAdapter;

    public static Intent createInstance(Context context, OrderData orderData) {
        Intent intent = new Intent(context, TxDetailActivity.class);
        intent.putExtra(EXTRA_ORDER_DATA, orderData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.orderData = extras.getParcelable(EXTRA_ORDER_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxDetailPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_people_tx_details;
    }

    @Override
    protected void initView() {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        lvProduct.setAdapter(productListAdapter);
    }

    @Override
    protected void initVar() {
        this.productListAdapter = new TxProductListAdapter(this, this);
    }

    @Override
    protected void setActionVar() {
        renderActionButton();
        renderOrderStatus();
        renderDetailInfo();
        renderProductList();
    }

    private void renderProductList() {
        productListAdapter.addAll(orderData.getOrderProducts());
        productListAdapter.notifyDataSetChanged();
    }

    private void renderOrderStatus() {
        holderOrderStatus.removeAllViews();
        int length = orderData.getOrderHistory().size();
        btnShowMoreHistory.setVisibility(View.GONE);
        if (length > 2) {
            btnShowMoreHistory.setVisibility(View.VISIBLE);
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            alterHistoryComment(orderData.getOrderHistory().get(i).getHistoryComments(), i);
            OrderStatusView orderStatusView = new OrderStatusView(this,
                    orderData.getOrderHistory().get(i));
            holderOrderStatus.addView(orderStatusView.getView());
        }
    }

    private void renderDetailInfo() {
        if (orderData.getOrderDetail().getDetailDropshipName() != null
                && !orderData.getOrderDetail().getDetailDropshipName().equals("0")
                && orderData.getOrderDetail().getDetailDropshipTelp() != null
                && !orderData.getOrderDetail().getDetailDropshipTelp().equals("0")) {
            tvSenderName.setText(orderData.getOrderDetail().getDetailDropshipName());
            tvSenderPhone.setText(orderData.getOrderDetail().getDetailDropshipTelp());
            holderFormSender.setVisibility(View.VISIBLE);
        } else {
            holderFormSender.setVisibility(View.GONE);
        }
        tvInvoiceNumber.setText(orderData.getOrderDetail().getDetailInvoice());
        tvShopName.setText(Html.fromHtml(orderData.getOrderShop().getShopName()));
        tvAdditionalCost.setText(orderData.getOrderDetail().getDetailTotalAddFeeIdr());
        tvShippingCost.setText(orderData.getOrderDetail().getDetailShippingPriceIdr());
        tvQuantity.setText(MessageFormat.format("{0} Barang",
                orderData.getOrderDetail().getDetailQuantity()));
        tvGrandTotal.setText(orderData.getOrderDetail().getDetailOpenAmountIdr());
        tvTransactionDate.setText(orderData.getOrderDetail().getDetailOrderDate());
        tvDestination.setText(MessageFormat.format("{0} {1}",
                Html.fromHtml(orderData.getOrderShipment().getShipmentName() + " -"),
                orderData.getOrderShipment().getShipmentProduct()));
        tvDestinationDetail.setText(orderData.getOrderDestination().getDetailDestination()
                .replace("&amp;", "&"));
    }

    private void renderActionButton() {
        String statusOrder = String.valueOf(orderData.getOrderDetail().getDetailOrderStatus());
        btnReceiveOrder.setVisibility(statusOrder.equals(getString(R.string.ORDER_DELIVERED))
                || statusOrder.equals(getString(R.string.ORDER_DELIVERY_AUTOMATIC))
                || statusOrder.equals(getString(R.string.ORDER_DELIVERY_FAILURE))
                || statusOrder.equals(getString(R.string.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_REF_NUM_EDITED))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_TRACKER_INVALID))
                ? View.VISIBLE : View.GONE);

        btnTrackOrder.setVisibility(statusOrder.equals(getString(R.string.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_REF_NUM_EDITED))
                || statusOrder.equals(getString(R.string.ORDER_SHIPPING_TRACKER_INVALID))
                ? View.VISIBLE : View.GONE);

        btnRejectOrder.setVisibility(orderData.getOrderButton().getButtonOpenDispute() != null &&
                orderData.getOrderButton().getButtonOpenDispute().equals("1")
                ? View.VISIBLE : View.GONE);

        btnComplainOrder.setVisibility(orderData.getOrderButton().getButtonResCenterGoTo() != null &&
                orderData.getOrderButton().getButtonResCenterGoTo().equals("1")
                ? View.VISIBLE : View.GONE);

        btnUploadProof.setVisibility(View.GONE);

        btnAskSeller.setVisibility(orderData.getOrderButton().getButtonAskSeller() != null &&
                orderData.getOrderButton().getButtonAskSeller().equals("1")
                ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void closeView() {
        this.finish();
    }

    @OnClick(R.id.invoice_text)
    void actionInvoice() {
        presenter.processInvoice(this, orderData);
    }

    @OnClick(R.id.shop_name)
    void actionToShop() {
        presenter.processToShop(this, orderData.getOrderShop());
    }

    @OnClick(R.id.complain_but)
    void actionShowComplain() {
        presenter.processShowComplain(this, orderData.getOrderButton());
    }

    @OnClick(R.id.ask_seller)
    void actionAskSeller() {
        presenter.processAskSeller(this, orderData);
    }

    @OnClick(R.id.reject_btn)
    void actionOpenDispute() {
        presenter.processOpenDispute(this, orderData, 0);
    }

    @OnClick(R.id.receive_btn)
    void actionConfirmDeliver() {
        presenter.processConfirmDeliver(this, orderData);
        UnifyTracking.eventReceivedShipping();
    }

    @OnClick(R.id.track_btn)
    void actionTracking() {
        presenter.processTrackOrder(this, orderData);
        UnifyTracking.eventTrackOrder();
    }

    @OnClick(R.id.upload_proof)
    void actionUploadProof() {
        presenter.processUploadProof(this, orderData);
    }


    @OnClick(R.id.see_all)
    void actionSeeAllHistories() {
        presenter.processSeeAllHistories(this, orderData);
    }

    @Override
    public void closeWithResult(int requestCode, Intent data) {
        this.setResult(requestCode, data);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void actionToProductInfo(ProductPass productPass) {
        navigateToActivity(ProductInfoActivity.createInstance(this, productPass));
    }

    private void alterHistoryComment(String comment, int historyIndex) {
        if(orderData.getOrderDetail().getDetailPreorder() !=null
                && orderData.getOrderDetail().getDetailPreorder().getPreorderStatus() == 1
                && comment.equals("0") && Integer.parseInt(orderData.getOrderHistory().get(historyIndex).getHistoryOrderStatus()) == 400){
            orderData.getOrderHistory().get(historyIndex)
                    .setHistoryComments("Lama waktu proses produk : "
                            + orderData.getOrderDetail().getDetailPreorder().getPreorderProcessTime()
                            + " "
                            + orderData.getOrderDetail().getDetailPreorder().getPreorderProcessTimeTypeString()) ;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(this, requestCode, resultCode, data);
    }
}
