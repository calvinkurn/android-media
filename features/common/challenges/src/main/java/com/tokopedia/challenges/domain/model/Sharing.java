
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Sharing {

    private MetaTags metaTags;
    private Assets assets;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public MetaTags getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(MetaTags metaTags) {
        this.metaTags = metaTags;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
