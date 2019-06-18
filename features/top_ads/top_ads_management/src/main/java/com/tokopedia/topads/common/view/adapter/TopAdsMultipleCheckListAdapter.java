package com.tokopedia.topads.common.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.topads.dashboard.view.model.Ad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsMultipleCheckListAdapter<T extends Visitable, F extends AdapterTypeFactory> extends BaseListAdapter<T, F> {
    private boolean isActionMode;
    private HashSet<String> hashSet;
    private BaseMultipleCheckViewHolder.CheckedCallback<T> checkedCallback;
    private TopAdsItemClickedListener<T> itemClickedListener;

    public TopAdsMultipleCheckListAdapter(F baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
        hashSet = new HashSet<>();
    }

    public TopAdsMultipleCheckListAdapter(F baseListAdapterTypeFactory, OnAdapterInteractionListener<T> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
        hashSet = new HashSet<>();
    }

    public void setCheckedCallback(BaseMultipleCheckViewHolder.CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    public void setItemClickedListener(TopAdsItemClickedListener<T> itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        final T item = (T) visitables.get(position);
        if (holder instanceof BaseMultipleCheckViewHolder && item instanceof Ad){
            ((BaseMultipleCheckViewHolder) holder).showCheckButton(isActionMode, ((Ad) item).isAutoAds());
            ((BaseMultipleCheckViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isActionMode){
                        if (itemClickedListener != null){
                            itemClickedListener.onAdClicked(item);
                        }
                    } else {
                        boolean updatedChecked = !((BaseMultipleCheckViewHolder) holder).isChecked();
                        ((BaseMultipleCheckViewHolder) holder).setChecked(updatedChecked);
                        setChecked(((Ad) item).getId(), updatedChecked);
                        if (checkedCallback != null){
                            checkedCallback.onItemChecked(item, updatedChecked);
                        }
                    }
                }
            });
            ((BaseMultipleCheckViewHolder) holder).bindObject(item, isItemChecked(((Ad) item).getId()));
            ((BaseMultipleCheckViewHolder) holder).setCheckedCallback(new BaseMultipleCheckViewHolder.CheckedCallback<T>() {
                @Override
                public void onItemChecked(T item, boolean isChecked) {
                    setChecked(((Ad) item).getId(), isChecked);
                    if (checkedCallback != null){
                        checkedCallback.onItemChecked(item, isChecked);
                    }
                }
            });
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    private boolean isItemChecked(String id){
        return hashSet.contains(id);
    }

    public void setChecked(String id, boolean checked) {
        if (checked) {
            hashSet.add(id);
        } else {
            hashSet.remove(id);
        }
    }

    public void setActionMode(boolean actionMode) {
        isActionMode = actionMode;
    }

    public void resetCheckedItemSet() {
        hashSet = new HashSet<>();
    }

    public int getTotalChecked() {
        return hashSet.size();
    }

    public List<String> getListChecked() {
        return new ArrayList<>(hashSet);
    }

    public interface TopAdsItemClickedListener<T>{
        public void onAdClicked(T item);
    }
}
