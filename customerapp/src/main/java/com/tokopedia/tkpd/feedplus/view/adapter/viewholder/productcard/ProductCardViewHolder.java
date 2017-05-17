package com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import butterknife.BindView;

import static com.google.ads.conversiontracking.h.a.a;

/**
 * @author by nisie on 5/16/17.
 */

public class ProductCardViewHolder extends AbstractViewHolder<ProductCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_multi;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.shop_avatar)
    ImageView shopAvatar;

    @BindView(R.id.gold_merchant)
    ImageView goldMerchantBadge;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.share_button)
    View shareButton;

    @BindView(R.id.product_list)
    RecyclerView recyclerView;

    private FeedProductAdapter adapter;
    private FeedPlus.View viewListener;

    public ProductCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getList().size() == 1) {
                    return 6;
                } else if (adapter.getList().size() % 3 == 0 || adapter.getList().size() > 6) {
                    return 2;
                } else if (adapter.getList().size() % 2 == 0) {
                    return 3;
                } else if (adapter.getList().size() == 5) {
                    return getSpanSizeFor5Item(position);
                } else {
                    return 0;
                }
            }
        });
        adapter = new FeedProductAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private int getSpanSizeFor5Item(int position) {
        switch (position) {
            case 0:
            case 1:
                return 3;
            case 2:
            case 3:
            case 4:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        setHeader(productCardViewModel);
        adapter.setList(productCardViewModel.getListProduct());
        setFooter(productCardViewModel);
    }

    public void setHeader(ProductCardViewModel productCardViewModel) {
        String titleText = "<b>" + productCardViewModel.getShopName() + "</b> "
                + productCardViewModel.getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardViewModel.getShopAvatar());

        if (productCardViewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(TimeConverter.generateTime(productCardViewModel.getPostTime()));

    }

    public void setFooter(ProductCardViewModel footer) {
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onShareButtonClicked();
            }
        });
    }
}
