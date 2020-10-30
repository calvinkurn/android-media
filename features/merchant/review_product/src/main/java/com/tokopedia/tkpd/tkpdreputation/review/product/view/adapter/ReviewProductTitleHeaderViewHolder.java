package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductTitleHeaderViewHolder extends AbstractViewHolder<ReviewProductModelTitleHeader> {
    public static final int LAYOUT = R.layout.item_product_review_header;

    private TextView title;

    public ReviewProductTitleHeaderViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.title_text);
    }

    @Override
    public void bind(ReviewProductModelTitleHeader element) {
        title.setText(element.getTitle());
    }
}
