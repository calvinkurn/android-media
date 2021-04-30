package com.tokopedia.digital.product.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class NumberListAdapter extends RecyclerView.Adapter<NumberListAdapter.ItemHolder> {

    private List<OrderClientNumber> clientNumbers;

    private OnClientNumberClickListener callback;

    public interface OnClientNumberClickListener {
        void onClientNumberClicked(OrderClientNumber orderClientNumber);
    }

    public NumberListAdapter(OnClientNumberClickListener callback, List<OrderClientNumber> clientNumbers) {
        this.callback = callback;
        this.clientNumbers = clientNumbers;
    }

    @Override
    public NumberListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_digital_item_autocomplete, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberListAdapter.ItemHolder holder, int position) {
        holder.bind(clientNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        if (clientNumbers == null) return 0;
        else return clientNumbers.size();
    }

    public void setNumbers(List<OrderClientNumber> clientNumbers) {
        this.clientNumbers = clientNumbers;
    }

    public List<OrderClientNumber> getClientNumbers() {
        return clientNumbers;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView number;
        private TextView name;

        private OrderClientNumber orderClientNumber;

        ItemHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.text_name);
            name = itemView.findViewById(R.id.text_number);
            itemView.setOnClickListener(this);
        }

        public void bind(OrderClientNumber orderClientNumber) {
            this.orderClientNumber = orderClientNumber;
            number.setText(orderClientNumber.getClientNumber());
            if (orderClientNumber.getName() != null) {
                name.setText(orderClientNumber.getName());
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            callback.onClientNumberClicked(orderClientNumber);
        }
    }

}
