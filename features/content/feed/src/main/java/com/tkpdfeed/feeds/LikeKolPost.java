package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
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
public final class LikeKolPost implements Mutation<LikeKolPost.Data, LikeKolPost.Data, LikeKolPost.Variables> {
  public static final String OPERATION_DEFINITION = "mutation LikeKolPost($idPost: Int!, $action: Int!) {\n"
      + "  do_like_kol_post(idPost: $idPost, action: $action) {\n"
      + "    __typename\n"
      + "    error\n"
      + "    data {\n"
      + "      __typename\n"
      + "      success\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final LikeKolPost.Variables variables;

  public LikeKolPost(int idPost, int action) {
    variables = new LikeKolPost.Variables(idPost, action);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public LikeKolPost.Data wrapData(LikeKolPost.Data data) {
    return data;
  }

  @Override
  public LikeKolPost.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<LikeKolPost.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int idPost;

    private final int action;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int idPost, int action) {
      this.idPost = idPost;
      this.action = action;
      this.valueMap.put("idPost", idPost);
      this.valueMap.put("action", action);
    }

    public int idPost() {
      return idPost;
    }

    public int action() {
      return action;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int idPost;

    private int action;

    Builder() {
    }

    public Builder idPost(int idPost) {
      this.idPost = idPost;
      return this;
    }

    public Builder action(int action) {
      this.action = action;
      return this;
    }

    public LikeKolPost build() {
      return new LikeKolPost(idPost, action);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Do_like_kol_post do_like_kol_post;

    public Data(@Nullable Do_like_kol_post do_like_kol_post) {
      this.do_like_kol_post = do_like_kol_post;
    }

    public @Nullable Do_like_kol_post do_like_kol_post() {
      return this.do_like_kol_post;
    }

    @Override
    public String toString() {
      return "Data{"
        + "do_like_kol_post=" + do_like_kol_post
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.do_like_kol_post == null) ? (that.do_like_kol_post == null) : this.do_like_kol_post.equals(that.do_like_kol_post));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (do_like_kol_post == null) ? 0 : do_like_kol_post.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Do_like_kol_post.Mapper do_like_kol_postFieldMapper = new Do_like_kol_post.Mapper();

      final Field[] fields = {
        Field.forObject("do_like_kol_post", "do_like_kol_post", new UnmodifiableMapBuilder<String, Object>(2)
          .put("action", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "action")
          .build())
          .put("idPost", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "idPost")
          .build())
        .build(), true, new Field.ObjectReader<Do_like_kol_post>() {
          @Override public Do_like_kol_post read(final ResponseReader reader) throws IOException {
            return do_like_kol_postFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Do_like_kol_post do_like_kol_post = reader.read(fields[0]);
        return new Data(do_like_kol_post);
      }
    }

    public static class Data1 {
      private final @Nullable Integer success;

      public Data1(@Nullable Integer success) {
        this.success = success;
      }

      public @Nullable Integer success() {
        return this.success;
      }

      @Override
      public String toString() {
        return "Data1{"
          + "success=" + success
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.success == null) ? (that.success == null) : this.success.equals(that.success));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (success == null) ? 0 : success.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final Field[] fields = {
          Field.forInt("success", "success", null, true)
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final Integer success = reader.read(fields[0]);
          return new Data1(success);
        }
      }
    }

    public static class Do_like_kol_post {
      private final @Nullable String error;

      private final @Nullable Data1 data;

      public Do_like_kol_post(@Nullable String error, @Nullable Data1 data) {
        this.error = error;
        this.data = data;
      }

      public @Nullable String error() {
        return this.error;
      }

      public @Nullable Data1 data() {
        return this.data;
      }

      @Override
      public String toString() {
        return "Do_like_kol_post{"
          + "error=" + error + ", "
          + "data=" + data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Do_like_kol_post) {
          Do_like_kol_post that = (Do_like_kol_post) o;
          return ((this.error == null) ? (that.error == null) : this.error.equals(that.error))
           && ((this.data == null) ? (that.data == null) : this.data.equals(that.data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (error == null) ? 0 : error.hashCode();
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Do_like_kol_post> {
        final Data1.Mapper data1FieldMapper = new Data1.Mapper();

        final Field[] fields = {
          Field.forString("error", "error", null, true),
          Field.forObject("data", "data", null, true, new Field.ObjectReader<Data1>() {
            @Override public Data1 read(final ResponseReader reader) throws IOException {
              return data1FieldMapper.map(reader);
            }
          })
        };

        @Override
        public Do_like_kol_post map(ResponseReader reader) throws IOException {
          final String error = reader.read(fields[0]);
          final Data1 data = reader.read(fields[1]);
          return new Do_like_kol_post(error, data);
        }
      }
    }
  }
}
