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
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.home.presenter.HotList;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by m.normansyah on 28/10/2015.
 * loading already handled by {@link BaseRecyclerViewAdapter}
 */
public class HotListAdapter extends BaseRecyclerViewAdapter {
    HotList hotList;

    public final class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageofProduct;
        TextView mNameOfProduct;
        TextView mPrice;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageofProduct = (ImageView) itemView.findViewById(R.id.hotprod_img);
            mNameOfProduct = (TextView) itemView.findViewById(R.id.hotprod_name);
            mPrice = (TextView) itemView.findViewById(R.id.hotprod_price);
            cardView = (CardView) itemView.findViewById(R.id.hot_list_cardview_listproduct);
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
                final HotListModel hotListModel = ((HotListModel) data.get(position));
                ImageHandler.loadImageFit2(((ViewHolder) viewHolder).getContext(), ((ViewHolder) viewHolder).mImageofProduct, hotListModel.getHotListBiggerImage());
                ((ViewHolder) viewHolder).mNameOfProduct.setText(hotListModel.getHotListName());
                ((ViewHolder) viewHolder).mPrice.setText(hotListModel.getHotListPrice());
                ((ViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventHotlist(hotListModel.getHotListName());
                        TrackingUtils.sendMoEngageClickHotListEvent(hotListModel);
                        hotList.moveToOtherActivity(hotListModel);
                    }
                });
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
