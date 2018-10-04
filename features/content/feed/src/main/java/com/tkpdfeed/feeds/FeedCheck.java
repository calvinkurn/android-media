package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class FeedCheck implements Query<FeedCheck.Data, FeedCheck.Data, FeedCheck.Variables> {
  public static final String OPERATION_DEFINITION = "query FeedCheck($userID: Int!, $cursor: String) {\n"
      + "  checkFeed(userID: $userID, cursor: $cursor) {\n"
      + "    __typename\n"
      + "    data\n"
      + "    meta {\n"
      + "      __typename\n"
      + "      total_data\n"
      + "    }\n"
      + "    links {\n"
      + "      __typename\n"
      + "      self\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final FeedCheck.Variables variables;

  public FeedCheck(int userID, @Nullable String cursor) {
    variables = new FeedCheck.Variables(userID, cursor);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public FeedCheck.Data wrapData(FeedCheck.Data data) {
    return data;
  }

  @Override
  public FeedCheck.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<FeedCheck.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final @Nullable String cursor;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, @Nullable String cursor) {
      this.userID = userID;
      this.cursor = cursor;
      this.valueMap.put("userID", userID);
      this.valueMap.put("cursor", cursor);
    }

    public int userID() {
      return userID;
    }

    public @Nullable String cursor() {
      return cursor;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private @Nullable String cursor;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder cursor(@Nullable String cursor) {
      this.cursor = cursor;
      return this;
    }

    public FeedCheck build() {
      return new FeedCheck(userID, cursor);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable CheckFeed checkFeed;

    public Data(@Nullable CheckFeed checkFeed) {
      this.checkFeed = checkFeed;
    }

    public @Nullable CheckFeed checkFeed() {
      return this.checkFeed;
    }

    @Override
    public String toString() {
      return "Data{"
        + "checkFeed=" + checkFeed
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.checkFeed == null) ? (that.checkFeed == null) : this.checkFeed.equals(that.checkFeed));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (checkFeed == null) ? 0 : checkFeed.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final CheckFeed.Mapper checkFeedFieldMapper = new CheckFeed.Mapper();

      final Field[] fields = {
        Field.forObject("checkFeed", "checkFeed", new UnmodifiableMapBuilder<String, Object>(2)
          .put("cursor", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "cursor")
          .build())
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), true, new Field.ObjectReader<CheckFeed>() {
          @Override public CheckFeed read(final ResponseReader reader) throws IOException {
            return checkFeedFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final CheckFeed checkFeed = reader.read(fields[0]);
        return new Data(checkFeed);
      }
    }

    public static class Meta {
      private final @Nullable Integer total_data;

      public Meta(@Nullable Integer total_data) {
        this.total_data = total_data;
      }

      public @Nullable Integer total_data() {
        return this.total_data;
      }

      @Override
      public String toString() {
        return "Meta{"
          + "total_data=" + total_data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Meta) {
          Meta that = (Meta) o;
          return ((this.total_data == null) ? (that.total_data == null) : this.total_data.equals(that.total_data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (total_data == null) ? 0 : total_data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Meta> {
        final Field[] fields = {
          Field.forInt("total_data", "total_data", null, true)
        };

        @Override
        public Meta map(ResponseReader reader) throws IOException {
          final Integer total_data = reader.read(fields[0]);
          return new Meta(total_data);
        }
      }
    }

    public static class Links {
      private final @Nullable String self;

      public Links(@Nullable String self) {
        this.self = self;
      }

      public @Nullable String self() {
        return this.self;
      }

      @Override
      public String toString() {
        return "Links{"
          + "self=" + self
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Links) {
          Links that = (Links) o;
          return ((this.self == null) ? (that.self == null) : this.self.equals(that.self));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (self == null) ? 0 : self.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Links> {
        final Field[] fields = {
          Field.forString("self", "self", null, true)
        };

        @Override
        public Links map(ResponseReader reader) throws IOException {
          final String self = reader.read(fields[0]);
          return new Links(self);
        }
      }
    }

    public static class CheckFeed {
      private final @Nullable String data;

      private final @Nullable Meta meta;

      private final @Nullable Links links;

      public CheckFeed(@Nullable String data, @Nullable Meta meta, @Nullable Links links) {
        this.data = data;
        this.meta = meta;
        this.links = links;
      }

      public @Nullable String data() {
        return this.data;
      }

      public @Nullable Meta meta() {
        return this.meta;
      }

      public @Nullable Links links() {
        return this.links;
      }

      @Override
      public String toString() {
        return "CheckFeed{"
          + "data=" + data + ", "
          + "meta=" + meta + ", "
          + "links=" + links
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof CheckFeed) {
          CheckFeed that = (CheckFeed) o;
          return ((this.data == null) ? (that.data == null) : this.data.equals(that.data))
           && ((this.meta == null) ? (that.meta == null) : this.meta.equals(that.meta))
           && ((this.links == null) ? (that.links == null) : this.links.equals(that.links));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        h *= 1000003;
        h ^= (meta == null) ? 0 : meta.hashCode();
        h *= 1000003;
        h ^= (links == null) ? 0 : links.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<CheckFeed> {
        final Meta.Mapper metaFieldMapper = new Meta.Mapper();

        final Links.Mapper linksFieldMapper = new Links.Mapper();

        final Field[] fields = {
          Field.forString("data", "data", null, true),
          Field.forObject("meta", "meta", null, true, new Field.ObjectReader<Meta>() {
            @Override public Meta read(final ResponseReader reader) throws IOException {
              return metaFieldMapper.map(reader);
            }
          }),
          Field.forObject("links", "links", null, true, new Field.ObjectReader<Links>() {
            @Override public Links read(final ResponseReader reader) throws IOException {
              return linksFieldMapper.map(reader);
            }
          })
        };

        @Override
        public CheckFeed map(ResponseReader reader) throws IOException {
          final String data = reader.read(fields[0]);
          final Meta meta = reader.read(fields[1]);
          final Links links = reader.read(fields[2]);
          return new CheckFeed(data, meta, links);
        }
      }
    }
  }
}
