package com.tokopedia.tkpd.peoplefave.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.tkpd.peoplefave.fragment.PeopleFavoritedShopFragment;
import com.tokopedia.tkpd.peoplefave.listener.PeopleFavoritedShopFragmentView;
import com.tokopedia.tkpd.peoplefave.model.PeopleFavoritedShopData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hangnadi on 10/11/16.
 */

public class PeopleFavoritedShopAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_MAIN = 4264;

    private List<PeopleFavoritedShopData.ShopFavorited> list;
    private PeopleFavoritedShopFragmentView viewListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.shop_name) TextView shopName;
        @Bind(R2.id.shop_avatar) ImageView shopAvatar;
        @Bind(R2.id.shop_layout) LinearLayout shopLayout;
        @Bind(R2.id.prods) TextView product;
        @Bind(R2.id.etalase) TextView etalase;
        @Bind(R2.id.unfav) TextView actionUnfavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private PeopleFavoritedShopAdapter() {
        super();
        this.list = new ArrayList<>();
    }

    public void setList(List<PeopleFavoritedShopData.ShopFavorited> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getListSize() {
        return list.size();
    }

    public static PeopleFavoritedShopAdapter createAdapter() {
        return new PeopleFavoritedShopAdapter();
    }

    public void setViewListener(PeopleFavoritedShopFragment viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_MAIN:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.listview_favorited_shop, parent, false)
                );
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_MAIN:
                bindMainView((ViewHolder) holder, getListItem(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private PeopleFavoritedShopData.ShopFavorited getListItem(int position) {
        return list.get(position);
    }

    private void bindMainView(ViewHolder holder, final PeopleFavoritedShopData.ShopFavorited shopFavorited) {
        holder.shopName.setText(shopFavorited.getShopName());
        ImageHandler.loadImageCircle2(holder.itemView.getContext(), holder.shopAvatar, shopFavorited.getShopImage());
        holder.etalase.setText(shopFavorited.getShopTotalEtalase() + " " + holder.itemView.getContext().getString(R.string.title_etalase));
        holder.product.setText(shopFavorited.getShopTotalProduct() +  " " + holder.itemView.getContext().getString(R.string.title_product));
        holder.shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.openShopPage(shopFavorited.getShopId());
            }
        });

        if(viewListener.isOwner()) {
            holder.actionUnfavorite.setVisibility(View.VISIBLE);
            holder.actionUnfavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onActionUnfavoriteClick(shopFavorited);
                }
            });
        } else {
            holder.actionUnfavorite.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_MAIN;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }
}
