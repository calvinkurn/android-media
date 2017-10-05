package com.tokopedia.digital.product.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class NumberListAdapter extends RecyclerView.Adapter<NumberListAdapter.ItemHolder> {

    private List<OrderClientNumber> clientNumbers;

    private OnClientNumberClickListener callback;

    public interface OnClientNumberClickListener {
        void onClientNumberClicked(String number);
    }

    public NumberListAdapter(OnClientNumberClickListener callback, List<OrderClientNumber> clientNumbers) {
        this.callback = callback;
        this.clientNumbers = clientNumbers;
    }

    @Override
    public NumberListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_autocomplete, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberListAdapter.ItemHolder holder, int position) {
        holder.bind(clientNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return clientNumbers.size();
    }

    public void setNumbers(List<OrderClientNumber> clientNumbers) {
        this.clientNumbers = clientNumbers;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView number;
        private TextView name;

        private String clientNumber;

        ItemHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.text_name);
            name = (TextView) itemView.findViewById(R.id.text_number);
            itemView.setOnClickListener(this);
        }

        public void bind(OrderClientNumber orderClientNumber) {
            this.clientNumber = orderClientNumber.getClientNumber();
            number.setText(orderClientNumber.getClientNumber());
            name.setText(orderClientNumber.getName());
        }

        @Override
        public void onClick(View v) {
            callback.onClientNumberClicked(clientNumber);
        }
    }

}
