package com.tokopedia.core.myproduct.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 *
 * this class too complicated
 */
@Deprecated
public class FolderModel {
    String path;
    List<ImageModel> imageModels;

    public FolderModel(String path, List<String> paths){
        this.path = path;
        imageModels = new ArrayList<>();
        for(String imagePath : paths){
            ImageModel imageModel = new ImageModel();
            imageModel.setPath(imagePath);
            imageModels.add(imageModel);
        }
    }

    public FolderModel(String path){
        this.path = path;
        imageModels = new ArrayList<>();
    }

    public FolderModel(){
        path = null;
        imageModels = new ArrayList<>();
    }

    public static List<ImageModel> searchImageModels(List<FolderModel> folderModels, String bucketName) {
        for (FolderModel folderModel : folderModels) {
            if (!TextUtils.isEmpty(bucketName) && folderModel.getPath().equals(bucketName)) {
                return folderModel.getImageModels();
            }
        }
        return null;
    }

    public void addAll(ImageModel imageModel){
        if(imageModel==null)
            throw new RuntimeException("unable to accept "+ImageModel.class);

        if(imageModels==null)
            throw new RuntimeException("unable to process because list is null "+ImageModel.class);

        imageModels.add(imageModel);
    }

    public void add(ImageModel imageModel){
        if(imageModel==null)
            throw new RuntimeException("unable to accept "+ImageModel.class);

        if(imageModels==null)
            throw new RuntimeException("unable to process because list is null "+ImageModel.class);

        imageModels.add(imageModel);
    }

    public List<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FolderModel that = (FolderModel) o;

        return !(path != null ? !path.equals(that.path) : that.path != null);

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
