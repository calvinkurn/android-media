package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/15/17.
 */

public class EmptyFeedViewHolder extends AbstractViewHolder<ProductCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_empty;

    @BindView(R.id.author_name)
    TextView authorName;

    private ProductCardViewModel productCardViewModel;
    private Context context;

    public EmptyFeedViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        this.productCardViewModel = productCardViewModel;
        authorName.setText(productCardViewModel.getAuthorName());
    }


}
