package com.tokopedia.challenges.view.share;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.challenges.R;

import java.util.List;

public class BottomSheetShareAdapter extends RecyclerView.Adapter<BottomSheetShareAdapter.ShareViewHolder> {

    @NonNull
    private final List<ResolveInfo> mActivities;
    @NonNull
    private final PackageManager mPackageManager;

    public interface OnItemClickListener {
        void onItemClick(String packageName);
    }

    private BottomSheetShareAdapter.OnItemClickListener onItemClickListener;

    public BottomSheetShareAdapter(@NonNull List<ResolveInfo> mActivities, @NonNull PackageManager mPackageManager) {
        this.mActivities = mActivities;
        this.mPackageManager = mPackageManager;
    }

    @Override
    public BottomSheetShareAdapter.ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_item_share,
                parent, false);
        return new BottomSheetShareAdapter.ShareViewHolder(itemView);
    }

    public void setOnItemClickListener(BottomSheetShareAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private boolean isPositionOther(int position) {
        return (position == mActivities.size() + 1);
    }

    private boolean isPositionCopy(int position) {
        return (position == mActivities.size());
    }

    @Override
    public void onBindViewHolder(BottomSheetShareAdapter.ShareViewHolder holder, int position) {

        CharSequence title;
        String type;
        Drawable resources;
        if(mActivities.size()<=position){
            return;
        }

//        if (isPositionCopy(position)) {
//            resources = AppCompatResources.getDrawable(holder.iconView.getContext(), com.tokopedia.core.R.drawable.ic_copy_clipboard);
//            title = holder.labelView.getContext().getString(com.tokopedia.core.R.string.copy);
//            type = ShareBottomSheet.KEY_COPY;
//        } else
        if (isPositionOther(position)) {
            resources = AppCompatResources.getDrawable(holder.iconView.getContext(), com.tokopedia.design.R.drawable.ic_btn_other);
            title = "other";//holder.labelView.getContext().getString(com.tokopedia.design.R.string.other);
            type = ShareBottomSheet.KEY_OTHER;
        } else {
            final ResolveInfo activity = mActivities.get(position);
            resources = activity.loadIcon(mPackageManager);
            title = activity.loadLabel(mPackageManager);
            type = activity.activityInfo.packageName;
        }

        if (resources != null)
            holder.iconView.setImageDrawable(resources);

        if (title.length() > 0)
            holder.labelView.setText(title);


        final String finalType = type;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(finalType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivities.size() + 2; // for salin link and lainnya
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconView;
        private TextView labelView;

        ShareViewHolder(View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.mac_item_activity_icon);
            labelView = itemView.findViewById(R.id.mac_item_activity_label);
        }

    }
}
