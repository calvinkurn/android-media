package com.tokopedia.feedplus.view.adapter.viewholder.productcard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/16/17.
 */

public class FeedProductAdapter extends RecyclerView.Adapter<FeedProductAdapter.ViewHolder> {

    private static final int MAX_FEED_SIZE = 6;
    private static final int MAX_FEED_SIZE_SMALL = 3;
    private static final int LAST_FEED_POSITION = 5;
    private static final int LAST_FEED_POSITION_SMALL = 2;
    protected final Context context;
    private final FeedPlus.View viewListener;
    protected ArrayList<ProductFeedViewModel> list;
    private ActivityCardViewModel activityCardViewModel;
    private int positionInFeed;

    public FeedProductAdapter(Context context, FeedPlus.View viewListener) {
        this.context = context;
        this.viewListener = viewListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_feed_product_item_blurred, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ArrayList<ProductFeedViewModel> list = activityCardViewModel.getListProduct();
        ImageHandler.loadImage2(
                holder.productImage,
                getItemCount() > 1 ? list.get(position).getImageSource() : list.get(position)
                        .getImageSourceSingle(),
                R.drawable.ic_loading_image);

        if (list.size() > MAX_FEED_SIZE && position == LAST_FEED_POSITION) {
            int extraProduct = (activityCardViewModel.getTotalProduct() - LAST_FEED_POSITION);
            showBlackScreen(holder, extraProduct);

            holder.container.setOnClickListener(goToFeedDetail());
            holder.productLayout.setOnClickListener(goToFeedDetail());
        } else if (list.size() < MAX_FEED_SIZE
                && list.size() > MAX_FEED_SIZE_SMALL
                && position == LAST_FEED_POSITION_SMALL) {
            int extraProduct = (activityCardViewModel.getTotalProduct() - LAST_FEED_POSITION_SMALL);
            showBlackScreen(holder, extraProduct);

            holder.container.setOnClickListener(goToFeedDetail());
            holder.productLayout.setOnClickListener(goToFeedDetail());
        } else {
            holder.extraProduct.setBackground(null);
            holder.extraProduct.setVisibility(View.GONE);

            holder.productName.setText(list.get(position).getName());

            holder.productPrice.setVisibility(View.VISIBLE);
            holder.productPrice.setText(list.get(position).getPrice());

            holder.container.setOnClickListener(
                    goToProductDetail(list, holder.getAdapterPosition())
            );
            holder.productLayout.setOnClickListener(
                    goToProductDetail(list, holder.getAdapterPosition())
            );
        }

        setProductNamePadding(holder);
    }

    @Override
    public int getItemCount() {
        if (activityCardViewModel != null) {
            if (activityCardViewModel.getListProduct().size() >= MAX_FEED_SIZE)
                return MAX_FEED_SIZE;
            else if (activityCardViewModel.getListProduct().size() >= MAX_FEED_SIZE_SMALL)
                return MAX_FEED_SIZE_SMALL;
            else
                return activityCardViewModel.getListProduct().size();
        }

        return 0;
    }

    public void setData(ActivityCardViewModel activityCardViewModel, int positionInFeed) {
        this.activityCardViewModel = activityCardViewModel;
        this.positionInFeed = positionInFeed;
        notifyDataSetChanged();
    }

    public ActivityCardViewModel getData() {
        return activityCardViewModel;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    private void showBlackScreen(ViewHolder holder, int extraProduct) {
        String extra = String.format("+%s", String.valueOf(extraProduct));
        holder.extraProduct.setBackground(new ColorDrawable(
                MethodChecker.getColor(
                        holder.extraProduct.getContext(),
                        R.color.black_screen_overlay))
        );
        holder.extraProduct.setVisibility(View.VISIBLE);
        holder.extraProduct.setText(extra);

        String seeOtherProduct = String.format(
                holder.productName.getContext().getString(R.string.see_other_product),
                String.valueOf(extraProduct));
        holder.productName.setText(seeOtherProduct);
        holder.productPrice.setVisibility(View.GONE);
    }

    private View.OnClickListener goToFeedDetail() {
        return v -> viewListener.onGoToFeedDetail(
                activityCardViewModel.getPage(),
                activityCardViewModel.getRowNumber(), activityCardViewModel.getFeedId()
        );
    }

    private View.OnClickListener goToProductDetail(ArrayList<ProductFeedViewModel> list,
                                                   int position) {
        return v -> {
            viewListener.onGoToProductDetailFromProductUpload(
                    activityCardViewModel.getRowNumber(),
                    activityCardViewModel.getPositionFeedCard(),
                    list.get(position).getPage(),
                    position,
                    String.valueOf(list.get(position).getProductId()),
                    list.get(position).getImageSourceSingle(),
                    list.get(position).getName(),
                    list.get(position).getPrice(),
                    list.get(position).getPriceInt(),
                    list.get(position).getUrl(),
                    activityCardViewModel.getEventLabel()
            );

            viewListener.eventTrackingEEGoToProduct(activityCardViewModel.getHeader().getShopId(),
                    activityCardViewModel.getFeedId(),
                    activityCardViewModel.getTotalProduct(),
                    this.positionInFeed,
                    "-");

        };
    }

    private void setProductNamePadding(ViewHolder holder) {
        int paddingSide, paddingTop, paddingBottom;
        Resources resources = context.getResources();

        holder.productName.setLines(2);

        if (getItemCount() == 1) {
            paddingTop = (int) resources.getDimension(R.dimen.product_padding_medium);
            paddingBottom = (int) resources.getDimension(R.dimen.product_padding_medium);

            holder.productName.setLines(1);
        } else if (getItemCount() == MAX_FEED_SIZE_SMALL) {
            paddingTop = (int) resources.getDimension(R.dimen.product_padding_small);
            paddingBottom = (int) resources.getDimension(R.dimen.product_padding_small);

        } else {
            paddingTop = (int) resources.getDimension(R.dimen.product_padding_very_small);
            paddingBottom = (int) resources.getDimension(R.dimen.new_margin_small);

        }

        paddingSide = (int) resources.getDimension(R.dimen.dp_2);
        holder.productName.setPadding(paddingSide, paddingTop, paddingSide, 0);
        holder.productPrice.setPadding(paddingSide, 0, paddingSide, paddingBottom);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View container;
        private TextView productName;
        private CardView productLayout;
        private ImageView productImage;
        private TextView productPrice;
        private TextView extraProduct;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            container = itemLayoutView;
            productName = itemView.findViewById(R.id.title);
            extraProduct = itemView.findViewById(R.id.extra_product);
            productImage = itemView.findViewById(R.id.product_image);
            productLayout = itemView.findViewById(R.id.product_layout);
            productPrice = itemView.findViewById(R.id.price);
        }
    }
}
