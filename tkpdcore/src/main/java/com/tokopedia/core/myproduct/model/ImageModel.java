package com.tokopedia.core.myproduct.model;

import android.os.Parcelable;

import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.model.constant.ImageModelType;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 03/12/2015.
 *
 * This class is too complicated
 */
@Deprecated
@Parcel
public class ImageModel {
    // this is for url / path from sdcard
    String path;

    // this is for drawable
    int resId;

    //this is for db - special case
    long dbId;

    // this is for state such as active, highlight and other
    /**
     * please refer to
     * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#ACTIVE}
     * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#INACTIVE}
     * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#SELECTED}
     */
    int type = ImageModelType.INACTIVE.getType();

    ArrayList<Integer> types = new ArrayList<>();

    public ImageModel(String path, int resId, long dbId){
        setPath(path);
        setResId(resId);
        setDbId(dbId);
    }

    public ImageModel(ImageModel imageModel){
        setPath(imageModel.getPath());
        setResId(imageModel.getResId());
        setDbId(imageModel.getDbId());
    }

    public ImageModel(int resId){
        this(null, resId, 0);
    }

    public ImageModel(String path) {
        this(path, 0, 0);
    }

    public ImageModel(){
    }

    public static ImageModelType getTypeEnum(int type) {
        if (type == ImageModelType.ACTIVE.getType()) {
            return ImageModelType.ACTIVE;
        } else if (type == ImageModelType.INACTIVE.getType()) {
            return ImageModelType.INACTIVE;
        } else {//if(type == ImageModelType.SELECTED.getType()){
            return ImageModelType.SELECTED;
        }
    }

    public static int calculateDefaults(List<ImageModel> imageModels) {
        int count = 0;
        for (ImageModel temp :
                imageModels) {
            if (temp.isDefault()) {
                count++;
            }
        }
        return count;
    }

    public static List<Integer> determineDefault(List<ImageModel> imageModels) {
        List<Integer> result = new ArrayList<>();
        int count = 0;
        for (ImageModel temp :
                imageModels) {
            if (temp.isDefault()) {
                result.add(count);
            }
            count++;
        }
        return result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isDefault(){
        return resId != 0;

    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public ImageModelType getTypeEnum(){
        return getTypeEnum(type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {

        if(types.contains(type))
            return;

        this.type = type;

        types.add(type);
    }

    public ArrayList<Integer> getTypes() {
        return types;
    }

    public void clearAll() {
        if (types != null)
            types.clear();
    }

    public PictureDB getGambar(){
        return DbManagerImpl.getInstance().getGambarById(dbId);
    }

    @Override
    public String toString() {
        String formatHere = "ImageModel{ path='%s' db = '%s'}";
        return String.format(formatHere, path, DbManagerImpl.getInstance().getGambarById(dbId));
    }

    /**
     * use this for specific object
     */
    public static class ImageModelParc implements Parcelable {
        public static final Parcelable.Creator<ImageModelParc> CREATOR = new Parcelable.Creator<ImageModelParc>() {
            @Override
            public ImageModelParc createFromParcel(android.os.Parcel source) {
                return new ImageModelParc(source);
            }

            @Override
            public ImageModelParc[] newArray(int size) {
                return new ImageModelParc[size];
            }
        };
        // this is for url / path from sdcard
        String path;
        // this is for drawable
        int resId;

        // this is for state such as active, highlight and other
        //this is for db - special case
        long dbId;
        /**
         * please refer to
         * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#ACTIVE}
         * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#INACTIVE}
         * {@link com.tokopedia.core.myproduct.model.constant.ImageModelType#SELECTED}
         */
        int type = ImageModelType.INACTIVE.getType();
        ArrayList<Integer> types = new ArrayList<>();

        public ImageModelParc() {
        }

        protected ImageModelParc(android.os.Parcel in) {
            this.path = in.readString();
            this.resId = in.readInt();
            this.dbId = in.readLong();
            this.type = in.readInt();
            this.types = new ArrayList<Integer>();
            in.readList(this.types, Integer.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.path);
            dest.writeInt(this.resId);
            dest.writeLong(this.dbId);
            dest.writeInt(this.type);
            dest.writeList(this.types);
        }
    }
}
