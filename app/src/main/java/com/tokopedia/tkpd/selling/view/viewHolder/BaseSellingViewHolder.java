package com.tokopedia.tkpd.selling.view.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.tokopedia.tkpd.selling.model.SellingStatusTxModel;

/**
 * Created by Erry on 7/25/2016.
 */
public abstract class BaseSellingViewHolder<T> extends SwappingHolder {

    public BaseSellingViewHolder(View itemView, MultiSelector multiSelector) {
        super(itemView, multiSelector);
    }

    public BaseSellingViewHolder(View itemView) {
        super(itemView, new MultiSelector());
    }

    public abstract void bindDataModel(Context context, T model);

    public abstract void setOnItemClickListener(OnItemClickListener clickListener);

    public interface OnItemClickListener {
        void onItemClicked(int position);
        void onLongClicked(int position);
    }
}
