package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.container.ProductDbContainer;
import com.tokopedia.core.database.DatabaseConstant;

/**
 * Created by m.normansyah on 12/27/15.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class PictureDB extends BaseModel implements DatabaseConstant {

    public static final int NOT_PRIMARY_IMAGE = 0;
    public static final int PRIMARY_IMAGE = 1;
    public static final int NOT_SYNC_TO_SERVER = -1;
    public static final String GAMBAR_PRODUCT = "gambar_product";

    private ProductDB productDB;

    public PictureDB() {
    }

    public PictureDB(String path, int width, int height) {
        this(path, width, height, NOT_SYNC_TO_SERVER, NOT_PRIMARY_IMAGE, null, null);
    }

    public PictureDB(String path, int width, int height, int pictureId, int picturePrimary, String pictureThumbnailUrl, String pictureImageSourceUrl) {
        super();
        this.path = path;
        this.width = width;
        this.height = height;
        this.pictureId = pictureId;
        this.picturePrimary = picturePrimary;
        this.pictureThumbnailUrl = pictureThumbnailUrl;
        this.pictureImageSourceUrl = pictureImageSourceUrl;
    }

    public PictureDB(String path, int width, int height, int pictureId, int picturePrimary, String pictureThumbnailUrl, String pictureImageSourceUrl, String pictureDescription) {
        super();
        this.path = path;
        this.width = width;
        this.height = height;
        this.pictureId = pictureId;
        this.picturePrimary = picturePrimary;
        this.pictureThumbnailUrl = pictureThumbnailUrl;
        this.pictureImageSourceUrl = pictureImageSourceUrl;
        this.pictureDescription = pictureDescription;
    }

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    ProductDbContainer productDbContainer;

    @Column
    public String path;
    @Column
    public int width;
    @Column
    public int height;
    @Column
    public int pictureId;
    @Column
    public int picturePrimary = 0;
    @Column
    public String pictureThumbnailUrl;
    @Column
    public String pictureImageSourceUrl;
    @Column
    public String pictureDescription;

    public void linkToProduct(ProductDB productDb) {
        productDbContainer = createProductDbContainer(productDb);
    }

    public static ProductDbContainer createProductDbContainer(ProductDB productDb) {
        return new ProductDbContainer(FlowManager.getContainerAdapter(ProductDB.class)
                .toForeignKeyContainer(productDb));
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public ProductDB getProductDB() {
        if (productDbContainer != null)
            return productDbContainer.toModel();
        return productDB;
    }

    public void setProductDB(ProductDB productDB) {
        linkToProduct(productDB);
        this.productDB = productDB;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public int getPicturePrimary() {
        return picturePrimary;
    }

    public void setPicturePrimary(int picturePrimary) {
        this.picturePrimary = picturePrimary;
    }

    public String getPictureThumbnailUrl() {
        return pictureThumbnailUrl;
    }

    public void setPictureThumbnailUrl(String pictureThumbnailUrl) {
        this.pictureThumbnailUrl = pictureThumbnailUrl;
    }

    public String getPictureImageSourceUrl() {
        return pictureImageSourceUrl;
    }

    public void setPictureImageSourceUrl(String pictureImageSourceUrl) {
        this.pictureImageSourceUrl = pictureImageSourceUrl;
    }

    @Override
    public String toString() {
        return "Gambar{" +
                "path='" + path + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", pictureId=" + pictureId +
                ", picturePrimary=" + picturePrimary +
                ", pictureThumbnailUrl='" + pictureThumbnailUrl + '\'' +
                ", pictureImageSourceUrl='" + pictureImageSourceUrl + '\'' +
                '}';
    }
}
