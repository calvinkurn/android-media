package com.tokopedia.imagepicker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.ImagePickerBuilder;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private @ImagePickerBuilder.ImagePickerTabTypeDef
    int[] tabTypeDef;

    public ImagePickerViewPagerAdapter(FragmentManager fm,
                                       @ImagePickerBuilder.ImagePickerTabTypeDef int[] tabTypeDef) {
        super(fm);
        this.tabTypeDef = tabTypeDef;
    }

    @Override
    public Fragment getItem(int position) {
        switch (tabTypeDef[position]) {
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_GALLERY:
                return new Fragment();
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_CAMERA:
                return new Fragment();
            case ImagePickerBuilder.ImagePickerTabTypeDef.TYPE_INSTAGRAM:
                return new Fragment();
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
        return tabTypeDef.length;
    }
}
