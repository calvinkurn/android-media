package com.tokopedia.tokopoints.view.adapter;

import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CatalogChipAdapter extends RecyclerView.Adapter<CatalogChipAdapter.ViewHolder> {
    private List<CatalogCategory> mItems;
    private CatalogListingPresenter mPresenter;
    private final int TICK_MS = 1000;

    /*This section is exclusively for handling flash-sale timer*/
    final private List<ViewHolder> mViewHolders;
    private CatalogCategory mItem;
    private Handler mHandler = new Handler();
    private Timer mTimer;

    private Runnable mRunnableUpdateTime = new Runnable() {
        @Override
        public void run() {
            synchronized (mViewHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : mViewHolders) {
                        holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public CatalogChipAdapter(CatalogListingPresenter presenter, List<CatalogCategory> items) {
        this.mPresenter = presenter;
        this.mItems = items;
        for (int i = 0; i < mItems.size(); i++) {
            //Converting seconds to milliseconds
            mItems.get(i).setTimeWithCurrentMs(mItems.get(i).getTimeRemainingSeconds() * 1000 * 60 + System.currentTimeMillis());
        }
        /*This section is exclusively for handling flash-sale timer*/
        mViewHolders = new ArrayList<>();
        startUpdateTimer();
    }

    /*This section is exclusively for handling flash-sale timer*/
    private void startUpdateTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mHandler != null) {
                    mHandler.post(mRunnableUpdateTime);
                }
            }
        }, TICK_MS, TICK_MS);
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

        /*This section is exclusively for handling flash-sale timer*/
        mItem = item;
        if (item.getTimeRemainingSeconds() > 0) {
            synchronized (mViewHolders) {
                mViewHolders.add(holder);
            }
        }

        holder.label.setText(item.getName());

        if (mPresenter.getSelectedCategoryId() == item.getId()) {
            holder.outer.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_selected));
        } else {
            holder.outer.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_default));
        }

        holder.outer.setOnClickListener(v -> {
            mPresenter.getView().updateSelectedCategoryId(item.getId());
            mPresenter.getView().refreshTab(item.getId());
            notifyDataSetChanged();
        });

        /*This section is exclusively for handling flash-sale timer*/
        if (item.getTimeRemainingSeconds() > 0) {
            holder.updateTimeRemaining(System.currentTimeMillis());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label, time;
        public LinearLayout outer;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_label);
            time = view.findViewById(R.id.text_time);
            outer = view.findViewById(R.id.outer);
        }

        /*This section is exclusively for handling flash-sale timer*/
        public void updateTimeRemaining(long currentTime) {
            long timeDiff = mItem.getTimeWithCurrentMs() - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                time.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
            } else {
                //refreshing the homepage for tokopoints then cleanup timers and handler
                try {
                    mTimer.cancel();
                    mHandler.removeCallbacks(mRunnableUpdateTime);
                    mHandler = null;
                    mRunnableUpdateTime = null;
                    mTimer = null;
                    mPresenter.getHomePageData();
                    mPresenter.getPointData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
