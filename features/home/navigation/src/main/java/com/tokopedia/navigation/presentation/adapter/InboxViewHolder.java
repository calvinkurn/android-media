package com.tokopedia.navigation.presentation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Inbox;

/**
 * Author errysuprayogi on 13,March,2019
 */
class InboxViewHolder extends AbstractViewHolder<Inbox> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_notification;
    private LabelView labelView;
    private LinearLayout container;
    private Context context;
    private InboxAdapterListener listener;

    InboxViewHolder(View itemView, InboxAdapterListener listener) {
        super(itemView);
        labelView = itemView.findViewById(R.id.labelview);
        container = itemView.findViewById(R.id.container);
        context = itemView.getContext();
        this.listener = listener;
    }


    @Override
    public void bind(Inbox item) {
        labelView.setImageResource(item.getIcon());
        labelView.setTitle(context.getString(item.getTitle()));
        labelView.setSubTitle(context.getString(item.getSubtitle()));
        if (item.getTotalBadge() != null
                && !item.getTotalBadge().isEmpty()
                && !item.getTotalBadge().equalsIgnoreCase("0")) {
            try {
                labelView.setBadgeCounter(Integer.parseInt(item.getTotalBadge()));
            } catch (NumberFormatException e) {
                //ignore
            }
        } else {
            labelView.setBadgeCounter(0);
        }
        labelView.showRightArrow(false);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(item, getAdapterPosition());
            }
        });
    }
}
