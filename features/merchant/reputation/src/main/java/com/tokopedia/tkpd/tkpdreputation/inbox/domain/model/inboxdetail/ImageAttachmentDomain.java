package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class ImageAttachmentDomain {

    private int attachmentId;
    private String description;
    private String uriThumbnail;
    private String uriLarge;

    public ImageAttachmentDomain(int attachmentId, String description,
                                 String uriThumbnail, String uriLarge) {
        this.attachmentId = attachmentId;
        this.description = description;
        this.uriThumbnail = uriThumbnail;
        this.uriLarge = uriLarge;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }
}
