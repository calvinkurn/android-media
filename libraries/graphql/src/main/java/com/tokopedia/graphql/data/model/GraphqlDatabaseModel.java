package com.tokopedia.graphql.data.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = DbMetadata.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class GraphqlDatabaseModel extends BaseModel {

    @PrimaryKey
    @Column
    public String key;

    @Column
    public String value;

    @Column
    public long expiredTime = 0;

    public GraphqlDatabaseModel() {
    }

    private GraphqlDatabaseModel(Builder builder) {
        key = builder.key;
        value = builder.value;
        expiredTime = builder.expiredTime;
    }


    public static final class Builder {
        private String key;
        private String value;
        private long expiredTime;

        public Builder() {
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder expiredTime(long val) {
            expiredTime = val;
            return this;
        }

        public GraphqlDatabaseModel build() {
            return new GraphqlDatabaseModel(this);
        }
    }
}
