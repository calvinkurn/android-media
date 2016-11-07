package com.tokopedia.tkpd.purchase.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.purchase.listener.TxConfDetailViewListener;
import com.tokopedia.tkpd.purchase.model.response.txconfirmation.TxConfData;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderData;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderProduct;
import com.tokopedia.tkpd.purchase.presenter.TxConfDetailPresenter;
import com.tokopedia.tkpd.purchase.presenter.TxConfDetailPresenterImpl;
import com.tokopedia.tkpd.util.TokopediaBankAccount;

import java.text.MessageFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by Angga.Prasetiyo on 15/06/2016.
 */
public class TxConfirmationDetailActivity extends BasePresenterActivity<TxConfDetailPresenter>
        implements TxConfDetailViewListener {
    private static final String EXTRA_TX_CONFIRMATION_DATA = "EXTRA_TX_CONFIRMATION_DATA";

    public static final int REQUEST_CONFIRMATION = 1;

    @Bind(R2.id.total_tx)
    TextView tvTotalTx;
    @Bind(R2.id.tx_date)
    TextView tvDateTx;
    @Bind(R2.id.due_date)
    TextView tvDueDateTx;
    @Bind(R2.id.total_item)
    TextView tvTotalItem;
    @Bind(R2.id.total_item_price)
    TextView tvTotalItemPrice;
    @Bind(R2.id.deposit_used)
    TextView tvDepositUsed;
    @Bind(R2.id.lv_cart)
    LinearLayout lvContainer;
    @Bind(R2.id.check_account)
    View btnSysAccountInfo;
    @Bind(R2.id.cancel_button)
    View btnCancel;
    @Bind(R2.id.confirm_button)
    View btnConfirm;

    private TxConfData txConfData;
    private TkpdProgressDialog mProgressDialog;

    public static Intent newInstance(Context context, TxConfData data) {
        Intent intent = new Intent(context, TxConfirmationDetailActivity.class);
        intent.putExtra(EXTRA_TX_CONFIRMATION_DATA, data);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.txConfData = extras.getParcelable(EXTRA_TX_CONFIRMATION_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxConfDetailPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_payment_confirmation_detail;
    }

    @Override
    protected void initView() {
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        tvTotalItem.setText(MessageFormat.format("{0} items ({1}kg)",
                txConfData.getConfirmation().getTotalItem(),
                txConfData.getConfirmation().getTotalWeight()));
        tvTotalTx.setText(txConfData.getConfirmation().getLeftAmount());
        tvDateTx.setText(txConfData.getConfirmation().getCreateTime());
        tvDueDateTx.setText(txConfData.getConfirmation().getPayDueDate());
        tvTotalItemPrice.setText(txConfData.getConfirmation().getOpenAmount());
        tvDepositUsed.setText(txConfData.getConfirmation().getDepositAmount());
        renderCartInfo();
    }

    private void renderCartInfo() {
        LayoutInflater vi = LayoutInflater.from(this);
        for (OrderData data : txConfData.getOrderDataList()) {
            View view = vi.inflate(R.layout.listview_shop_cart_payment_conf, lvContainer, false);
            final HolderCartItem holder = new HolderCartItem(view);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnChosen.setText(data.getOrderDetail().getDetailPartialOrder().equals("0")
                    ? getString(R.string.title_cart_cancel_order)
                    : getString(R.string.title_cart_ship_left));
            holder.viewDetailInfo.setVisibility(View.GONE);
            holder.btnDetailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (holder.viewDetailInfo.getVisibility() == View.GONE) {
                        holder.viewDetailInfo.setVisibility(View.VISIBLE);
                        holder.ivChevron.setImageResource(R.drawable.ic_chevron_up);
                    } else {
                        holder.viewDetailInfo.setVisibility(View.GONE);
                        holder.ivChevron.setImageResource(R.drawable.ic_chevron_down);
                    }
                }

            });
            holder.tvShopName.setText(Html.fromHtml(data.getOrderShop().getShopName()));
            holder.tvTotalPrice.setText(data.getOrderDetail().getDetailOpenAmountIdr());
            holder.tvShippingAddress.setText(data.getOrderDestination().getReceiverName());
            holder.tvShippingAgency.setText(MessageFormat.format("{0} - {1}",
                    data.getOrderShipment().getShipmentName(),
                    data.getOrderShipment().getShipmentProduct()));
            holder.tvTotalWeight.setText(MessageFormat.format("{0} {1} ( {2} kg ) ",
                    data.getOrderDetail().getDetailQuantity(),
                    getString(R.string.title_item), data.getOrderDetail().getDetailTotalWeight()));
            holder.tvSubTotal.setText(data.getOrderDetail().getDetailProductPriceIdr());
            holder.tvShippingCost.setText(data.getOrderDetail().getDetailShippingPriceIdr());
            holder.tvInsurancePrice.setText(data.getOrderDetail().getDetailInsurancePriceIdr());
            holder.tvInsurance.setText(!data.getOrderDetail().getDetailInsurancePrice().equals("0")
                    || data.getOrderDetail().getDetailForceInsurance().equals("1")
                    ? getString(R.string.yes) : getString(R.string.No));
            renderProductCartInfo(holder.containerProduct, data.getOrderProducts());
            lvContainer.addView(view);
        }
    }

    private void renderProductCartInfo(LinearLayout container, List<OrderProduct> datas) {
        for (OrderProduct data : datas) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.listview_product_cart_payment_conf, container, false);
            final HolderProductCartItem holder = new HolderProductCartItem(view);
            ImageHandler.loadImageRounded2(this, holder.ivPic, data.getProductPicture());
            holder.tvName.setText(Html.fromHtml(data.getProductName()));
            holder.tvPrice.setText(data.getProductPrice());
            holder.tvWeight.setText(MessageFormat.format(" ( {0} kg ) ",
                    data.getProductWeight()));
            holder.tvPriceTotal.setText(data.getOrderSubtotalPriceIdr());
            holder.tvNotes.setText(Html.fromHtml(alterNotesData(data.getProductNotes())));
            holder.tvQty.setText(data.getProductQuantity());
            holder.tvNotes.setEnabled(false);
            container.addView(view);
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        mProgressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
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
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @OnClick(R2.id.check_account)
    void actionCheckAccount() {
        TokopediaBankAccount.createShowAccountDialog(this);
    }

    @OnClick(R2.id.cancel_button)
    void actionCancelTransaction() {
        presenter.processCancelTransaction(this, txConfData);
    }

    @OnClick(R2.id.confirm_button)
    void actionConfirmTransaction() {
        presenter.processConfirmTransaction(this, txConfData);
    }

    @Override
    public void setResultActivity(int resultCode, Bundle bundle) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONFIRMATION:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                } else if (resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED
                        && data.hasExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)) {
                    NetworkErrorHelper.showSnackbar(this,
                            data.getStringExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM));
                }
                break;
        }
    }

    public class HolderCartItem {
        @Bind(R2.id.listview_prod)
        LinearLayout containerProduct;
        @Bind(R2.id.shop_name)
        TextView tvShopName;
        @Bind(R2.id.total_price)
        TextView tvTotalPrice;
        @Bind(R2.id.shipping_address)
        TextView tvShippingAddress;
        @Bind(R2.id.shipping_agency)
        TextView tvShippingAgency;
        @Bind(R2.id.total_weight)
        TextView tvTotalWeight;
        @Bind(R2.id.sub_total)
        TextView tvSubTotal;
        @Bind(R2.id.shipping_cost)
        TextView tvShippingCost;
        @Bind(R2.id.insurance_price)
        TextView tvInsurancePrice;
        @Bind(R2.id.edit)
        ImageView btnEdit;
        @Bind(R2.id.delete)
        ImageView btnDelete;
        @Bind(R2.id.error1)
        TextView tvError1;
        @Bind(R2.id.error2)
        TextView tvError2;
        @Bind(R2.id.detail_info)
        View viewDetailInfo;
        @Bind(R2.id.detail_info_but)
        View btnDetailInfo;
        @Bind(R2.id.error_area)
        View viewError;
        @Bind(R2.id.main_view)
        View viewMain;
        @Bind(R2.id.insurance)
        TextView tvInsurance;
        @Bind(R2.id.remaining_stock)
        TextView btnChosen;
        @Bind(R2.id.chevron_sign)
        ImageView ivChevron;

        public HolderCartItem(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class HolderProductCartItem {
        @Bind(R2.id.img)
        ImageView ivPic;
        @Bind(R2.id.name)
        TextView tvName;
        @Bind(R2.id.price)
        TextView tvPrice;
        @Bind(R2.id.weight)
        TextView tvWeight;
        @Bind(R2.id.price_total)
        TextView tvPriceTotal;
        @Bind(R2.id.error_msg)
        TextView tvError;
        @Bind(R2.id.notes)
        TextView tvNotes;
        @Bind(R2.id.qty)
        TextView tvQty;

        public HolderProductCartItem(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private String alterNotesData(String notes) {
        if (notes.equals("0")) {
            return "-";
        } else return notes;
    }
}
