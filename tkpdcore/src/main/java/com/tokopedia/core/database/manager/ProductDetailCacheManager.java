package com.tokopedia.core.database.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.ProductDetailModelDB;
import com.tokopedia.core.database.model.ProductDetailModelDB_Table;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import java.util.List;

/**
 * Created by ricoharisin on 12/3/15.
 */
public class ProductDetailCacheManager implements DbFlowOperation<ProductDetailModelDB> {
    private static String TAG = "CacheProductDetail";
    private String productID;
    private String productData;
    private long expiredTime;

    public ProductDetailCacheManager() {

    }


    public String getProductID() {
        return productID;
    }

    public ProductDetailCacheManager setProductID(String productID) {
        this.productID = productID;

        return this;
    }

    public ProductDetailCacheManager setProductData(ProductDetailData data) {
        Gson gson = new Gson();
        this.productData = gson.toJson(data);

        return this;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public ProductDetailCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void store() {
        ProductDetailModelDB cache = new ProductDetailModelDB();
        cache.productID = this.productID;
        cache.productData = this.productData;
        Log.i(TAG, "Store expired time: " + this.expiredTime);
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    @Override
    public void store(ProductDetailModelDB data) {

    }

    public void delete(String key) {
        new Delete().from(ProductDetailModelDB.class).where(ProductDetailModelDB_Table.productID.is(key)).execute();
    }

    @Override
    public void deleteAll() {
        new Delete().from(ProductDetailModelDB.class).execute();
    }

    public String getValueString(String productID) throws RuntimeException {
        ProductDetailModelDB cache = new Select().from(ProductDetailModelDB.class)
                .where(ProductDetailModelDB_Table.productID.is(productID))
                .querySingle();

        if (cache!=null && isExpired(cache.expiredTime)) {
            throw new RuntimeException("Cache is expired");
        } else {
            this.productData = cache.productData;
            return productData;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        Gson gson = new Gson();
        return (Z) gson.fromJson(getValueString(key), ProductDetailData.class);
    }

    public boolean isExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    @Override
    public ProductDetailModelDB getData(String key) {
        return null;
    }

    @Override
    public List<ProductDetailModelDB> getDataList(String key) {
        return null;
    }
}