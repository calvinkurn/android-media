package com.tokopedia.tkpd.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.home.model.HotListModel;
import com.tokopedia.tkpd.home.presenter.HotList;
import com.tokopedia.tkpd.var.RecyclerViewItem;
import com.tokopedia.tkpd.var.TkpdState;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 28/10/2015.
 * loading already handled by {@link BaseRecyclerViewAdapter}
 */
public class HotListAdapter extends BaseRecyclerViewAdapter {
    HotList hotList;

    public final class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.hotprod_img)
        ImageView mImageofProduct;
        @Bind(R2.id.hotprod_name)
        TextView mNameOfProduct;
        @Bind(R2.id.hotprod_price)
        TextView mPrice;
        @Bind(R2.id.hot_list_cardview)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.hot_list_cardview)
        public void hotListClick(View v) {
            if (itemView.getContext() != null) {
                RecyclerViewItem temp = data.get(getAdapterPosition());

                HotListModel hlm = (HotListModel) data.get(getAdapterPosition());
                UnifyTracking.eventHotlist(hlm.getHotListName());

                hotList.moveToOtherActivity(temp);
            }
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }

    public HotListAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TkpdState.RecyclerView.VIEW_STANDARD) {
            View parentView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listview_hotproduct, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(parentView);
            return viewHolder;
        }
        return super.onCreateViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position > data.size())
            return;
        switch (getItemViewType(position)) {
            case TkpdState.RecyclerView.VIEW_STANDARD:
                HotListModel temp = ((HotListModel) data.get(position));
                ImageHandler.loadImageFit2(((ViewHolder) viewHolder).getContext(), ((ViewHolder) viewHolder).mImageofProduct, temp.getHotListBiggerImage());
                ((ViewHolder) viewHolder).mNameOfProduct.setText(temp.getHotListName());
                ((ViewHolder) viewHolder).mPrice.setText(temp.getHotListPrice());
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position))
            return super.getItemViewType(position);
        return TkpdState.RecyclerView.VIEW_STANDARD;
    }

    public HotList getHotList() {
        return hotList;
    }

    public void setHotList(HotList hotList) {
        this.hotList = hotList;
    }
}
