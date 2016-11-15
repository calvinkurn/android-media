package com.tokopedia.core.dynamicfilter.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.discovery.adapter.ProductAdapter;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterListAdapter extends ProductAdapter {
    private static final String TAG = DynamicFilterListAdapter.class.getSimpleName();
    private static final int DYNAMIC_FILTER_MODEL = 912_282;
    private int activeLocation = -1;
    private Context context;
    private NotifyActive notifyActive = new NotifyActive() {
        @Override
        public void notify(boolean status, int position) {
            //[START] reset old active position
            Log.d(TAG, "notify status " + status + " position " + position);
            if (activeLocation != -1)
                resetOldActivePositon();
            //[END] reset old active position

            activeLocation = position;

            //[START] reset old active position
            activatePosition(activeLocation);
            //[END] reset old active position

            notifyDataSetChanged();
        }

        private void resetOldActivePositon() {
            RecyclerViewItem recyclerViewItem = getData().get(activeLocation);
            if (recyclerViewItem != null && recyclerViewItem instanceof DynamicListModel) {
                DynamicListModel dynamicListModel = (DynamicListModel) recyclerViewItem;
                dynamicListModel.active = false;

                // set data again
                data.set(activeLocation, dynamicListModel);

            }
        }
    };

    public void activatePosition(int location) {
        RecyclerViewItem recyclerViewItem = getData().get(location);
        if (recyclerViewItem != null && recyclerViewItem instanceof DynamicListModel) {
            DynamicListModel dynamicListModel = (DynamicListModel) recyclerViewItem;
            dynamicListModel.active = true;
            // set data again
            data.set(location, dynamicListModel);
        }
        if (activeLocation != location) {
            // Update active location if not same
            activeLocation = location;
        }
    }


    public void setActiveIndicator(String key, boolean active) {
        for (RecyclerViewItem item : getData()) {
            DynamicListModel model = (DynamicListModel) item;
            for (DynamicFilterModel.Option o : model.filter.getOptions()) {
                if (o.getKey().equals(key)) {
                    model.setHasFilter(active);
                }
            }
        }
        if (key.equals("sc")) {
            DynamicListModel model = (DynamicListModel) getData().get(0);
            model.setHasFilter(active);
        }
        notifyDataSetChanged();
    }

    public void reset() {
        for (RecyclerViewItem item : getData()) {
            DynamicListModel model = (DynamicListModel) item;
            model.setHasFilter(false);
        }
        notifyDataSetChanged();
    }

    interface NotifyActive {
        void notify(boolean status, int position);
    }

    public DynamicFilterListAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
        this.context = context;
        for (Map.Entry<String, String> entry : ((DynamicFilterView) context).getSelectedFilter().entrySet()) {
            setActiveIndicator(entry.getKey(), true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DYNAMIC_FILTER_MODEL:
                View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dynamic_filter, parent, false);
                return new DynamicViewHolder(context, itemLayoutView);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DYNAMIC_FILTER_MODEL:
                DynamicViewHolder dynamicViewHolder = (DynamicViewHolder) holder;
                dynamicViewHolder.bindData(getData().get(position), position);
                dynamicViewHolder.setNotifyActive(notifyActive);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public static class DynamicViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.dynamic_filter_list_text)
        TextView dynamicFilterListText;

        DynamicListModel dynamicListModel;
        Context context;
        NotifyActive notifyActive;

        public NotifyActive getNotifyActive() {
            return notifyActive;
        }

        public void setNotifyActive(NotifyActive notifyActive) {
            this.notifyActive = notifyActive;
        }

        public DynamicViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void bindData(RecyclerViewItem recyclerViewItem, int position) {
            if (recyclerViewItem != null && recyclerViewItem instanceof DynamicListModel) {
                bindData((DynamicListModel) recyclerViewItem, position);
            }
        }

        public void bindData(DynamicListModel dynamicListModel, int position) {
            if (dynamicListModel.active) {
                dynamicFilterListText.setSelected(true);
            } else {
                dynamicFilterListText.setSelected(false);
            }
            Drawable dot;
            if (dynamicListModel.isHasFilter()) {
                dot = setTint(ContextCompat.getDrawable(context, R.drawable.dot_notification), ContextCompat.getColor(context, R.color.tkpd_main_green));
            } else {
                dot = setTint(ContextCompat.getDrawable(context, R.drawable.dot_notification), ContextCompat.getColor(context, android.R.color.transparent));
            }
            dynamicFilterListText.setCompoundDrawablesWithIntrinsicBounds(dot, null, null, null);
            dynamicFilterListText.setText(dynamicListModel.title);

            this.dynamicListModel = dynamicListModel;
        }

        public Drawable setTint(Drawable drawable, int color) {
            final Drawable newDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(newDrawable, color);
            return newDrawable;
        }

        @OnClick(R2.id.dynamic_filter_list_text)
        public void dynamicFilterClick() {
            Context context = itemView.getContext();
            if (context != null && context instanceof DynamicFilterView) {
                ((DynamicFilterView) context).setFragmentBasedOnData(dynamicListModel.filter);
            }
            if (getNotifyActive() != null) {
                getNotifyActive().notify(true, getAdapterPosition());
            }
            CommonUtils.hideKeyboard((Activity) context, itemView);
        }
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case DYNAMIC_FILTER_MODEL:
                return recyclerViewItem.getType();
        }
        return super.isInType(recyclerViewItem);
    }

    @Parcel
    public static class DynamicListModel extends RecyclerViewItem {
        String title;
        boolean active;
        boolean hasFilter;
        DynamicFilterModel.Filter filter;

        public DynamicListModel() {
            setType(DYNAMIC_FILTER_MODEL);
        }

        public DynamicListModel(String title) {
            this(title, false);
        }

        public DynamicListModel(String title, boolean active) {
            this();
            this.title = title;
            this.active = active;
        }

        public DynamicListModel(DynamicFilterModel.Filter filter) {
            this(filter.getTitle(), false);
            this.filter = filter;
        }

        public boolean isHasFilter() {
            return hasFilter;
        }

        public void setHasFilter(boolean hasFilter) {
            this.hasFilter = hasFilter;
        }
    }

    @Deprecated
    public static List<DynamicListModel> convertTo(List<String> temp) {
        List<DynamicListModel> dynamicListModels = new ArrayList<>();
        for (String t : temp) {
            dynamicListModels.add(new DynamicListModel(t));
        }

        return dynamicListModels;
    }

    public static List<DynamicListModel> convertTo2(List<DynamicFilterModel.Filter> temp) {
        List<DynamicListModel> dynamicListModels = new ArrayList<>();
        for (DynamicFilterModel.Filter t : temp) {
            dynamicListModels.add(new DynamicListModel(t));
        }

        return dynamicListModels;
    }
}
