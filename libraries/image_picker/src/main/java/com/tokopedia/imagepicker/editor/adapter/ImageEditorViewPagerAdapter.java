package com.tokopedia.imagepicker.editor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.editor.main.view.ImageEditPreviewFragment;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<>();
    private ArrayList<ArrayList<String>> edittedImagePaths;

    private ArrayList<Integer> currentEditStepIndexList;
    private int minResolution;
    private ArrayList<ArrayList<ImageRatioTypeDef>> ratioTypeList;
    private boolean isCirclePreview;

    public ImageEditorViewPagerAdapter(FragmentManager fm,
                                       ArrayList<ArrayList<String>> edittedImagePaths,
                                       ArrayList<Integer> currentEditStepIndexList,
                                       int minResolution,
                                       ArrayList<ArrayList<ImageRatioTypeDef>> ratioTypeList,
                                       boolean isCirclePreview) {
        super(fm);
        this.edittedImagePaths = edittedImagePaths;
        this.currentEditStepIndexList = currentEditStepIndexList;
        this.minResolution = minResolution;
        this.ratioTypeList = ratioTypeList;
        this.isCirclePreview = isCirclePreview;
    }

    public void setEdittedImagePaths(ArrayList<ArrayList<String>> edittedImagePaths) {
        this.edittedImagePaths = edittedImagePaths;
    }

    public void setCurrentEditStepIndexList(ArrayList<Integer> currentEditStepIndexList) {
        this.currentEditStepIndexList = currentEditStepIndexList;
    }

    @Override
    public Fragment getItem(int position) {
        int currentStepPosition = currentEditStepIndexList.get(position);
        String localImagePath = edittedImagePaths.get(position).get(currentStepPosition);
        return ImageEditPreviewFragment.newInstance(position, localImagePath, minResolution, isCirclePreview);
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

    public void destroyIndex(int position){
        registeredFragments.remove(position);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return edittedImagePaths.size();
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
