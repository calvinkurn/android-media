package com.tokopedia.imagepicker.picker.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.collection.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.instagram.view.fragment.ImagePickerInstagramFragment;
import com.tokopedia.imagepicker.picker.video.VideoRecorderFragment;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    protected ImagePickerBuilder imagePickerBuilder;
    protected Context context;

    public ImagePickerViewPagerAdapter(Context context, FragmentManager fm, ImagePickerBuilder imagePickerBuilder) {
        super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.imagePickerBuilder = imagePickerBuilder;
        this.context = context;
    }

    // Note: permission will be handled in activity, before the adapter is attached.
    @Override
    public Fragment getItem(int position) {
        switch (imagePickerBuilder.getImagePickerTab()[position]) {
            case TYPE_GALLERY:
                return createGalleryFragment();
            case TYPE_CAMERA:
                return createCameraFragment();
            case TYPE_INSTAGRAM:
                return createInstagramFragment();
            case TYPE_RECORDER:
                return createVideoFragment();
            default:
                return new Fragment();
        }
    }

    @SuppressLint("MissingPermission")
    protected Fragment createGalleryFragment(){
        return ImagePickerGalleryFragment.newInstance(
                imagePickerBuilder.getGalleryType(),
                imagePickerBuilder.supportMultipleSelection() ,
                imagePickerBuilder.getMinResolution(),
                imagePickerBuilder.getBelowMinResolutionErrorMessage(),
                imagePickerBuilder.getImageTooLargeErrorMessage());
    }

    @SuppressLint("MissingPermission")
    protected Fragment createCameraFragment(){
        return ImagePickerCameraFragment.newInstance();
    }

    @SuppressLint("MissingPermission")
    protected Fragment createInstagramFragment(){
        return ImagePickerInstagramFragment.newInstance(
                imagePickerBuilder.supportMultipleSelection() ,
                imagePickerBuilder.getMinResolution());
    }

    protected Fragment createVideoFragment(){
        return new VideoRecorderFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (imagePickerBuilder.getImagePickerTab()[position]) {
            case TYPE_GALLERY:
                return context.getString(R.string.gallery);
            case TYPE_CAMERA:
                return context.getString(R.string.camera);
            case TYPE_INSTAGRAM:
                return context.getString(R.string.instagram);
            case TYPE_RECORDER:
                return context.getString(R.string.recorder);
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
        return imagePickerBuilder.getImagePickerTab().length;
    }

    public void destroyAllIndex(){
        registeredFragments.clear();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object!= null) {
            if (registeredFragments.indexOfValue((Fragment)object) < 0) {
                return POSITION_NONE;
            } else {
                return super.getItemPosition(object);
            }
        }
        return super.getItemPosition(object);
    }
}
