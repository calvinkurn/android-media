package com.tokopedia.kol.feature.following_list.view.adapter;

import android.content.Context;
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
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingAdapter extends RecyclerView.Adapter<KolFollowingAdapter.KolFollowingViewHolder> {

    private Context context;
    private List<KolFollowingViewModel> itemList = new ArrayList<>();
    private KolFollowingList.View mainView;

    public KolFollowingAdapter(Context context, KolFollowingList.View view) {
        this.context = context;
        this.mainView = view;
    }

    public List<KolFollowingViewModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<KolFollowingViewModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public KolFollowingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_kol_following,
                viewGroup,
                false);

        return new KolFollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KolFollowingViewHolder kolFollowingViewHolder, int position) {
        final KolFollowingViewModel viewModel = itemList.get(position);
        initView(kolFollowingViewHolder, viewModel);
        initData(kolFollowingViewHolder, viewModel);
        kolFollowingViewHolder.layout.setOnClickListener(new View.OnClickListener() {
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
}
