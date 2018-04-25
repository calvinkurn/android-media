package com.tokopedia.imagepicker.editor.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.github.florent37.camerafragment.configuration.Configuration;
import com.tokopedia.imagepicker.editor.ImageEditPreviewFragment;
import com.tokopedia.imagepicker.picker.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.camera.ImagePickerCameraFragment;
import com.tokopedia.imagepicker.picker.gallery.ImagePickerGalleryFragment;
import com.tokopedia.imagepicker.picker.instagram.ImagePickerInstagramFragment;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private ArrayList<String> originalImagePaths;
    private ArrayList<String> localImagePaths;

    public ImageEditorViewPagerAdapter(FragmentManager fm,
                                       ArrayList<String> originalImagePaths,
                                       ArrayList<String> localImagePaths) {
        super(fm);
        this.originalImagePaths = originalImagePaths;
        this.localImagePaths = localImagePaths;
    }

    @Override
    public Fragment getItem(int position) {
        String oriImagePath = originalImagePaths.get(position);
        String localImagePath = localImagePaths.get(position);
        if (TextUtils.isEmpty(localImagePath)) {
            localImagePath = oriImagePath;
        }
        return ImageEditPreviewFragment.newInstance(oriImagePath, localImagePath);
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
        return originalImagePaths.size();
    }
}
