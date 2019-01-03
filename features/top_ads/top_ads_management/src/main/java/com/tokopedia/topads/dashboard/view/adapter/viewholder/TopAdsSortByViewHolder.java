package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByViewHolder extends BaseViewHolder<TopAdsSortByModel> {

    public interface ListenerCheckedSort {
        boolean isItemChecked(@SortTopAdsOption String productSortId);
    }

    private ListenerCheckedSort listenerCheckedSort;
    private TextView titleSort;
    private ImageView imageCheckList;

    public TopAdsSortByViewHolder(View itemView) {
        super(itemView);
        titleSort = (TextView) itemView.findViewById(R.id.text_view_title);
        imageCheckList = (ImageView) itemView.findViewById(R.id.image_view_check);
    }

    public void setListenerCheckedSort(ListenerCheckedSort listenerCheckedSort) {
        this.listenerCheckedSort = listenerCheckedSort;
    }

    @Override
    public void bindObject(TopAdsSortByModel topAdsSortByModel) {
        boolean isItemChecked = false;
        if (listenerCheckedSort != null) {
            isItemChecked = listenerCheckedSort.isItemChecked(topAdsSortByModel.getId());
        }

        titleSort.setText(topAdsSortByModel.getTitleSort());
        if (isItemChecked) {
            imageCheckList.setVisibility(View.VISIBLE);
        } else {
            imageCheckList.setVisibility(View.INVISIBLE);
        }
    }
}
