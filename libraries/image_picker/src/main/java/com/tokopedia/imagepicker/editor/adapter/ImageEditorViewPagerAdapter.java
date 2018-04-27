package com.tokopedia.imagepicker.editor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.editor.ImageEditPreviewLegacyFragment;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private ArrayList<String> originalImagePaths;
    private ArrayList<String> localImagePaths;
    private int minResolution;

    public ImageEditorViewPagerAdapter(FragmentManager fm,
                                       ArrayList<String> originalImagePaths,
                                       ArrayList<String> localImagePaths,
                                       int minResolution) {
        super(fm);
        this.originalImagePaths = originalImagePaths;
        this.localImagePaths = localImagePaths;
        this.minResolution = minResolution;
    }

    @Override
    public Fragment getItem(int position) {
        String oriImagePath = originalImagePaths.get(position);
        String localImagePath = localImagePaths.get(position);
        if (TextUtils.isEmpty(localImagePath)) {
            localImagePath = oriImagePath;
        }
        return ImageEditPreviewLegacyFragment.newInstance(oriImagePath, localImagePath, minResolution);
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
