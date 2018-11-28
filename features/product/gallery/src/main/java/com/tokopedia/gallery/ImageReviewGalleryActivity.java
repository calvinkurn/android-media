package com.tokopedia.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider;
import com.tokopedia.gallery.customview.GalleryItemDecoration;
import com.tokopedia.gallery.domain.GetImageReviewUseCase;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter;
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl;
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.ArrayList;
import java.util.List;

public class ImageReviewGalleryActivity extends BaseSimpleActivity {

    private static final String EXTRA_PRODUCT_ID = "product_id";
    private static final String EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST";
    private static final String EXTRA_DEFAULT_POSITION = "EXTRA_DEFAULT_POSITION";

    private BottomSheetImageReviewSlider bottomSheetImageReviewSlider;
    private int productId;
    private int defaultPosition;
    private ArrayList<String> imageUrlList;

    public static void moveTo(Activity activity, int productId) {
        if (activity != null) {
            Intent intent = new Intent(activity, ImageReviewGalleryActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, productId);
            activity.startActivity(intent);
        }
    }

    public static void moveTo(Context context, ArrayList<String> imageUrlList, int defaultPosition) {
        if (context != null) {
            Intent intent = new Intent(context, ImageReviewGalleryActivity.class);
            intent.putStringArrayListExtra(EXTRA_IMAGE_URL_LIST, imageUrlList);
            intent.putExtra(EXTRA_DEFAULT_POSITION, defaultPosition);
            context.startActivity(intent);
        }
    }

    @DeepLink(ApplinkConst.PRODUCT_IMAGE_REVIEW)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ImageReviewGalleryActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        bindView();
    }

    private void getDataFromIntent() {
        productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        defaultPosition = getIntent().getIntExtra(EXTRA_DEFAULT_POSITION, 0);
        imageUrlList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URL_LIST);
    }

    private void bindView() {
        bottomSheetImageReviewSlider = findViewById(R.id.bottomSheetImageSlider);
    }

    public BottomSheetImageReviewSlider getBottomSheetImageReviewSlider() {
        return bottomSheetImageReviewSlider;
    }

    public int getProductId() {
        return productId;
    }

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }

    public boolean isImageListPreloaded() {
        return imageUrlList != null && !imageUrlList.isEmpty();
    }

    @Override
    public void onBackPressed() {
        if (isImageListPreloaded() || !bottomSheetImageReviewSlider.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ImageReviewGalleryFragment.createInstance();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_review_gallery;
    }
}
