package com.tokopedia.tokopoints.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogFilterPointRange;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;

import java.util.List;

public class FiltersBottomSheet extends BottomSheets {

    private List<CatalogFilterPointRange> mFilterDetails;
    private OnSaveFilterCallback onSaveFilterCallback;
    private boolean fromInitView = true;
    private int lastCheckedPosition = Integer.MIN_VALUE, lastSavedPosition = Integer.MIN_VALUE;

    public interface OnSaveFilterCallback {
        void onSaveFilter(CatalogFilterPointRange filter, int selectedPosition);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.tp_bottomsheet_filters;
    }

    @Override
    public void initView(View view) {
        fromInitView = true;
        RecyclerView rvFilters = view.findViewById(R.id.rv_filters);
        TextView btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFilterDetails == null) {
                    return;
                }

                if (lastCheckedPosition != Integer.MIN_VALUE) {
                    if (lastSavedPosition != Integer.MIN_VALUE)
                        mFilterDetails.get(lastSavedPosition).setSelected(false);
                    lastSavedPosition = lastCheckedPosition;
                    onSaveFilterCallback.onSaveFilter(mFilterDetails.get(lastSavedPosition), lastSavedPosition);
                    mFilterDetails.get(lastSavedPosition).setSelected(true);
                } else
                    onSaveFilterCallback.onSaveFilter(null, lastSavedPosition);
                dismiss();

                AnalyticsTrackerUtil.sendEvent(getActivity(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SAVE_FILTER,
                        mFilterDetails.get(lastSavedPosition).getText());
            }
        });
        rvFilters.setAdapter(new FiltersAdapter(mFilterDetails));
    }

    @Override
    protected String title() {
        return getString(R.string.title_bottomsheet_filters);
    }

    public void setData(List<CatalogFilterPointRange> filterDetails, OnSaveFilterCallback onSaveFilterCallback) {
        this.mFilterDetails = filterDetails;
        this.onSaveFilterCallback = onSaveFilterCallback;
    }

    public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

        private List<CatalogFilterPointRange> mFilterDetails;

        public FiltersAdapter(List<CatalogFilterPointRange> filterDetails) {
            this.mFilterDetails = filterDetails;
            int size = getItemCount();
            for (int i = 0; i < size; i++) {
                if (filterDetails.get(i).isSelected()) {
                    lastCheckedPosition = i;
                    lastSavedPosition = i;
                    break;
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_catalog_filters, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!TextUtils.isEmpty(mFilterDetails.get(position).getText()))
                holder.radioButton.setText(mFilterDetails.get(position).getText());
            if (fromInitView) {
                holder.radioButton.setChecked(mFilterDetails.get(position).isSelected());
            } else {
                holder.radioButton.setChecked(position == lastCheckedPosition);
            }
        }

        @Override
        public int getItemCount() {
            return mFilterDetails.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton radioButton;

            public ViewHolder(View itemView) {
                super(itemView);
                radioButton = itemView.findViewById(R.id.rb_filter);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastCheckedPosition = getAdapterPosition();
                        fromInitView = false;

                        try {
                            if (mFilterDetails != null && mFilterDetails.get(getAdapterPosition()) != null) {
                                AnalyticsTrackerUtil.sendEvent(getActivity(),
                                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                                        AnalyticsTrackerUtil.ActionKeys.PILIH_FILTER,
                                        mFilterDetails.get(getAdapterPosition()).getText());
                            }
                        } catch (Exception e) {

                        }

                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
