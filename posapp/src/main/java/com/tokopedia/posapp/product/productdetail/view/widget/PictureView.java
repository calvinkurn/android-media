package com.tokopedia.posapp.product.productdetail.view.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.R;
import com.tokopedia.tkpdpdp.adapter.ImagePagerAdapter;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

/**
 * Created by yoshua on 17/04/18.
 */

public class PictureView extends BaseView<ProductDetailData, ProductDetailView> {

    private ViewPager vpImage;
    private CirclePageIndicator indicator;
    private LinearLayout errorProductContainer;
    private TextView errorProductTitle;
    private TextView errorProductSubitle;

    private ImagePagerAdapter imagePagerAdapter;
    private String urlTemporary;

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
        return R.layout.view_picture_posapp;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        vpImage = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.indicator_picture);
        errorProductContainer = findViewById(R.id.error_product_container);
        errorProductTitle = findViewById(R.id.error_product_title);
        errorProductSubitle = findViewById(R.id.error_product_subtitle);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        imagePagerAdapter = new ImagePagerAdapter(listener.getActivityContext(), urlTemporary);
        vpImage.setAdapter(imagePagerAdapter);
        indicator.setViewPager(vpImage);
        List<ProductImage> productImageList = data.getProductImages();
        if (productImageList.isEmpty()) {
            int resId = com.tokopedia.tkpdpdp.R.drawable.product_no_photo_default;
            Resources res = getContext().getResources();
            Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId));
            productImageList.add(ProductImage.Builder.aProductImage()
                    .setImageSrc300(resUri.toString())
                    .setImageDescription("").build());
            imagePagerAdapter.addAllWithoutSort(productImageList);
            indicator.notifyDataSetChanged();
        } else {
            imagePagerAdapter.addAllWithoutSort(productImageList);
            indicator.notifyDataSetChanged();
            imagePagerAdapter.setActionListener(new PictureView.PagerAdapterAction());
        }
    }

    public void renderTempData(ProductPass productPass) {
        imagePagerAdapter = new ImagePagerAdapter(listener.getActivityContext(), urlTemporary);
        vpImage.setAdapter(imagePagerAdapter);
        indicator.setViewPager(vpImage);
        ProductImage productImage = new ProductImage();
        productImage.setImageSrc300(productPass.getProductImage());
        productImage.setImageSrc(productPass.getProductImage());
        productImage.setImageDescription("");
        imagePagerAdapter.addFirst(productImage);
        indicator.notifyDataSetChanged();
        urlTemporary = productPass.getProductImage();
    }

    private class PagerAdapterAction implements ImagePagerAdapter.OnActionListener {

        PagerAdapterAction() {}

        @Override
        public void onItemImageClicked(int position) {
            listener.onImageZoomClick(position);
        }
    }
}
