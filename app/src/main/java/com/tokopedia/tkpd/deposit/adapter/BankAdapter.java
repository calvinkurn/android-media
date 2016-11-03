package com.tokopedia.tkpd.deposit.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deposit.model.BankAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 4/13/16.
 */
public class BankAdapter extends ArrayAdapter {

    public static class ViewHolder {
        TextView bankName;
    }

    private ArrayList<BankAccount> list;
    private LayoutInflater inflater;
    private Context context;


    public BankAdapter(Context context, int multiline_spinner, ArrayList<BankAccount> list) {
        super(context, multiline_spinner, list);
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static BankAdapter createInstance(Context context) {
        return new BankAdapter(context, R.layout.spinner_item, new ArrayList<BankAccount>());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.spinner_item, null);

            holder.bankName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        String text;
        if (position == 0) {
            text = context.getString(R.string.title_choose_bank);
        } else if (position == list.size() + 1) {
            text = context.getString(R.string.title_add_bank_account);
        } else {
            text = Html.fromHtml(list.get(position - 1).getBankName() + "<br/>"
                    + " " + list.get(position - 1).getBankAccountNumber() + "<br/>"
                    + " a/n " + list.get(position - 1).getBankAccountName()).toString();
        }


        holder.bankName.setText(text);

        return convertView;
    }

    @Override
    public int getCount() {
        return 2 + getList().size();
    }

    public ArrayList<BankAccount> getList() {
        return list;
    }

    public void setList(List<BankAccount> bankAccount) {
        this.list.clear();
        this.list.addAll(bankAccount);
        notifyDataSetChanged();
    }
}
