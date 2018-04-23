package com.tokopedia.imagepicker.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.github.florent37.camerafragment.configuration.Configuration;
import com.tokopedia.imagepicker.ImagePickerBuilder;
import com.tokopedia.imagepicker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.instagram.ImagePickerInstagramFragment;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private ImagePickerBuilder imagePickerBuilder;
    private Context context;

    public ImagePickerViewPagerAdapter(Context context, FragmentManager fm, ImagePickerBuilder imagePickerBuilder) {
        super(fm);
        this.imagePickerBuilder = imagePickerBuilder;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (imagePickerBuilder.getTabTypeDef(position)) {
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY:
                return ImagePickerGalleryFragment.newInstance(
                        imagePickerBuilder.getGalleryType(),
                        imagePickerBuilder.supportMultipleSelection(),
                        imagePickerBuilder.getMinResolution());
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA:
                return ImagePickerCameraFragment.newInstance(
                        new Configuration.Builder().build()
                );

            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM:
                return ImagePickerInstagramFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        registeredFragments.put(position, (Fragment) o);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return imagePickerBuilder.getTabTypeDef().length;
    }
}
