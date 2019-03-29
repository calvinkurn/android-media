package com.tokopedia.home.beranda.domain.model.feed;

import javax.annotation.Nullable;

public class DataFeedDomain {

    private final @Nullable
    String id;

    private final @Nullable String createTime;

    private final @Nullable String type;

    private final @Nullable String cursor;

    private final @Nullable
    SourceFeedDomain source;

    private final @Nullable
    ContentFeedDomain content;

    public DataFeedDomain(@Nullable String id, @Nullable String create_time, @Nullable String type,
                          @Nullable String cursor, @Nullable SourceFeedDomain source, @Nullable ContentFeedDomain content) {
        this.id = id;
        this.createTime = create_time;
        this.type = type;
        this.cursor = cursor;
        this.source = source;
        this.content = content;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getCreateTime() {
        return createTime;
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
    public SourceFeedDomain getSource() {
        return source;
    }

    @Nullable
    public ContentFeedDomain getContent() {
        return content;
    }
}
