package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdcustomer.R;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreAdapter extends RecyclerView.Adapter<OfficialStoreAdapter.ViewHolder>{

    protected ArrayList<ProductFeedViewModel> list;
    private final Context context;
    private final FeedPlus.View viewListener;

    public OfficialStoreAdapter(Context context, FeedPlus.View viewListener) {
        this.context = context;
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;
        public ImageView shopAva;
        public TextView shopName;
        public ImageView favoriteButton;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            shopAva = (ImageView) itemLayoutView.findViewById(R.id.shop_ava);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            favoriteButton = (ImageView) itemLayoutView.findViewById(R.id.fav_area);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
        ImageHandler.LoadImage(holder.shopAva, list.get(position).getShopAva());
        ImageHandler.loadImage2(holder.favoriteButton, ""
            ,list.get(position).isFavorited() ? R.drawable.ic_faved : R.drawable.ic_fav);
        holder.shopName.setText(list.get(position).getShopName());

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

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
        if (list.size() > 6)
            return 6;
        else
            return list.size();
    }

    public void setList(ArrayList<ProductFeedViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<ProductFeedViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
