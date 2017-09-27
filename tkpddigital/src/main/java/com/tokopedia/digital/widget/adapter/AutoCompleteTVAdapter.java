package com.tokopedia.digital.widget.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.ClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 9/26/2017.
 */

public class AutoCompleteTVAdapter extends ArrayAdapter<ClientNumber> {

    private Context context;
    private int resource;
    private List<ClientNumber> clientNumbers;

    public AutoCompleteTVAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ClientNumber> clientNumbers) {
        super(context, resource, clientNumbers);
        this.context = context;
        this.resource = resource;
        this.clientNumbers = clientNumbers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemHolder holder;

        if (convertView == null) {
            convertView  = LayoutInflater.from(context).inflate(resource, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        ClientNumber clientNumber = clientNumbers.get(position);
        holder.bind(clientNumber);

        return convertView;
    }

    private class ItemHolder {
        private TextView number;
        private TextView name;

        ItemHolder(View itemView) {
            number = (TextView) itemView.findViewById(R.id.text_name);
            name = (TextView) itemView.findViewById(R.id.text_number);
        }

        public void bind(ClientNumber clientNumber) {
            number.setText(clientNumber.getText());
            name.setText(clientNumber.getName());
        }
    }

}
