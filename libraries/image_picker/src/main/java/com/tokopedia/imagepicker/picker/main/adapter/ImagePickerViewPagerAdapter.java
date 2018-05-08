package com.tokopedia.imagepicker.picker.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.ImagePickerInstagramFragment;
import com.tokopedia.imagepicker.picker.main.util.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.util.ImagePickerTabTypeDef;

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

    // Note: camera permission will be handled in activity, before the adapter is attached.
    @SuppressLint("MissingPermission")
    @Override
    public Fragment getItem(int position) {
        switch (imagePickerBuilder.getTabTypeDef(position)) {
            case ImagePickerTabTypeDef.TYPE_GALLERY:
                return ImagePickerGalleryFragment.newInstance(
                        imagePickerBuilder.getGalleryType(),
                        imagePickerBuilder.supportMultipleSelection() ,
                        imagePickerBuilder.getMinResolution());
            case ImagePickerTabTypeDef.TYPE_CAMERA:
                return ImagePickerCameraFragment.newInstance();
            case ImagePickerTabTypeDef.TYPE_INSTAGRAM:
                return ImagePickerInstagramFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (imagePickerBuilder.getTabTypeDef(position)) {
            case ImagePickerTabTypeDef.TYPE_GALLERY:
                return context.getString(R.string.gallery);
            case ImagePickerTabTypeDef.TYPE_CAMERA:
                return context.getString(R.string.camera);
            case ImagePickerTabTypeDef.TYPE_INSTAGRAM:
                return context.getString(R.string.instagram);
            default:
                return context.getString(R.string.gallery);
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
