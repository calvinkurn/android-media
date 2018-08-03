package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class BadReasonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> dataSet;
    private Context mContext;
    private InboxDetailContract.InboxDetailPresenter mPresenter;

    public BadReasonAdapter(List<String> data, Context context, InboxDetailContract.InboxDetailPresenter presenter) {
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
        return new BadReasonHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BadReasonHolder) holder).setLocationDate(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class BadReasonHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_filter_txt)
        TextView filterText;
        @BindView(R2.id.filter_item)
        View locationDateItem;

        String valueItem;

        private BadReasonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setLocationDate(String value) {
            this.valueItem = value;
            filterText.setText(valueItem);
        }

        @OnClick(R2.id.filter_item)
        void onClickFilterItem() {
            mPresenter.setBadRating(getAdapterPosition());
        }
    }
}
