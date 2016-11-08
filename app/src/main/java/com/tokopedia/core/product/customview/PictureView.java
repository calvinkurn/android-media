package com.tokopedia.core.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.AttributeSet;

import com.tkpd.library.viewpagerindicator.LinePageIndicator;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.adapter.ImagePagerAdapter;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 29/10/2015.
 */
public class PictureView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = PictureView.class.getSimpleName();

    @Bind(R2.id.view_pager)
    ViewPager vpImage;
    @Bind(R2.id.indicator)
    LinePageIndicator indicator;

    private ImagePagerAdapter imagePagerAdapter;

    public PictureView(Context context) {
        super(context);
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_picture_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {
        imagePagerAdapter = new ImagePagerAdapter(context, new ArrayList<ProductImage>());
    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
        vpImage.setAdapter(imagePagerAdapter);
        indicator.setViewPager(vpImage);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        List<ProductImage> productImageList = data.getProductImages();
        if (productImageList.isEmpty()) {
            productImageList.add(ProductImage.Builder.aProductImage()
                    .setImageSrc300("android.resource://" +
                            BuildConfig.APPLICATION_ID +
                            "/drawable/product_no_photo_default")
                    .setImageDescription("").build());
        }
        imagePagerAdapter.addAll(productImageList);
        indicator.notifyDataSetChanged();
        setVisibility(VISIBLE);
        imagePagerAdapter.setActionListener(new PagerAdapterAction(data));
    }

    public void renderTempData(ProductPass productPass) {
        ProductImage productImage = new ProductImage();
        productImage.setImageSrc300(productPass.getProductImage());
        productImage.setImageDescription("");
        indicator.notifyDataSetChanged();
    }

    private class PagerAdapterAction implements ImagePagerAdapter.OnActionListener {
        private final ProductDetailData data;

        public PagerAdapterAction(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onItemImageClicked(ProductImage productImage, int position) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("fileloc", imagePagerAdapter.getImageURIPaths());
            bundle.putString("product_name", Html.fromHtml(data.getInfo().getProductName()).toString());
            bundle.putString("product_price", Html.fromHtml(data.getInfo().getProductPrice()).toString());
            bundle.putStringArrayList("image_desc", imagePagerAdapter.getImageDescs());
            bundle.putInt("img_pos", position);
            listener.onProductPictureClicked(bundle);
        }
    }
}
