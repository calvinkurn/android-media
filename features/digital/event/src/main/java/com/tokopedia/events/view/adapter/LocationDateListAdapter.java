package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.viewmodel.LocationDateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class LocationDateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocationDateModel> dataSet;
    private Context mContext;
    private EventBookTicketContract.BookTicketPresenter mPresenter;

    public LocationDateListAdapter(List<LocationDateModel> data, Context context, EventBookTicketContract.BookTicketPresenter presenter) {
        dataSet = data;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.locationdate_item_layout, parent, false);
        return new LocationDateHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((LocationDateHolder) holder).setLocationDate(position, dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(List<LocationDateModel> data) {
        dataSet = data;
    }

    public class LocationDateHolder extends RecyclerView.ViewHolder {

        TextView tvLocation;
        TextView tvDayTime;
        View locationDateItem;

        LocationDateModel valueItem;
        int mPosition;

        private LocationDateHolder(View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tv_location_bts);
            tvDayTime = itemView.findViewById(R.id.tv_day_time);
            locationDateItem = itemView.findViewById(R.id.location_date_item);
            locationDateItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onClickLocationDate(valueItem, mPosition);
                    notifyItemChanged(mPosition);
                }
            });
        }

        private void setLocationDate(int position, LocationDateModel value) {
            this.valueItem = value;
            this.mPosition = position;
            tvLocation.setText(valueItem.getmLocation());
            if (value.getDate() != null && value.getDate().length() > 1)
                tvDayTime.setText(valueItem.getDate());
            else
                tvDayTime.setVisibility(View.GONE);
        }
    }
}
