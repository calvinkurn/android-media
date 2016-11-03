package com.tokopedia.tkpd.database.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.tkpd.database.DbFlowOperation;
import com.tokopedia.tkpd.database.model.ProductOtherModelDB;
import com.tokopedia.tkpd.database.model.ProductOtherModelDB_Table;
import com.tokopedia.tkpd.product.model.productother.ProductOther;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 21/01/2016.
 */
public class ProductOtherCacheManager implements DbFlowOperation<ProductOtherModelDB> {
    private static final String TAG = ProductOtherCacheManager.class.getSimpleName();

    private String productID;
    private List<ProductOther> productOthers;
    private long expiredTime;

    public ProductOtherCacheManager() {

    }

    public String getProductID() {
        return productID;
    }

    public ProductOtherCacheManager setProductID(String productID) {
        this.productID = productID;
        return this;
    }

    public ProductOtherCacheManager setData(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
        return this;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public ProductOtherCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void store() {
        ProductOtherModelDB cache = new ProductOtherModelDB();
        cache.setProductId(this.productID);
        cache.setData(new Gson().toJson(this.productOthers));
        Log.i(TAG, "Store expired time: " + this.expiredTime);
        cache.setExpiredTime(this.expiredTime);
        cache.save();
    }

    @Override
    public void store(ProductOtherModelDB data) {

    }

    public void delete(String key) {
        new Delete().from(ProductOtherModelDB.class).where(ProductOtherModelDB_Table.productId.is(key)).execute();
    }

    @Override
    public void deleteAll() {
        new Delete().from(ProductOtherModelDB.class).execute();
    }

    public String getValueString(String productID) throws RuntimeException {
        ProductOtherModelDB cache = new Select().from(ProductOtherModelDB.class)
                .where(ProductOtherModelDB_Table.productId.is(productID))
                .querySingle();
        if (isExpired(cache.getExpiredTime())) {
            throw new RuntimeException("Cache is expired");
        } else {
            this.productOthers = Arrays.asList(new Gson().fromJson(cache.getData(), ProductOther[].class));
            return cache.getData();
        }
    }

    public List<ProductOther> getData() {
        return productOthers;
    }

    public boolean isExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    @Override
    public ProductOtherModelDB getData(String key) {
        return null;
    }

    @Override
    public List<ProductOtherModelDB> getDataList(String key) {
        return null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        return null;
    }

    public <T> List<T> getConvertObjDataList(String key, Class<T[]> clazz) {
        return Arrays.asList(new Gson().fromJson(getValueString(key), clazz));
    }
}