
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private String image;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
