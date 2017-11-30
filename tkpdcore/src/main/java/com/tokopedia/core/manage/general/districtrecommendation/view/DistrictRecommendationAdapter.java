package com.tokopedia.core.manage.general.districtrecommendation.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class DistrictRecommendationAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_ADDRESS = 100;
    private ArrayList<Address> addresses;
    private Listener listener;

    public DistrictRecommendationAdapter(ArrayList<Address> addresses, Listener listener) {
        this.addresses = addresses;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_ADDRESS:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_district_recomendation, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_ADDRESS:
                bindAddress((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindAddress(final ViewHolder holder, final int position) {
        Address address = addresses.get(position);

        String completeAddress = address.getProvinceName() +
                ", " +
                address.getCityName() +
                ", " +
                address.getDistrictName();

        holder.tvAddress.setText(completeAddress);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (addresses.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_ADDRESS;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == addresses.size();
    }

    @Override
    public int getItemCount() {
        return addresses.size() + super.getItemCount();
    }

    public interface Listener {
        void onItemClick(Address address);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.tvAddress)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() >= 0 && addresses.size() > getAdapterPosition()) {
                listener.onItemClick(addresses.get(getAdapterPosition()));
            }
        }
    }

}
