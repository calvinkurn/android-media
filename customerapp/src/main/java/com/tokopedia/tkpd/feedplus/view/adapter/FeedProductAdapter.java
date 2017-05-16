package com.tokopedia.tkpd.feedplus.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/16/17.
 */

public class FeedProductAdapter extends RecyclerView.Adapter<FeedProductAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mainContent;
        public LinearLayout badgesContainer;
        public FlowLayout labelsContainer;
        public TextView productName;
        public TextView productPrice;
        public TextView shopName;
        public TextView shopLocation;
        public ImageView productImage;
        public View grosir;
        public View preorder;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mainContent = (RelativeLayout) itemLayoutView.findViewById(R.id.container);
            badgesContainer = (LinearLayout) itemLayoutView.findViewById(R.id.badges_container);
            labelsContainer = (FlowLayout) itemLayoutView.findViewById(R.id.label_container);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            shopLocation = (TextView) itemLayoutView.findViewById(R.id.location);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            grosir = itemLayoutView.findViewById(R.id.grosir);
            preorder = itemLayoutView.findViewById(R.id.preorder);
        }
    }

    ArrayList<ProductFeedViewModel> list;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_product_item_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<ProductFeedViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<ProductFeedViewModel> getList() {
        return list;
    }


}
