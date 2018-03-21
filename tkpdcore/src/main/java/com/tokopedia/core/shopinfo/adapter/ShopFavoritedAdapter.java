package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.shopinfo.models.shopfavoritedmodel.ShopFavoritee;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 10/5/2016.
 */
public class ShopFavoritedAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_FAVORITEE = 100;

    private ArrayList<ShopFavoritee> list;
    private final Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_name)
        TextView userName;

        @BindView(R2.id.user_avatar)
        ImageView userAvatar;

        @BindView(R2.id.user_layout)
        LinearLayout userLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ShopFavoritedAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_FAVORITEE:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_favoritee, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_FAVORITEE:
                bindFavoritee((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindFavoritee(final ViewHolder holder, int position) {
        holder.userName.setText(list.get(position).getUserName());
        ImageHandler.loadImageCircle2(context,
                holder.userAvatar,
                list.get(position).getUserImage());
        holder.userLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (context.getApplicationContext() instanceof TkpdCoreRouter) {
                    context.startActivity(
                            ((TkpdCoreRouter) context.getApplicationContext())
                                    .getTopProfileIntent(context, list.get(
                                            holder.getAdapterPosition()).getUserId())
                    );
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_FAVORITEE;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount() ;
    }

    public static ShopFavoritedAdapter createInstance(Context context) {
        return new ShopFavoritedAdapter(context);
    }

    public ArrayList<ShopFavoritee> getList() {
        return list;
    }

    public void addList(List<ShopFavoritee> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
