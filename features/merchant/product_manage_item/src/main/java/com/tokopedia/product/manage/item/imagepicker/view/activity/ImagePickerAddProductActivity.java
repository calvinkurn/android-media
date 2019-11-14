package com.tokopedia.product.manage.item.imagepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.manage.item.imagepicker.view.fragment.ImagePickerCatalogFragment;
import com.tokopedia.product.manage.item.imagepicker.view.adapter.ImagePickerViewPageAddProductAdapter;

/**
 * Created by zulfikarrahman on 6/6/18.
 */

public class ImagePickerAddProductActivity extends ImagePickerActivity implements ImagePickerCatalogFragment.ListenerImagePickerCatalog {

    public static final String EXTRA_IMAGE_PICKER_CATALOG_ID = "EXTRA_IMAGE_PICKER_CATALOG_ID";
    String catalogId;

    @Override
    public void onClickImageCatalog(String url, boolean isChecked) {
        onImageSelected(url, isChecked, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        catalogId = getIntent().getStringExtra(EXTRA_IMAGE_PICKER_CATALOG_ID);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected ImagePickerViewPagerAdapter getImagePickerViewPagerAdapter() {
        if (!TextUtils.isEmpty(catalogId)) {
            return new ImagePickerViewPageAddProductAdapter(this, getSupportFragmentManager(), imagePickerBuilder, catalogId);
        } else {
            return super.getImagePickerViewPagerAdapter();
        }
    }

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder, String catalogId) {
        Intent intent = new Intent(context, ImagePickerAddProductActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        intent.putExtra(EXTRA_IMAGE_PICKER_CATALOG_ID, catalogId);
        return intent;
    }
}
