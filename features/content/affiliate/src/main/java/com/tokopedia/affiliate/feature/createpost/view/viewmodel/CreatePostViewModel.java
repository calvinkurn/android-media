package com.tokopedia.affiliate.feature.createpost.view.viewmodel;

import java.util.ArrayList;

public class CreatePostViewModel {
    private String pdpImage;
    private ArrayList<String> imageList;

    public CreatePostViewModel() {
        imageList = new ArrayList<>();
    }

    public void setPdpImage(String pdpImage) {
        this.pdpImage = pdpImage;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<String> getCompleteList() {
        ArrayList<String> completeList = new ArrayList<>(imageList);
        completeList.add(pdpImage);
        return completeList;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }
}
