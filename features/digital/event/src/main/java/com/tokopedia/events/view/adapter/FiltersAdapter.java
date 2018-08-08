package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.events.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.events.view.presenter.EventSearchPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class FiltersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FilterDomainModel filterModel;
    private Context mContext;
    private EventSearchPresenter mPresenter;

    public FiltersAdapter(FilterDomainModel model, Context context, EventSearchPresenter presenter) {
        this.filterModel = model;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.filter_item_layout, parent, false);
        return new FilterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FilterViewHolder) holder).setFilterView(position, filterModel.getValuesItems().get(position));
    }

    @Override
    public int getItemCount() {
        return filterModel.getValuesItems().size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.filter_checkbox)
        CheckBox filterCheckbox;
        @BindView(R2.id.tv_filter_text)
        TextView tvFilterText;
        @BindView(R2.id.filter_item)
        View filterItem;

        ValuesItemDomain valueItem;
        int mPosition;

        private FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setFilterView(int position, ValuesItemDomain value) {
            this.valueItem = value;
            this.mPosition = position;
            tvFilterText.setText(valueItem.getName());
            if (valueItem.getIsSelected())
                filterCheckbox.setChecked(true);
        }

        @OnClick(R2.id.filter_item)
        void onClickFilterItem() {
            mPresenter.onClickFilterItem(valueItem, this);
            notifyItemChanged(mPosition);
        }
    }
}
