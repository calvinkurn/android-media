package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

/**
 * Created by zulfikarrahman on 8/1/17.
 */

public class TopAdsNewProductListViewHolder extends BaseViewHolder<TopAdsProductViewModel> {
    private ImageView image_product;
    private TextView name_product;
    private ImageView image_delete;
    private final DeleteListener deleteListener;

    public TopAdsNewProductListViewHolder(View itemView, DeleteListener deleteListener) {
        super(itemView);
        image_product = (ImageView) itemView.findViewById(R.id.image_product);
        name_product = (TextView) itemView.findViewById(R.id.name_product);
        image_delete = (ImageView) itemView.findViewById(R.id.delete_product);
        this.deleteListener = deleteListener;
    }

    @Override
    public void bindObject(TopAdsProductViewModel topAdsProductViewModel) {
        ImageHandler.loadImageRounded2(
                image_product.getContext(),
                image_product,
                topAdsProductViewModel.getImageUrl()
        );
        name_product.setText(topAdsProductViewModel.getName());
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteListener != null) {
                    deleteListener.onDelete(getLayoutPosition());
                }
            }
        });
    }

    public interface DeleteListener{
        void onDelete(int position);
    }
}
