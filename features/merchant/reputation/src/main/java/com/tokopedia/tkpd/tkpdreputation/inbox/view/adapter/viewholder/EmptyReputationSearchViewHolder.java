package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;

/**
 * @author by nisie on 9/13/17.
 */

public class EmptyReputationSearchViewHolder extends AbstractViewHolder<EmptySearchModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_reputation;

    TextView button;
    TextView title;

    public EmptyReputationSearchViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        button = (TextView) itemView.findViewById(R.id.button);
    }

    @Override
    public void bind(final EmptySearchModel element) {
        title.setText(element.getTitle());
        if (!TextUtils.isEmpty(element.getButtonText())) {
            button.setVisibility(View.VISIBLE);
            button.setText(element.getButtonText());
            button.setOnClickListener(element.getButtonListener());
        } else {
            button.setVisibility(View.GONE);
        }

    }
}