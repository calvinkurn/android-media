package com.tokopedia.topupbills.telco.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.topupbills.R;
import com.tokopedia.topupbills.telco.view.model.DigitalOrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class NumberListAdapter extends RecyclerView.Adapter<NumberListAdapter.ItemHolder> {

    private List<DigitalOrderClientNumber> clientNumbers;

    private OnClientNumberClickListener callback;

    public interface OnClientNumberClickListener {
        void onClientNumberClicked(DigitalOrderClientNumber orderClientNumber);
    }

    public NumberListAdapter(OnClientNumberClickListener callback, List<DigitalOrderClientNumber> clientNumbers) {
        this.callback = callback;
        this.clientNumbers = clientNumbers;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_autocomplete_fav_num, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(clientNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return clientNumbers.size();
    }

    public void setNumbers(List<DigitalOrderClientNumber> clientNumbers) {
        this.clientNumbers = clientNumbers;
    }

    public List<DigitalOrderClientNumber> getClientNumbers() {
        return clientNumbers;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView number;
        private TextView name;

        private DigitalOrderClientNumber orderClientNumber;

        ItemHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.text_name);
            name = (TextView) itemView.findViewById(R.id.text_number);
            itemView.setOnClickListener(this);
        }

        public void bind(DigitalOrderClientNumber orderClientNumber) {
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
