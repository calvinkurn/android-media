package com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model;

/**
 * @author by nisie on 9/5/17.
 */

public class GenerateHostDomain {
    String uploadHost;
    String serverId;

    public GenerateHostDomain(String uploadHost, String serverId) {
        this.uploadHost = uploadHost;
        this.serverId = serverId;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public String getServerId() {
        return serverId;
    }
}
