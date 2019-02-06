package com.tokopedia.gm.featured.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.featured.constant.GMFeaturedProductTypeView;
import com.tokopedia.gm.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.view.adapter.viewholder.GMFeaturedProductViewHolder;

import java.util.Collections;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductAdapter extends BaseMultipleCheckListAdapter<GMFeaturedProductModel> implements
        ItemTouchHelperAdapter, GMFeaturedProductViewHolder.PostDataListener {

    private OnStartDragListener onStartDragListener;
    private UseCaseListener useCaseListener;

    public GMFeaturedProductAdapter(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    public void setUseCaseListener(UseCaseListener useCaseListener) {
        this.useCaseListener = useCaseListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GMFeaturedProductModel.TYPE:
                return new GMFeaturedProductViewHolder(getLayoutView(parent, R.layout.item_gm_featured_product), onStartDragListener, this);
            default:
                return super.onCreateViewHolder(parent, viewType);
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
