package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdcustomer.R;
import com.tokopedia.tkpd.tkpdcustomer.R2;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by nisie on 5/16/17.
 */

public class FeedProductAdapter extends RecyclerView.Adapter<FeedProductAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.title)
        public TextView productName;

        @BindView(R2.id.price)
        public TextView productPrice;

        @BindView(R2.id.product_image)
        public ImageView productImage;

        @BindView(R2.id.black_screen)
        public FrameLayout blackScreen;

        @BindView(R2.id.extra_product)
        public TextView extraProduct;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }
    }

    protected ArrayList<ProductFeedViewModel> list;
    private ProductCardViewModel productCardViewModel;
    private final FeedPlus.View viewListener;
    protected final Context context;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<ProductFeedViewModel> list = productCardViewModel.getListProduct();
        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

        if (list.size() > 6 && position == 5) {
            holder.blackScreen.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.trans_black_40)));
            holder.extraProduct.setVisibility(View.VISIBLE);
            String extra = "+" + (list.size() - 6);
            holder.extraProduct.setText(extra);
            holder.blackScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToFeedDetail(productCardViewModel);
                }
            });
        } else {
            holder.blackScreen.setForeground(null);
            holder.extraProduct.setVisibility(View.GONE);
            holder.blackScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail();
                }
            });
        }

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail();
            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail();
            }
        });


    }

    @Override
    public int getItemCount() {
        if (productCardViewModel.getListProduct().size() > 6)
            return 6;
        else
            return productCardViewModel.getListProduct().size();
    }

    public void setData(ProductCardViewModel productCardViewModel) {
        this.productCardViewModel = productCardViewModel;
        notifyDataSetChanged();
    }

    public ProductCardViewModel getData() {
        return productCardViewModel;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}
