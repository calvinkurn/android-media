package com.tokopedia.browse.homepage.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseMarketplaceDataCloudSource extends DataCloudSource<DigitalBrowseMarketplaceData> {

    public static final String DIGITAL_MARKETPLACE_EXAMPLE_DATA = "marketplace_example_data.json";

    private Context context;

    @Inject
    public DigitalBrowseMarketplaceDataCloudSource(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<DigitalBrowseMarketplaceData> getData(HashMap<String, Object> params) {
        DigitalBrowseMarketplaceData response = new DigitalBrowseMarketplaceData();

        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DigitalBrowseMarketplaceData>() {
        }.getType();
        DigitalBrowseMarketplaceData dataResponse = g.fromJson(loadJSONFromAsset(), dataResponseType);
        return Observable.just(dataResponse);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open(DIGITAL_MARKETPLACE_EXAMPLE_DATA);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
