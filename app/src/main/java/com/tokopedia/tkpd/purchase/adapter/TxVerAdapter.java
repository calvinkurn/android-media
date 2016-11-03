package com.tokopedia.tkpd.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.purchase.model.response.txverification.TxVerData;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TxVerAdapter
 * Created by Angga.Prasetiyo on 25/05/2016.
 */
public class TxVerAdapter extends ArrayAdapter<TxVerData> {

    private final LayoutInflater inflater;
    private final Context context;
    private final ActionListener actionListener;

    public TxVerAdapter(Context context, ActionListener actionListener) {
        super(context, R.layout.listview_payment_verfication, new ArrayList<TxVerData>());
        this.context = context;
        this.actionListener = actionListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface ActionListener {

        void actionEditPayment(TxVerData data);

        void actionUploadProof(TxVerData data);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_payment_verfication, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TxVerData item = getItem(position);
        holder.tvPaymentDate.setText(item.getPaymentDate());
        holder.btnOverflow.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                showPopUp(view, item);
            }
        });


        holder.tvSysAccountNumber.setText(item.getSystemAccountNo());
        holder.tvSysAccountBankName.setText(item.getBankName());
        holder.tvPaymentRefNumber.setText(item.getPaymentRefNum());
        holder.tvPayementAmount.setText(item.getPaymentAmount());
        holder.tvUserAccountName.setText(item.getUserAccountName());
        holder.tvUserAccountBankName.setText(item.getUserBankName());

        switch (getTypePaymentMethod(item)) {
            case 1:
                renderKlikBCAHolder(item, holder);
                break;
            case 2:
                renderUnchangeableHolder(item, holder);
                break;
            case 3:
                renderNormalHolder(holder);
                break;
        }
        return convertView;
    }

    private void showPopUp(View view, final TxVerData data) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        int menuId = data.getButton().getButtonUploadProof() == 0
                ? R.menu.menu_edit_payment
                : R.menu.menu_edit_payment_upload;
        inflater.inflate(menuId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        actionListener.actionEditPayment(data);
                        return true;
                    case R.id.action_upload:
                        actionListener.actionUploadProof(data);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


    private void renderNormalHolder(ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.VISIBLE);
        holder.holderUnchangeablePayment.setVisibility(View.GONE);
        holder.btnOverflow.setVisibility(View.VISIBLE);
    }

    private void renderUnchangeableHolder(TxVerData item, ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.GONE);
        holder.btnOverflow.setVisibility(View.GONE);
        holder.holderUnchangeablePayment.setVisibility(View.VISIBLE);
        holder.tvSpecialPaymentMethod.setText(MessageFormat.format("Kode {0} : {1}",
                item.getBankName(), item.getUserAccountName()));
    }

    private void renderKlikBCAHolder(TxVerData item, ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.GONE);
        holder.btnOverflow.setVisibility(View.GONE);
        holder.holderUnchangeablePayment.setVisibility(View.VISIBLE);
        holder.tvSpecialPaymentMethod.setText(item.getBankName());
    }

    private int getTypePaymentMethod(TxVerData item) {
        return item.getBankName().contains("Klik") && item.getBankName().contains("BCA") ? 1
                : item.getButton().getButtonEditPayment() == 0 &&
                item.getButton().getButtonUploadProof() == 0 &&
                item.getButton().getButtonViewProof() == 0 ? 2 : 3;
    }

    class ViewHolder {
        @Bind(R.id.date)
        TextView tvPaymentDate;
        @Bind(R.id.user_account)
        TextView tvUserAccountName;
        @Bind(R.id.system_account)
        TextView tvSysAccountNumber;
        @Bind(R.id.total_invoice)
        TextView tvPayementAmount;
        @Bind(R.id.payment_ref)
        TextView tvPaymentRefNumber;
        @Bind(R.id.user_bank_name)
        TextView tvUserAccountBankName;
        @Bind(R.id.system_bank_name)
        TextView tvSysAccountBankName;
        @Bind(R.id.payment_method_name)
        TextView tvSpecialPaymentMethod;
        @Bind(R.id.but_overflow)
        View btnOverflow;
        @Bind(R.id.normal_payment_info)
        LinearLayout holderNormalPayment;
        @Bind(R.id.unchangeable_payment_info)
        LinearLayout holderUnchangeablePayment;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
