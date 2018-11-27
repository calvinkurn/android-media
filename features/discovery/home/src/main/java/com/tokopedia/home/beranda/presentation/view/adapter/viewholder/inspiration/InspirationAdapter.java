package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.inspiration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationProductViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

import java.util.ArrayList;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class InspirationAdapter extends RecyclerView.Adapter<InspirationAdapter.ViewHolder> {

    private final HomeFeedListener viewListener;
    private InspirationViewModel inspirationViewModel;
    private Context context;

    public InspirationAdapter(Context context, HomeFeedListener viewListener) {
        this.viewListener = viewListener;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomePageTracking.eventEnhancedClickProductHomePage(
                            context,
                            inspirationViewModel.getHomePageClickDataLayer(getAdapterPosition())
                    );
                    viewListener.onGoToProductDetailFromInspiration(
                            String.valueOf(inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getProductId()),
                            inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getImageSource(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getName(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPrice());
                }
            });
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomePageTracking.eventEnhancedClickProductHomePage(
                            context,
                            inspirationViewModel.getHomePageClickDataLayer(getAdapterPosition())
                    );
                    viewListener.onGoToProductDetailFromInspiration(
                            String.valueOf(inspirationViewModel.getListProduct().get(getAdapterPosition()).getProductId()),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getImageSource(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getName(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPrice());
                    HomeTrackingUtils.homepageRecommedationClicked(context,
                            inspirationViewModel.getListProduct().get(getAdapterPosition()));

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inspiration_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(inspirationViewModel.getListProduct().get(position).getName()));
        holder.productPrice.setText(inspirationViewModel.getListProduct().get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, inspirationViewModel.getListProduct().get(position).getImageSource());
    }

    @Override
    public int getItemCount() {
        if (inspirationViewModel != null
                && inspirationViewModel.getListProduct() != null
                && !inspirationViewModel.getListProduct().isEmpty()) {
            if (inspirationViewModel.getListProduct().size() > 6)
                return 6;
            else
                return inspirationViewModel.getListProduct().size();
        } else {
            return 0;
        }
    }

    public void setData(InspirationViewModel inspirationViewModel) {
        this.inspirationViewModel = inspirationViewModel;
        notifyDataSetChanged();
    }

    public ArrayList<InspirationProductViewModel> getList() {
        return inspirationViewModel.getListProduct();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
