package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InboxFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> dataSet;
    private Context mContext;
    private int selected;
    private InboxListContract.InboxListPresenter mPresenter;

    public InboxFilterAdapter(List<String> data, Context context, InboxListContract.InboxListPresenter presenter) {
        dataSet = data;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.layout_filter_item, parent, false);
        return new FilterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FilterHolder) holder).setLocationDate(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setSelected(int index) {
        selected = index;
    }

    class FilterHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_filter_txt)
        TextView filterText;
        @BindView(R2.id.iv_tick)
        ImageView tvDayTime;
        @BindView(R2.id.filter_item)
        View locationDateItem;

        String valueItem;

        private FilterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setLocationDate(String value) {
            this.valueItem = value;
            filterText.setText(valueItem);
            if (getAdapterPosition() == selected) {
                tvDayTime.setVisibility(View.VISIBLE);
                filterText.setTextColor(mContext.getResources().getColor(R.color.green_nob));
            } else {
                tvDayTime.setVisibility(View.GONE);
                filterText.setTextColor(mContext.getResources().getColor(R.color.black_70));
            }
        }

        @OnClick(R2.id.filter_item)
        void onClickFilterItem() {
            int prevSelected = selected;
            selected = getAdapterPosition();
            notifyItemChanged(selected);
            notifyItemChanged(prevSelected);
            mPresenter.setFilter(getAdapterPosition());
        }
    }
}
