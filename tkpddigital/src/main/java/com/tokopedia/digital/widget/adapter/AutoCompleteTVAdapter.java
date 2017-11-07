package com.tokopedia.digital.widget.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.compoundview.WidgetClientNumberView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 9/26/2017.
 */

public class AutoCompleteTVAdapter extends ArrayAdapter<OrderClientNumber> implements Filterable {
    private Context context;
    private int resource;
    private List<OrderClientNumber> orderClientNumbers;
    private List<OrderClientNumber> allOrderClientNumbers;
    private List<OrderClientNumber> filteredOrderClientNumbers;

    public AutoCompleteTVAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<OrderClientNumber> orderClientNumbers) {
        super(context, resource, orderClientNumbers);
        this.context = context;
        this.resource = resource;
        this.orderClientNumbers = orderClientNumbers;
        allOrderClientNumbers = orderClientNumbers;
        filteredOrderClientNumbers = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        OrderClientNumber orderClientNumber = orderClientNumbers.get(position);
        holder.bind(orderClientNumber);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return orderClientNumberFilter;
    }

    Filter orderClientNumberFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filteredOrderClientNumbers.clear();
                for (OrderClientNumber orderClientNumber : allOrderClientNumbers) {
                    if (orderClientNumber.getClientNumber().contains(constraint)) {
                        filteredOrderClientNumbers.add(orderClientNumber);
                    }
                }
                filterResults.values = filteredOrderClientNumbers;
                filterResults.count = filteredOrderClientNumbers.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderClientNumbers = (List<OrderClientNumber>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    @Override
    public int getCount() {
        if (orderClientNumbers != null) {
            return orderClientNumbers.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public OrderClientNumber getItem(int position) {
        return orderClientNumbers.get(position);
    }

    private class ItemHolder {
        private TextView number;
        private TextView name;

        ItemHolder(View itemView) {
            number = (TextView) itemView.findViewById(R.id.text_name);
            name = (TextView) itemView.findViewById(R.id.text_number);
        }

        public void bind(OrderClientNumber orderClientNumber) {
            number.setText(orderClientNumber.getClientNumber());
            name.setText(orderClientNumber.getName());
        }
    }

}
