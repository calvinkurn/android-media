package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.ProductReviewModelDB;
import com.tokopedia.core.database.model.ProductReviewModelDB_Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by stevenfredian on 2/23/16.
 */
public class ProductReviewCacheManager implements DbFlowOperation<ProductReviewModelDB> {

    private static String TAG = "CacheProductReview";
    private String productID;
    private String json;
    private long expiredTime;

    public ProductReviewCacheManager() {

    }

    public String getProductID() {
        return productID;
    }

    public ProductReviewCacheManager setProductID(String productID, String NAV) {
        this.productID = productID + NAV;
        return this;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public ProductReviewCacheManager setCacheDuration(int duration) {
        Log.i(TAG, "Storing expired time: " + (System.currentTimeMillis() + (duration * 1000)));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public ProductReviewCacheManager setResult(JSONObject result) {
        this.json = String.valueOf(result);
        return this;
    }

    public void store() {
        ProductReviewModelDB cache = new ProductReviewModelDB();
        cache.productID = this.productID;
        cache.jsonResult = this.json;
        Log.i(TAG, "Store expired time: " + this.expiredTime);
        cache.expiredTime = this.expiredTime;
        cache.save();
    }

    @Override
    public void store(ProductReviewModelDB data) {

    }

    public void delete(String key) {
        new Delete().from(ProductReviewModelDB.class).where(ProductReviewModelDB_Table.productID.is(key)).execute();
    }

    @Override
    public void deleteAll() {
        new Delete().from(ProductReviewModelDB.class).execute();
    }

    public ProductReviewCacheManager getCache(String productID, String NAV) throws RuntimeException {
        String key = productID + NAV;
        ProductReviewModelDB cache = getData(key);

        if (isExpired(cache.expiredTime)) {
            throw new RuntimeException("Cache is expired");
        } else {
            this.productID = cache.productID;
            this.json = cache.jsonResult;
            return this;
        }
    }

    public boolean isExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
        return expiredTime < System.currentTimeMillis();
    }

    @Override
    public ProductReviewModelDB getData(String key) {
        return new Select().from(ProductReviewModelDB.class)
                .where(ProductReviewModelDB_Table.productID.is(key))
                .querySingle();
    }

    @Override
    public List<ProductReviewModelDB> getDataList(String key) {
        return null;
    }

    @Override
    public String getValueString(String key) {
        return null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        return null;
    }

    public JSONObject getJson() throws JSONException {
        return new JSONObject(this.json);
    }
}