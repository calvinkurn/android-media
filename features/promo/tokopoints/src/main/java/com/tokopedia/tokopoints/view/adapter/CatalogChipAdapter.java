package com.tokopedia.tokopoints.view.adapter;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogCategory;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;
import java.util.Locale;

public class CatalogChipAdapter extends RecyclerView.Adapter<CatalogChipAdapter.ViewHolder> {
    private List<CatalogCategory> mItems;
    private CatalogListingPresenter mPresenter;
    private final int TICK_MS = 1000;

    public CatalogChipAdapter(CatalogListingPresenter presenter, List<CatalogCategory> items) {
        this.mPresenter = presenter;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_layout_catalog_chips, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CatalogCategory item = mItems.get(position);

        holder.label.setText(item.getName());

        if (mPresenter.getSelectedCategoryId() == item.getId()) {
            holder.outer.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_selected));
        } else {
            holder.outer.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_default));
        }

        holder.outer.setOnClickListener(v -> {
            if (mPresenter.getSelectedCategoryId() == item.getId()) {
                mPresenter.getView().updateSelectedCategoryId(CommonConstant.DEFAULT_CATEGORY_TYPE);
                mPresenter.getView().refreshTab(CommonConstant.DEFAULT_CATEGORY_TYPE);
            } else {
                mPresenter.getView().updateSelectedCategoryId(item.getId());
                mPresenter.getView().refreshTab(item.getId());
            }

            notifyDataSetChanged();
        });

        /*This section is exclusively for handling flash-sale timer*/
        if (holder.timer != null) {
            holder.timer.cancel();
        }

        /*This section is exclusively for handling flash-sale timer*/
        if (item.getTimeRemainingSeconds() > 0) {
            holder.timer = new CountDownTimer(item.getTimeRemainingSeconds() * 1000, TICK_MS) {
                @Override
                public void onTick(long l) {
                    item.setTimeRemainingSeconds(l / 1000);
                    int seconds = (int) (l / 1000) % 60;
                    int minutes = (int) ((l / (1000 * 60)) % 60);
                    int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                    holder.time.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                }

                @Override
                public void onFinish() {
                    if (mPresenter.isViewAttached() && mPresenter.getView().isAddedView()) {
                        mPresenter.getHomePageData();
                        mPresenter.getPointData();
                    }

                }
            }.start();
        } else {
            holder.time.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label, time;
        public LinearLayout outer;
        /*This section is exclusively for handling flash-sale timer*/
        public CountDownTimer timer;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_label);
            time = view.findViewById(R.id.text_time);
            outer = view.findViewById(R.id.outer);
        }
    }
}
