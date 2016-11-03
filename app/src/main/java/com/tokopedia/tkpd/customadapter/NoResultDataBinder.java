package com.tokopedia.tkpd.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.util.DataBindAdapter;
import com.tokopedia.tkpd.util.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/26/16.
 */
public class NoResultDataBinder extends DataBinder<NoResultDataBinder.ViewHolder> {


    public NoResultDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.no_result_image)
        ImageView emptyImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public NoResultDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_no_result, null);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        ImageHandler.loadImageWithId(holder.emptyImage, R.drawable.status_no_result);

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
