package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedDetailViewHolder extends AbstractViewHolder<FeedDetailViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_detail;
    private static final String CASHBACK = "Cashback";

    public TextView productName;
    public TextView productPrice;
    public ImageView productImage;
    public ImageView wishlist;
    public RatingBar productRating;
    public TextView cashback;
    public TextView wholesale;
    public TextView preorder;
    public ImageView freeReturn;
    public View mainView;

    protected final FeedPlusDetail.View viewListener;

    public FeedDetailViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        productImage = itemView.findViewById(R.id.product_image);
        wishlist = itemView.findViewById(R.id.wishlist);
        productRating = itemView.findViewById(R.id.product_rating);
        cashback = itemView.findViewById(R.id.cashback);
        wholesale = itemView.findViewById(R.id.wholesale);
        preorder = itemView.findViewById(R.id.preorder);
        freeReturn = itemView.findViewById(R.id.free_return);
        mainView = itemView.findViewById(R.id.main_view);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final FeedDetailViewModel feedDetailViewModel) {

        ImageHandler.LoadImage(productImage, feedDetailViewModel.getImageSource());

        if (feedDetailViewModel.isWishlist()) {
            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist_faved);
        } else {
            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist);

        }
        productName.setText(MethodChecker.fromHtml(feedDetailViewModel.getName()));
        productPrice.setText(feedDetailViewModel.getPrice());

        if (feedDetailViewModel.getRating() > 0) {
            productRating.setRating((feedDetailViewModel.getRating().floatValue()));
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.INVISIBLE);
        }

        if (TextUtils.isEmpty(feedDetailViewModel.getCashback()))
            cashback.setVisibility(View.GONE);
        else {
            cashback.setVisibility(View.VISIBLE);
            cashback.setText(CASHBACK + " " + feedDetailViewModel.getCashback());
        }

        if (feedDetailViewModel.isWholesale())
            wholesale.setVisibility(View.VISIBLE);
        else
            wholesale.setVisibility(View.GONE);

        if (feedDetailViewModel.isFreeReturn())
            freeReturn.setVisibility(View.VISIBLE);
        else
            freeReturn.setVisibility(View.GONE);

        if (feedDetailViewModel.isPreorder())
            preorder.setVisibility(View.VISIBLE);
        else
            preorder.setVisibility(View.GONE);

        wishlist.setOnClickListener(v -> viewListener.onWishlistClicked(
                getAdapterPosition(),
                feedDetailViewModel.getProductId(),
                feedDetailViewModel.isWishlist())
        );

        mainView.setOnClickListener(v -> viewListener.onGoToProductDetail(
                feedDetailViewModel, getAdapterPosition())
        );
    }
}
