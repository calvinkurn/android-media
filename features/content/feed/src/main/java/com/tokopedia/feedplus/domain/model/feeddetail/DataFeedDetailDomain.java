package com.tokopedia.feedplus.domain.model.feeddetail;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class DataFeedDetailDomain {

    private final
    @Nullable
    String id;

    private final
    @Nullable
    String create_time;

    private final
    @Nullable
    String type;

    private final
    @Nullable
    String cursor;

    private final
    @Nullable
    FeedDetailSourceDomain source;

    private final
    @Nullable
    FeedDetailContentDomain content;

    private final
    @Nullable
    FeedDetailMetaDomain meta;

    public DataFeedDetailDomain(String id,
                                String create_time,
                                String type,
                                String cursor,
                                FeedDetailSourceDomain source,
                                FeedDetailContentDomain content,
                                FeedDetailMetaDomain meta) {
        this.id = id;
        this.create_time = create_time;
        this.type = type;
        this.cursor = cursor;
        this.source = source;
        this.content = content;
        this.meta = meta;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getCreate_time() {
        return create_time;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getCursor() {
        return cursor;
    }

    @Nullable
    public FeedDetailSourceDomain getSource() {
        return source;
    }

    @Nullable
    public FeedDetailContentDomain getContent() {
        return content;
    }

    @Nullable
    public FeedDetailMetaDomain getMeta() {
        return meta;
    }
}
