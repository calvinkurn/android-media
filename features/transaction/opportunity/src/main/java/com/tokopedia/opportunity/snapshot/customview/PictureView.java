package com.tokopedia.opportunity.snapshot.customview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.tkpd.library.viewpagerindicator.LinePageIndicator;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.opportunity.adapter.ImagePagerAdapter;
import com.tokopedia.opportunity.snapshot.listener.SnapShotFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/1/17.
 */
public class PictureView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    ViewPager vpImage;
    LinePageIndicator indicator;

    private ImagePagerAdapter imagePagerAdapter;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        vpImage = (ViewPager) findViewById(R.id.view_pager);
        indicator = (LinePageIndicator) findViewById(R.id.indicator);
    }

    public PictureView(Context context) {
        super(context);
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_picture_product_snapshot;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {
        imagePagerAdapter = new ImagePagerAdapter(context, new ArrayList<ProductImage>());
    }

    @Override
    protected void setViewListener() {
        vpImage.setAdapter(imagePagerAdapter);
        indicator.setViewPager(vpImage);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        imagePagerAdapter = new ImagePagerAdapter(getContext(), new ArrayList<ProductImage>());
        vpImage.setAdapter(imagePagerAdapter);
        List<ProductImage> productImageList = data.getProductImages();
        if (productImageList.isEmpty()) {
            int resId = R.drawable.product_no_photo_default;
            Resources res = getContext().getResources();
            Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId));
            productImageList.add(ProductImage.Builder.aProductImage()
                    .setImageSrc300(resUri.toString())
                    .setImageDescription("").build());
            imagePagerAdapter.addAll(productImageList);
            indicator.notifyDataSetChanged();
        } else {
            imagePagerAdapter.addAll(productImageList);
            indicator.notifyDataSetChanged();
            imagePagerAdapter.setActionListener(new PictureView.PagerAdapterAction(data));
        }
    }

    public void renderTempData(ProductPass productPass) {
        ProductImage productImage = new ProductImage();
        productImage.setImageSrc300(productPass.getProductImage());
        productImage.setImageSrc(productPass.getProductImage());
        productImage.setImageDescription("");
        imagePagerAdapter.add(productImage);
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
            bundle.putString("product_name", MethodChecker.fromHtml(data.getInfo().getProductName()).toString());
            bundle.putString("product_price", MethodChecker.fromHtml(data.getInfo().getProductPrice()).toString());
            bundle.putStringArrayList("image_desc", imagePagerAdapter.getImageDescs());
            bundle.putInt("img_pos", position);
            listener.onProductPictureClicked(bundle);
        }
    }
}
