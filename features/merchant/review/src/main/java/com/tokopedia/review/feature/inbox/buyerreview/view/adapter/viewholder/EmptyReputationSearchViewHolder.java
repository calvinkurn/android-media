package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author by nisie on 9/13/17.
 */

public class EmptyReputationSearchViewHolder extends AbstractViewHolder<EmptySearchModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_reputation;

    Typography button;
    Typography title;

    public EmptyReputationSearchViewHolder(View itemView) {
        super(itemView);
        title = (Typography) itemView.findViewById(R.id.title);
        button = (Typography) itemView.findViewById(R.id.button);
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