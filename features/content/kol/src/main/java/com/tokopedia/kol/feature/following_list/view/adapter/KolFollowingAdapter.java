package com.tokopedia.kol.feature.following_list.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.viewmodel.FollowingViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_KOL = 1;
    private static final int TYPE_SHOP = 2;

    private Context context;
    private List<FollowingViewModel> itemList = new ArrayList<>();
    private KolFollowingList.View mainView;

    public KolFollowingAdapter(Context context, KolFollowingList.View view) {
        this.context = context;
        this.mainView = view;
    }

    public List<FollowingViewModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<FollowingViewModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_KOL) {
            return new KolFollowingViewHolder(
                    LayoutInflater.from(context).inflate(
                            R.layout.item_kol_following,
                            viewGroup,
                            false)
            );
        } else if (viewType == TYPE_SHOP) {
            return new ShopFollowingViewHolder(
                    LayoutInflater.from(context).inflate(
                            R.layout.item_shop_following,
                            viewGroup,
                            false)
            );
        } else throw new IllegalStateException("Type is not supported");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder followingViewHolder, int position) {
        final FollowingViewModel viewModel = itemList.get(position);

        if (viewModel instanceof KolFollowingViewModel && followingViewHolder instanceof KolFollowingViewHolder) {
            KolFollowingViewHolder viewHolder = (KolFollowingViewHolder) followingViewHolder;
            KolFollowingViewModel model = (KolFollowingViewModel) viewModel;
            initView(viewHolder, model);
            initData(viewHolder, model);
        } else if (viewModel instanceof ShopFollowingViewModel && followingViewHolder instanceof ShopFollowingViewHolder) {
            ShopFollowingViewHolder viewHolder = (ShopFollowingViewHolder) followingViewHolder;
            ShopFollowingViewModel model = (ShopFollowingViewModel) viewModel;
            initView(viewHolder, model);
            initData(viewHolder, model);
        }

        followingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onListItemClicked(viewModel);
            }
        });

    }

    private void initView(KolFollowingViewHolder viewHolder,
                          KolFollowingViewModel viewModel) {
        viewHolder.layout.setVisibility(viewModel.isLoadingItem() ? View.GONE : View.VISIBLE);
        viewHolder.progressBar.setVisibility(viewModel.isLoadingItem() ? View.VISIBLE : View.GONE);
        viewHolder.ivVerified.setVisibility(viewModel.isInfluencer() ? View.VISIBLE : View.GONE);
    }

    private void initData(KolFollowingViewHolder viewHolder,
                          KolFollowingViewModel viewModel) {
        if (!viewModel.isLoadingItem()) {
            ImageHandler.loadImageCircle2(context, viewHolder.ivAvatar, viewModel.getAvatarUrl());
            viewHolder.tvName.setText(MethodChecker.fromHtml(viewModel.getName()));
        }
    }

    private void initView(ShopFollowingViewHolder viewHolder,
                          ShopFollowingViewModel viewModel) {
        viewHolder.itemView.setVisibility(viewModel.isLoadingItem() ? View.GONE : View.VISIBLE);
        viewHolder.progressBar.setVisibility(viewModel.isLoadingItem() ? View.VISIBLE : View.GONE);
    }

    private void initData(ShopFollowingViewHolder viewHolder,
                          ShopFollowingViewModel viewModel) {
        ImageHandler.loadImageCircle2(context, viewHolder.ivAvatar, viewModel.getAvatarUrl());
        viewHolder.tvName.setText(MethodChecker.fromHtml(viewModel.getName()));
        viewHolder.tvEtalase.setText(viewModel.getEtalase());
        viewHolder.tvProduct.setText(viewModel.getProduct());
    }

    public void addBottomLoading() {
        getItemList().add(new KolFollowingViewModel(true));
        notifyDataSetChanged();
    }

    public void removeBottomLoading() {
        getItemList().remove(getItemCount() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FollowingViewModel viewModel = itemList.get(position);
        if (viewModel instanceof KolFollowingViewModel) {
            return TYPE_KOL;
        } else if (viewModel instanceof ShopFollowingViewModel) {
            return TYPE_SHOP;
        } else throw new IllegalStateException("Type is not supported");
    }

    public class KolFollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivVerified;
        View layout;
        TextView tvName;
        ProgressBar progressBar;

        public KolFollowingViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivVerified = itemView.findViewById(R.id.iv_verified);
            tvName = itemView.findViewById(R.id.tv_name);
            layout = itemView.findViewById(R.id.layout);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public class ShopFollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvEtalase, tvProduct;
        ProgressBar progressBar;

        public ShopFollowingViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEtalase = itemView.findViewById(R.id.tv_etalase);
            tvProduct = itemView.findViewById(R.id.tv_product);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
