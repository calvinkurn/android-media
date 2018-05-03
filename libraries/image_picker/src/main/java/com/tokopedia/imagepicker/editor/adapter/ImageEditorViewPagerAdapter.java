package com.tokopedia.imagepicker.editor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.editor.ImageEditPreviewFragment;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private ArrayList<String> localStep0ImagePaths;
    private ArrayList<ArrayList<String>> edittedImagePaths;
    private ArrayList<Integer> currentEditStepIndexList;
    private int minResolution;

    public ImageEditorViewPagerAdapter(FragmentManager fm,
                                       ArrayList<String> localStep0ImagePaths,
                                       ArrayList<ArrayList<String>> edittedImagePaths,
                                       ArrayList<Integer> currentEditStepIndexList,
                                       int minResolution) {
        super(fm);
        this.localStep0ImagePaths = localStep0ImagePaths;
        this.edittedImagePaths = edittedImagePaths;
        this.currentEditStepIndexList = currentEditStepIndexList;
        this.minResolution = minResolution;
    }

    public void setEdittedImagePaths(ArrayList<ArrayList<String>> edittedImagePaths) {
        this.edittedImagePaths = edittedImagePaths;
    }

    public void setCurrentEditStepIndexList(ArrayList<Integer> currentEditStepIndexList) {
        this.currentEditStepIndexList = currentEditStepIndexList;
    }

    @Override
    public Fragment getItem(int position) {
        String oriImagePath = localStep0ImagePaths.get(position);
        String localImagePath = edittedImagePaths.get(position).get(currentEditStepIndexList.get(position));
        if (TextUtils.isEmpty(localImagePath)) {
            localImagePath = oriImagePath;
        }
        return ImageEditPreviewFragment.newInstance(localImagePath, minResolution);
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
        return localStep0ImagePaths.size();
    }
}
