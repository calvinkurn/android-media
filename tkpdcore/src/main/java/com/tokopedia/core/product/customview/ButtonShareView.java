package com.tokopedia.core.product.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.core.R;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.share.ShareData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Angga.Prasetiyo on 30/10/2015.
 */
public class ButtonShareView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ButtonShareView.class.getSimpleName();
    private Bitmap bitmap;

    public ButtonShareView(Context context) {
        super(context);
    }

    public ButtonShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_share_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        if (bitmap != null) {
            setVisibility(VISIBLE);
            setOnClickListener(new OnClicked(data));
        } else {
            new BitmapTask(data, getContext()).execute();
        }
    }

    private class OnClicked implements OnClickListener {
        private final ProductDetailData data;

        public OnClicked(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            ShareData shareData = ShareData.Builder.aShareData()
                    .setName(data.getInfo().getProductName())
                    .setDescription(data.getInfo().getProductDescription())
                    .setImgUri(data.getProductImages().get(0).getImageSrc())
                    .setPrice(data.getInfo().getProductPrice())
                    .setUri(data.getInfo().getProductUrl())
                    .setType(ShareData.PRODUCT_TYPE)
                    .build();
            listener.onProductShareClicked(shareData);
        }
    }

    private class BitmapTask extends AsyncTask<String, String, Bitmap> {

        private final ProductDetailData data;
        private Context context;

        public BitmapTask(ProductDetailData data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                List<ProductImage> images = data.getProductImages();
                if (!images.isEmpty()) {
                    String uriImage = images.get(0).getImageSrc300();
                    if (!uriImage.isEmpty())
                        try {
                            return Glide.with(context)
                                    .load(uriImage)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(500, 500)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            if (s != null) {
                bitmap = s;
                setVisibility(VISIBLE);
                setOnClickListener(new OnClicked(data));
            }
        }
    }
}
