package com.tokopedia.gm.featured.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.featured.constant.GMFeaturedProductTypeView;
import com.tokopedia.gm.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.view.adapter.model.TickerReadMoreFeaturedModel;
import com.tokopedia.gm.featured.view.adapter.viewholder.GMFeaturedProductViewHolder;
import com.tokopedia.gm.featured.view.adapter.viewholder.TickerReadMoreFeaturedViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductAdapter extends BaseMultipleCheckListAdapter<GMFeaturedProductModel> implements
        ItemTouchHelperAdapter, GMFeaturedProductViewHolder.PostDataListener {

    private OnStartDragListener onStartDragListener;
    private TickerReadMoreFeaturedViewHolder.TickerViewHolderListener tickerViewHolderListener;
    private UseCaseListener useCaseListener;

    public GMFeaturedProductAdapter(
            OnStartDragListener onStartDragListener,
            TickerReadMoreFeaturedViewHolder.TickerViewHolderListener tickerViewHolderListener
    ) {
        this.onStartDragListener = onStartDragListener;
        this.tickerViewHolderListener = tickerViewHolderListener;
    }

    public void setUseCaseListener(UseCaseListener useCaseListener) {
        this.useCaseListener = useCaseListener;
    }

    @Override
    public List<GMFeaturedProductModel> getData() {
        List<GMFeaturedProductModel> gmFeaturedProductModels = new ArrayList<>();
        for (ItemType model : super.getData()) {
            if(model instanceof GMFeaturedProductModel){
                gmFeaturedProductModels.add((GMFeaturedProductModel) model);
            }
        }
        return gmFeaturedProductModels;
    }

    @Override
    public int getDataSize() {
        List<GMFeaturedProductModel> gmFeaturedProductModels = new ArrayList<>();
        for (ItemType model : super.getData()) {
            if(model instanceof GMFeaturedProductModel){
                gmFeaturedProductModels.add((GMFeaturedProductModel) model);
            }
        }
        return gmFeaturedProductModels.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GMFeaturedProductModel.TYPE:
                return new GMFeaturedProductViewHolder(
                        getLayoutView(parent, R.layout.item_gm_featured_product), onStartDragListener,
                        this
                );
            case TickerReadMoreFeaturedModel.TYPE:
                return new TickerReadMoreFeaturedViewHolder(
                        getLayoutView(parent, R.layout.item_featured_product_ticker_idle_pm),
                        tickerViewHolderListener
                );
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected void bindData(int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        if(viewHolder instanceof BaseViewHolder){
            ((BaseViewHolder) viewHolder).bindObject(data.get(position));
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getFeaturedProductType() {
        if (useCaseListener != null) {
            return useCaseListener.getFeaturedProductTypeView();
        } else {
            return GMFeaturedProductTypeView.DEFAULT_DISPLAY;
        }
    }

    public interface UseCaseListener {

        int getFeaturedProductTypeView();
    }
}
