package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;


public class FiltersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FilterDomainModel filterModel;
    private Context mContext;
    private DealsSearchPresenter mPresenter;

    public FiltersAdapter(FilterDomainModel model, Context context, DealsSearchPresenter presenter) {
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

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox filterCheckbox;
        TextView tvFilterText;

        View itemView;

        ValuesItemDomain valueItem;
        int mPosition;

        private FilterViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            filterCheckbox=itemView.findViewById(R.id.filter_checkbox);
            tvFilterText=itemView.findViewById(R.id.tv_filter_text);
        }

        private void setFilterView(int position, ValuesItemDomain value) {
            this.valueItem = value;
            this.mPosition = position;
            tvFilterText.setText(valueItem.getName());
            this.itemView.setOnClickListener(this);
            if (valueItem.getIsSelected())
                filterCheckbox.setChecked(true);


        }


        @Override
        public void onClick(View v) {
            mPresenter.onClickFilterItem(valueItem, this);
            notifyItemChanged(mPosition);
        }
    }
}
